package gw.ast

import static org.codehaus.groovy.ast.ClassHelper.make

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.VariableScope
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ConstructorNode

import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression

import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement

import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.CompilePhase

import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.AbstractASTTransformation

import io.dropwizard.Configuration
import io.dropwizard.setup.Environment

@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
class ApplicationModuleAst extends AbstractASTTransformation {

    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        if (!validate(nodes)) return

        nodes[1].with { annotatedClassNode ->
            addConfigurationAnnotationTo(annotatedClassNode)
            addNonDefaultConstructorTo(annotatedClassNode)
        }
    }

    boolean validate(ASTNode[] nodes) {
        if (nodes.length != 2) return false
        if (!(nodes[0] instanceof AnnotationNode)) return false
        if (nodes[0].classNode.name != ApplicationModule.class.name) return false
        if (!(nodes[1] instanceof ClassNode)) return false

        return true
    }

    void addConfigurationAnnotationTo(ClassNode annotatedNode) {
        annotatedNode.setSuperClass(ClassHelper.make(gw.app.ApplicationModule))
    }

    void addNonDefaultConstructorTo(ClassNode annotatedNode) {
        ConstructorNode constructorNode =
            new ConstructorNode(
                ACC_PUBLIC,
                constructorParameters,
                [] as ClassNode[],
                block(
                    ctorExpression(
                        args(
                            getSuperCallArgumentsFrom(
                                constructorParameters))))
            )

        annotatedNode.addConstructor(constructorNode)
    }

    ArgumentListExpression args(Expression... expressions) {
        return new ArgumentListExpression(expressions)
    }

    ExpressionStatement ctorExpression(Expression args) {
        new ExpressionStatement(
            new ConstructorCallExpression(ClassNode.SUPER, args)
        )
    }

    BlockStatement block(Statement... statements) {
        return new BlockStatement(statements, new VariableScope())
    }

    Expression[] getSuperCallArgumentsFrom(Parameter[] parameters) {
        return parameters.collect { Parameter p ->
           varX(p.name, p.type)
        }
    }

    VariableExpression varX(String name, ClassNode type) {
        return new VariableExpression(name, type)
    }

    Parameter[] getConstructorParameters() {
        return [
            new Parameter(make(Configuration), 'configuration'),
            new Parameter(make(Environment), 'environment')
        ]
    }

}
