package gw.app

import org.skife.jdbi.v2.DBI

import io.dropwizard.Configuration
import io.dropwizard.setup.Environment

abstract class ApplicationModule<T extends Configuration>  extends Module {

    final T configuration
    final Environment environment

    ApplicationModule(T configuration, Environment environment) {
       this.configuration = configuration
       this.environment = environment
    }

    void configurePreModule() {

        bind(QuotesConfiguration).toInstance(configuration)
        bind(Environment).toInstance(environment)

        bind(DBI).toProvider(DBIProvider)
    }

}
