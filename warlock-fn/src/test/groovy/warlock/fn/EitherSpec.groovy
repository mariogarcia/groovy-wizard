package warlock.fn

import static warlock.fn.Either.left
import static warlock.fn.Either.right

import spock.lang.Unroll
import spock.lang.Specification

class EitherSpec extends Specification {

    static final Integer ONE = 1
    static final Closure<String> TO_UPPER = { String word -> word.toUpperCase() }
    static final Function<Integer,Either<Integer>> F = { it * 2 }
    static final Function<Integer,Either<Integer>> G = { it * 5 }
    static final Function<Integer,Either<Integer>> F_THEN_G = {
        G.apply(F.apply(it))
    }

    @Unroll void 'fmap'() {
        given: 'a left Either instance'
            Either<String,String> eitherInstance = left('no processable')
        when: 'trying to transform the value'
            Either<String,String> result = instance.fmap(TO_UPPER)
        then: 'the value should match the expected result'
            result.value == value
        where: 'possible cases are'
            instance              | value
            right('processable')  | 'PROCESSABLE'
            left('unprocessable') | 'unprocessable'
    }

    @Unroll void 'fapply'() {
        when: 'trying apply an applicative'
            Either<String,String> result = instance.fapply(applicativeFn)
        then: 'the value should match the expected result'
            result.value == value
        where: 'possible cases are'
            instance               | applicativeFn   | value
            right('processable')   | right(TO_UPPER) | 'PROCESSABLE'
            right('unprocessable') | left(TO_UPPER)  | 'unprocessable'
            left('unprocessable')  | left(TO_UPPER)  | 'unprocessable'
            left('unprocessable')  | right(TO_UPPER) | 'unprocessable'
    }

    void 'first law: left identity'() {
        expect: 'to follow the rule'
            right(ONE).fmap(F).value == F.apply(ONE)
    }

    void 'second law: right identity'() {
        expect: 'to follow the rule'
            right(ONE).fmap(F).value == right(F.apply(ONE)).value
    }

    void 'third law: associativity'() {
        expect: 'to follow the rule'
            right(ONE).fmap(F).fmap(G).value == right(ONE).fmap(F_THEN_G).value
    }

    @Unroll void 'using Either'() {
        expect: 'calling a method'
            doComplicatedCalculation(
                Either.right(sample),
                function
            ).value == result
        where: 'possible values are'
            sample | function     | result
            'a'    | { it + 2 }   | 'a2'
            2      | { it + 'a' } | '2a'
            2      | { it.div(0) }| 2
    }

    Either<?,?> doComplicatedCalculation(Either<?,?> input, Function<?,?> fn) {
        try {
            return input.fmap(fn)
        } catch(e) {
            return Either.left(input.value)
        }
    }


}
