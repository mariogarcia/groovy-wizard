package warlock.fn

interface Function<I,O> {
    O apply(I input)
}
