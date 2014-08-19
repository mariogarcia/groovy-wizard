package warlock.fn

interface Applicative<I,O> {
    I getValue()
    Applicative<I,O> fapply(Applicative<I,O> afn)
}
