package warlock.fn

import groovy.transform.CompileStatic

@CompileStatic
interface Applicative<I,O> extends Functor<I,O> {
    I getValue()
    Applicative<I,O> fapply(Applicative<I,O> afn)
}
