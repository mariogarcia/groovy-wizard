package gw.ast

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer

import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation

abstract class ResourceAnnotatedClassNodeAst extends AbstractASTTransformation {

    final List<ClassCodeExpressionTransformer> transformerList

    ResourceAnnotatedClassNodeAst(List<ClassCodeExpressionTransformer> transformerList) {
       this.transformerList = transformerList
    }

    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        sourceUnit
            .AST
            .classes
            .each { ClassNode classNode ->
                if (classNode.getAnnotations(ClassHelper.make(Resource))) {
                    transformerList.each { ClassCodeExpressionTransformer transformer ->
                        transformer.sourceUnit = sourceUnit
                        transformer.visitClass(classNode)
                    }
                }
            }
    }

}

