package gw.ast

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.AnnotationNode

import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.CompilePhase

import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.AbstractASTTransformation

@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
class ConfigurationAst extends AbstractASTTransformation {

    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        if (!validate(nodes)) return

        addConfigurationAnnotationTo(nodes[1])
    }

    boolean validate(ASTNode[] nodes) {
        if (nodes.length != 2) return false
        if (!(nodes[0] instanceof AnnotationNode)) return false
        if (nodes[0].classNode.name != Configuration.class.name) return false
        if (!(nodes[1] instanceof ClassNode)) return false

        return true
    }

    void addConfigurationAnnotationTo(ClassNode annotatedNode) {
        annotatedNode.setSuperClass(ClassHelper.make(io.dropwizard.Configuration))
    }

}
