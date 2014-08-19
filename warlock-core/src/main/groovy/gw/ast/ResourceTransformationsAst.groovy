package gw.ast

import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.CompilePhase

import org.codehaus.groovy.transform.GroovyASTTransformation


@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
class ResourceTransformationsAst extends ResourceAnnotatedClassNodeAst {

    ResourceTransformationsAst() {
        super(
            Arrays.asList(
                new ResourceClassTransformer(),
                new ResourceMethodTransformer()
            )
        )
    }

}
