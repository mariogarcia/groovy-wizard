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

import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.control.SourceUnit

import java.nio.file.Paths

class RestResourceMethodTransformer extends ClassCodeExpressionTransformer {

    SourceUnit sourceUnit
    Map<String, Class> METHOD_ANNOTATION = [
        GET: GET,
        POST: POST,
        PUT: PUT,
        DELETE: DELETE,
    ]

    void visitClass(ClassNode classNode) {
        classNode.methods.each { MethodNode method ->

            def completeURI = Paths.get(method.name)
            def httpMethod = completeURI.subpath(0, 1).toString()
            def uri = method.name - httpMethod

            Class annotationType = METHOD_ANNOTATION[httpMethod]

            if (httpMethod && uri) {
                checkParametersInUri(uri, method.parameters)
               method
                    .addAnnotations([
                        buildHttpMethodAnnotation(httpMethod),
                        buildPathMethodAnnotation(uri)
                    ])
            }
        }
    }

    AnnotationNode buildHttpMethodAnnotation(String method) {
        Class annotationType = METHOD_ANNOTATION[method]
        AnnotationNode annotationNode=
            new AnnotationNode(ClassHelper.make(annotationType))

        return annotationNode
    }

    AnnotationNode buildPathMethodAnnotation(String uri) {
        AnnotationNode annotation =
            new AnnotationNode(ClassHelper.make(javax.ws.rs.Path))

        annotation.
            addMember('value', new ConstantExpression(uri))

        return annotation
    }

    void checkParametersInUri(String uri, Parameter[] parameters) {
        parameters.each { Parameter p ->
            if (uri.contains("{${p.name}}")) {
                AnnotationNode annotation =
                    new AnnotationNode(ClassHelper.make(javax.ws.rs.PathParam))
                    annotation.addMember('value', new ConstantExpression(p.name))
                p.addAnnotation(annotation)
            }
        }
    }

}
