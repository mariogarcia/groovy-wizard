package gw.hc

import gw.app.Module

class HelloHealthCheckModule extends Module {

    void configureModule() {
        bindHealthCheck().to(HelloHealthCheck)
    }

}
