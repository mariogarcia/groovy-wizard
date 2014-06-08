package gw.res

import gw.ast.SimpleJSON

@SimpleJSON('/hello-groovy')
class HelloGroovyResource {

    String sayHello() {
        return "Hello from groovy"
    }

}
