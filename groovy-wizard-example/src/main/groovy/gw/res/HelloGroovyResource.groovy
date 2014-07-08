package gw.res

import gw.ast.Rest
import gw.ast.Inject

import gw.services.GreetingsService

@Rest('/api/greetings')
class HelloGroovyResource {

    @Inject GreetingsService service

    String 'GET/hello/{name}'(String name) {
        return service.sayHelloTo(name)
    }

    String 'GET/bye/{name}'(String name) {
        return service.sayGoodbyeTo(name)
    }

}
