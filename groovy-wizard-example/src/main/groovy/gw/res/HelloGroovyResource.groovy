package gw.res

import gw.ast.Rest
import gw.services.LoggingService

@Rest('/api/greetings')
class HelloGroovyResource {

    String 'GET/hello/{name}'(String name) {
        return "Hello $name"
    }

    String 'GET/bye/{name}'(String name) {
        return "Goodbye $name"
    }

}
