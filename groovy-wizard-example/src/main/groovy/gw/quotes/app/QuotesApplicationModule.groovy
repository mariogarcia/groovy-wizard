package gw.quotes.app

import gw.app.ApplicationModule

import gw.quotes.health.QuotesHealthChecksModule
import gw.quotes.resource.QuotesResourcesModule

import groovy.transform.InheritConstructors

@InheritConstructors
class QuotesApplicationModule extends ApplicationModule {
    void configureModule() {
        install(new QuotesResourcesModule())
        install(new QuotesHealthChecksModule())
    }
}
