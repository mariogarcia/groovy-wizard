package gw.quotes.app

import gw.app.Module
import gw.quotes.health.QuotesHealthChecksModule
import gw.quotes.resource.QuotesResourcesModule

import io.dropwizard.setup.Environment

import org.skife.jdbi.v2.DBI

class QuotesApplicationModule extends Module {

    final QuotesConfiguration configuration
    final Environment environment

    QuotesApplicationModule(QuotesConfiguration configuration, Environment environment) {
       this.configuration = configuration
       this.environment = environment
    }

    void configureModule() {

        bind(QuotesConfiguration).toInstance(configuration)
        bind(Environment).toInstance(environment)

        bind(DBI).toProvider(DBIProvider)

        install(new QuotesResourcesModule())
        install(new QuotesHealthChecksModule())
    }

}
