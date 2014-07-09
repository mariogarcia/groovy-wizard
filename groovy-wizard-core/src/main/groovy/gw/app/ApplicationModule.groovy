package gw.app

import gw.jdbi.DBIProvider
import org.skife.jdbi.v2.DBI

import io.dropwizard.Configuration
import io.dropwizard.setup.Environment

abstract class ApplicationModule extends Module {

    final Configuration configuration
    final Environment environment

    ApplicationModule(Configuration configuration, Environment environment) {
       this.configuration = configuration
       this.environment = environment
    }

    void configurePreModule() {

        bind(Configuration).toInstance(configuration)
        bind(Environment).toInstance(environment)

        bind(DBI).toProvider(DBIProvider)
    }

}
