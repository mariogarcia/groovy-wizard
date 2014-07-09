package gw.quotes.app

import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory

import javax.validation.Valid
import javax.validation.constraints.NotNull

class QuotesConfiguration extends Configuration {

    @Valid
    @NotNull
    DataSourceFactory database = new DataSourceFactory()

}
