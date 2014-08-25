package warlock.fn

import groovy.transform.ToString
import groovy.transform.CompileStatic

@ToString
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

    Either either(Function<I,O> inCaseRight, Function<I,O> inCaseLeft) {
        try {
            return isLeft() ? this.fmap(inCaseLeft) : this.fmap(inCaseRight)
        } catch(e) {
            return this.fmap(inCaseLeft)
        }
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
