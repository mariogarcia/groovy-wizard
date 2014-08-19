package gw.quotes.service

import gw.ast.Inject
import gw.quotes.dao.QuotesDao

import org.skife.jdbi.v2.DBI

class QuotesService {

    @Inject DBI jdbi

    Map get(Long id) {
        return jdbi.onDemand(QuotesDao).get(id)
    }

    List<Map> list() {
        return jdbi.onDemand(QuotesDao).list()
    }

    int save(Long id, String quote) {
        return jdbi.onDemand(QuotesDao).save(id, quote)
    }


}
