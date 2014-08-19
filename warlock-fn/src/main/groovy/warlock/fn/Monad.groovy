package warlock.fn

import groovy.transform.CompileStatic

@CompileStatic
interface Monad<I,O> extends Applicative<I,O> {
    Monad<I,O> bind(Function<I,Monad<I,O>> function)
}
