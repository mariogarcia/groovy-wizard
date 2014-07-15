package gw.ast

import static org.codehaus.groovy.ast.ClassHelper.make

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.GenericsType
import org.codehaus.groovy.ast.VariableScope
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ConstructorNode
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer

import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement

import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression

import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.CompilePhase

import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.AbstractASTTransformation

import gw.ast.Configuration
import gw.ast.ApplicationModule

@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
class ApplicationAst extends AbstractASTTransformation {

    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        if (nodes.length != 2) return
        if (!(nodes[0] instanceof AnnotationNode)) return
        if (!(nodes[1] instanceof ClassNode)) return
        if (nodes[0].classNode.name != Application.class.name) return

        AnnotationNode annotationNode = nodes[0]
        ClassNode classNode = nodes[1]

        createDropWizardApplicationArtifact(annotationNode, classNode)
    }

    void createDropWizardApplicationArtifact(final AnnotationNode annotationNode, final ClassNode classNode) {
        ClassNode configurationClassNode     = getClassNodeFromMember(annotationNode, 'configuration')
        ClassNode applicationModuleClassNode = getClassNodeFromMember(annotationNode, 'module')
        ClassNode applicationClassNode       = make(gw.app.Application).plainNodeReference
        GenericsType[] genericsTypes = [ new GenericsType(configurationClassNode) ]

        applicationClassNode.genericsTypes = genericsTypes

        classNode.superClass = applicationClassNode
        classNode.usingGenerics = true
        classNode.addConstructor(getDefaultConstructorWithSuperCall(applicationModuleClassNode))

    }

    ClassNode getClassNodeFromMember(final AnnotationNode annotationNode, String member) {
        return make(annotationNode.getMember(member).text)
    }

    ConstructorNode getDefaultConstructorWithSuperCall(final ClassNode applicationModuleClassNode) {
        ConstructorNode constructorNode =
            new ConstructorNode(
                ACC_PUBLIC,
                [] as Parameter[] ,
                [] as ClassNode[],
                new BlockStatement(
                    [
                        new ExpressionStatement(
                            new ConstructorCallExpression(
                                ClassNode.SUPER,
                                new ArgumentListExpression(
                                    new ClassExpression(applicationModuleClassNode)
                                )
                            )
                        )
                    ],
                    new VariableScope()
                )
            )

        return constructorNode
    }

}
