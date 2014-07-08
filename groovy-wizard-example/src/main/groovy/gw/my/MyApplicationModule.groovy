package gw.my

import gw.app.Module
import gw.res.HelloGroovyModule
import gw.hc.HelloHealthCheckModule

class MyApplicationModule extends Module {

    final MyConfiguration configuration

    MyApplicationModule(MyConfiguration configuration) {
       this.configuration = configuration
    }

    void configureModule() {
        bind(MyConfiguration).toInstance(configuration)

        install(new HelloGroovyModule())
        install(new HelloHealthCheckModule())
    }

}
