package gw.ast

import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.CompilePhase

import org.codehaus.groovy.transform.GroovyASTTransformation


@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
class RestResourceTransformationsAst extends RestAnnotatedClassNodeAst {

    RestResourceTransformationsAst() {
        super(
            Arrays.asList(
                new RestResourceClassTransformer(),
                new RestResourceMethodTransformer()
            )
        )
    }

}
