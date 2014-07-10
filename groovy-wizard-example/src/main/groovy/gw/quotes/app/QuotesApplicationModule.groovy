package gw.quotes.app

import gw.ast.ApplicationModule

import gw.quotes.health.QuotesHealthChecksModule
import gw.quotes.resource.QuotesResourcesModule

@ApplicationModule
class QuotesApplicationModule {

    void configureModule() {
        install(new QuotesResourcesModule())
        install(new QuotesHealthChecksModule())
    }

}
