package warlock.fn

import groovy.transform.CompileStatic

@CompileStatic
interface Function<I,O> {
    O apply(I input)
}
