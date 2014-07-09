package gw.quotes.app

import gw.ast.Inject

import com.google.inject.Provider
import com.google.inject.Provides
import com.google.inject.Singleton

import io.dropwizard.setup.Environment

import org.skife.jdbi.v2.DBI
import io.dropwizard.jdbi.DBIFactory

@Singleton
class DBIProvider implements Provider<DBI> {

    @Inject QuotesConfiguration configuration
    @Inject Environment environment

    DBI get() {
        return new DBIFactory().build(environment, configuration.database, 'postgresql')
    }

}
