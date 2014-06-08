package gw.ast

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.expr.ClassExpression
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
        if (!nodes) return
        if(nodes.length < 2) return
        if(!(nodes[0] instanceof AnnotationNode)) return
        if(!(nodes[0].classNode.name == SimpleJSON.class.name)) return
        if(!(nodes[1] instanceof ClassNode)) return

        AnnotationNode annotationNode = nodes[0]
        ClassNode annotatedClassNode = nodes[1]

        String path = annotationNode.getMember('value').text
        List<MethodNode> allMethods = annotatedClassNode.methods.findAll { !it.isSynthetic() }

        if (!allMethods) {
            sourceUnit.addError(new SyntaxException('Micro-Service needs at least one method!', 0, 0))
        }
        if (allMethods?.size() > 1) {
            sourceUnit.addError(new SyntaxException('Micro-Service ambiguity: Only one method allowed!',0, 0))
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
            new AnnotationNode(ClassHelper.make(javax.ws.rs.Path))
        annotationNode.addMember('value', new ConstantExpression(path))
        return annotationNode
    }

    AnnotationNode buildContentTypeAnnotation() {
        AnnotationNode annotationNode =
            new AnnotationNode(ClassHelper.make(javax.ws.rs.Produces))
        annotationNode.addMember(
            'value',
            new PropertyExpression(
                new ClassExpression(ClassHelper.make(javax.ws.rs.core.MediaType)),
                new ConstantExpression('APPLICATION_JSON')
            )
        )
        return annotationNode
    }
}
