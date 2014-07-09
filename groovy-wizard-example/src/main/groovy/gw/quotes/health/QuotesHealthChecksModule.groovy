package gw.quotes.health

import gw.app.Module

class QuotesHealthChecksModule extends Module {

    void configureModule() {
        bindHealthCheck().to(HelloHealthCheck)
    }

}
