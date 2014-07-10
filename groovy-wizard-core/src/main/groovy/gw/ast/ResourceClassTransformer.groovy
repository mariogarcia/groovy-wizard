package gw.ast

import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer

import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.DELETE

import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.expr.ConstantExpression

import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.control.SourceUnit

import java.nio.file.Paths

class ResourceClassTransformer extends ClassCodeExpressionTransformer {

    SourceUnit sourceUnit

    void visitClass(ClassNode classNode) {

        AnnotationNode annotationNode = classNode.getAnnotations(ClassHelper.make(Resource)).first()
        String path = annotationNode.getMember('value').text
        List<MethodNode> allMethods = classNode.methods.findAll { !it.isSynthetic() }

        if (!allMethods) {
            sourceUnit.addError(new SyntaxException('Micro-Service needs at least one method!', 0, 0))
        }

        classNode.addAnnotations([
            buildPathAnnotation(path),
            buildContentTypeAnnotation()
        ])

        classNode.addInterface(ClassHelper.make(gw.app.Resource))
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

