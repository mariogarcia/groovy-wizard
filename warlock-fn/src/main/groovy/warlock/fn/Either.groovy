package warlock.fn

import groovy.transform.CompileStatic

@CompileStatic
class Either<I,O> implements Monad<I,O> {

    static enum Type { RIGHT, LEFT }

    O value
    Type type

    Either fmap(Function<I,O> function) {
        return isLeft() ? this : right(function.apply(value))
    }

    Either fapply(Applicative<Function<I,O>,O> afn) {
        return isLeft() ? this : this.fmap(afn.value)
    }

    Either bind(Function<I,Either<I,O>> function) {
        return isLeft() ? this : function.apply(value)
    }

    Boolean isLeft() {
        return this.type == Type.LEFT
    }

    static <I,O> Either<I,O> right(O value) {
        return new Either<I,O>(value: value, type: Type.RIGHT)
    }

    static <I,O> Either<I,O> left(O value) {
        return new Either<I,O>(value: value, type: Type.LEFT)
    }

}
