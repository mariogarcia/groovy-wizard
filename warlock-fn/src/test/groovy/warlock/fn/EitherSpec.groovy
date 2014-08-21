package warlock.fn

import static warlock.fn.Either.left
import static warlock.fn.Either.right

import spock.lang.Unroll
import spock.lang.Specification

class EitherSpec extends Specification {

    static final Closure<String> TO_UPPER = { String word -> word.toUpperCase() }

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
        given: 'a left Either instance'
            Either<String,String> eitherInstance = left('no processable')
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


}
