package gw.ast

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.VariableScope
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ConstructorNode
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement

import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.expr.PropertyExpression

import org.codehaus.groovy.syntax.Token
import org.codehaus.groovy.syntax.Types

import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.CompilePhase

import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.AbstractASTTransformation

@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
class InjectAst extends AbstractASTTransformation {

    Closure<Boolean> fieldAnnotatedWithInject = { FieldNode node ->
        node.getAnnotations(ClassHelper.make(Inject))
    }

    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        sourceUnit
            .AST
            .classes
            .each { ClassNode classNode ->
                List<FieldNode> fields = classNode.fields
                if (fields.any(fieldAnnotatedWithInject)) {
                    createInjectableConstructor(
                        classNode,
                        fields.findAll(fieldAnnotatedWithInject)
                    )
                }
            }
    }

    void createInjectableConstructor(
        final ClassNode annotatedClassNode,
        final List<FieldNode> fields) {

        List<Parameter> parameters = fields.collect(parameterFromField)
        ConstructorNode constructorNode =
            new ConstructorNode(
                ACC_PUBLIC,
                parameters as Parameter[] ,
                [] as ClassNode[],
                new BlockStatement(
                    parameters.collect { buildAssignationStatement(it.name)},
                    new VariableScope()
                )
            )

        constructorNode.addAnnotation(buildGuiceInjectAnnotation())
        annotatedClassNode.addConstructor(constructorNode)

    }

    AnnotationNode buildGuiceInjectAnnotation() {
        new AnnotationNode(
            ClassHelper.make(com.google.inject.Inject)
        )
    }

    ExpressionStatement buildAssignationStatement(String propertyName) {
        return new ExpressionStatement(
            new BinaryExpression(
                new PropertyExpression(
                    new VariableExpression("this"),
                    new ConstantExpression(propertyName)
                ),
                new Token(Types.EQUALS, '=', -1, -1),
                new VariableExpression(propertyName)
            )
        )
    }

    Closure<Parameter> parameterFromField = { FieldNode node ->
        new Parameter(node.type, node.name)
    }

}
