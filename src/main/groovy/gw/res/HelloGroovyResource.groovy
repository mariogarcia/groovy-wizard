package gw.res

import gw.ast.SimpleJSON

@SimpleJSON('GET/hello-groovy')
class HelloWorldResource {

    String sayHello() {
        return "This application has version [${configuration.version}]"
    }

}
