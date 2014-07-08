package gw.res

import gw.app.Module
import gw.services.GreetingsService
import gw.services.GreetingsServiceImpl

class HelloGroovyModule extends Module {

    void configureModule() {
        bind(GreetingsService).to(GreetingsServiceImpl)

        bindResource().to(HelloGroovyResource)
    }

}
