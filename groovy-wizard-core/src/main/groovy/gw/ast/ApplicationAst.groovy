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

import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression

import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation

import gw.ast.Configuration
import gw.ast.ApplicationModule

class ApplicationAst extends AbstractASTTransformation {

    final List<ClassCodeExpressionTransformer> transformerList

    final Closure<Boolean> classAnnotatedWithApplication =  { ClassNode node ->
        return node.getAnnotations(make(Application))
    }

    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        ClassNode applicationClassNode =
            sourceUnit
                .AST
                .classes
                .find(classAnnotatedWithApplication)

        createDropWizardApplicationArtifact(sourceUnit, applicationClassNode)
    }

    void createDropWizardApplicationArtifact(final SourceUnit sourceUnit, final ClassNode classNode) {
        ClassNode configurationClassNode     = findFirstNodeAnnotatedByClass(sourceUnit, Configuration)
        ClassNode applicationModuleClassNode = findFirstNodeAnnotatedByClass(sourceUnit, ApplicationModule)
        ClassNode applicationClassNode       = make(Application)

        applicationClassNode.setGenericsTypes([new GenericsType(make(configurationClassNode))] as GenericsType[])
        classNode.superClass = applicationClassNode
        classNode.addConstructor(getDefaultConstructorWithSuperCall(applicationModuleClassNode))

    }

    ClassNode findFirstNodeAnnotatedByClass(final SourceUnit sourceUnit, final Class type) {
        return sourceUnit.AST.classes.find(annotationOfType(type))
    }

    Closure<Boolean> annotationOfType = { Class type ->
        return { ClassNode classNode ->
            classNode.getAnnotations(make(type))
        }
    }

    ConstructorNode getDefaultConstructorWithSuperCall(final ClassNode applicationModuleClassNode) {
        ConstructorNode constructorNode =
            new ConstructorNode(
                ACC_PUBLIC,
                [] as Parameter[] ,
                [] as ClassNode[],
                new BlockStatement(
                    new ExpressionStatement(
                        new ConstructorCallExpression(
                            ClassNode.SUPER,
                            new ArgumentListExpression(
                                new ConstantExpression(applicationModuleClassNode)
                            )
                        )
                    ),
                    new VariableScope()
                )
            )

        return constructorNode
    }

}
