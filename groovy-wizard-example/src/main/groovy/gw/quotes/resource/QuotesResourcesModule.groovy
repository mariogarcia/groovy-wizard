package gw.quotes.resource

import gw.app.Module

class QuotesResourcesModule extends Module {

    void configureModule() {
        bindResource().to(QuotesResource)
    }

}
