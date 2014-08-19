package warlock.fn

import groovy.transform.CompileStatic

@CompileStatic
interface Functor<I,O> {
    O fmap(Function<I,O> function)
}
