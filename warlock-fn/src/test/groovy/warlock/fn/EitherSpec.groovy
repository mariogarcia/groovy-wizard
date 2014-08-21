package warlock.fn

import static warlock.fn.Either.left
import static warlock.fn.Either.right

import spock.lang.Specification

class EitherSpec extends Specification {

    void 'Creating a left instance'() {
        given: 'a left Either instance'
            Either<String,String> eitherInstance = left('no processable')
        when: 'trying to do something with it'
            Either<String,String> result = eitherInstance.fmap(toUpper)
        then: 'the value should remain unchanged'
            result.value == 'no processable'
    }

    Closure<String> toUpper = { String word -> word.toUpperCase() }

    void 'Creating a right instance'() {
        given: 'a right Either instance'
            Either<String,String> eitherInstance = right('processable')
        when: 'trying to do something with it'
            Either<String,String> result = eitherInstance.fmap(toUpper)
        then: 'the value should change'
            result.value == 'PROCESSABLE'


    }

}
