package gw.ast

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.AbstractASTTransformation

@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
class SimpleJSONAst extends AbstractASTTransformation {
    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        if(nodes?.count():0 < 2) return
        if(!(nodes[0] instanceof AnnotationNode)) return
        if(!(nodes[1] instanceof ClassNode)) return

        AnnotationNode annotationNode = nodes[0]
        ClassNode annotatedClassNode = nodes[1]

        String path = getMember('value').text
        List<MethodNode> allMethods = annotatedClassNode.allDeclaredMethods

        if (!allMethods) {
            sourceUnit.addError(new SyntaxException('Micro-Service needs at least one method!'))
        }
        if (allMethods?.size() > 1) {
            sourceUnit.addError(new SyntaxException('Micro-Service ambiguity: Only one method allowed!'))
        }

        annotatedClassNode.addAnnotations([
            buildPathAnnotation(path),
            buildContentTypeAnnotation()
        ])

        MethodNode methodNode = allMethods.first()
        methodNode.addAnnotation(
            new AnnotationNode(
                ClassHelper.make(javax.ws.rs.GET)
            )
        )

    }

    AnnotationNode buildPathAnnotation(String path) {
        AnnotationNode annotationNode =
            new AnnotationNode(javax.ws.rs.Path)

        annotationNode.addMember('value', new ConstantExpression(path))

        return annotationNode
    }

    AnnotationNode buildContentTypeAnnotation() {
        AnnotationNode annotationNode =
            new AnnotationNode(javax.ws.rs.Produces)

        annotationNode.addMember(
            'value',
            new PropertyExpression(
                new ClassHelper.make(javax.ws.rs.core.MediaType),
                new ConstantExpression('MEDIA_JSON')
            )
        )
        //javax.ws.rs.core.MediaType.APPLICATION_JSON
        return annotationNode
    }
}
