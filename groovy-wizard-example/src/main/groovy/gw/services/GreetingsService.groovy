package gw.services

import groovy.util.logging.Slf4j

@Slf4j
class GreetingsService {

    String sayHelloTo(String name) {
        log.info("saying hello to $name")
        return "Hello $name"
    }

    String sayGoodbyeTo(String name) {
        log.info("saying goodbye to $name")
        return "Goodbye $name"
    }

}
