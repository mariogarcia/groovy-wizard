package warlock.fn

interface Functor<I,O> {
    O fmap(Function<I,O> function)
}
