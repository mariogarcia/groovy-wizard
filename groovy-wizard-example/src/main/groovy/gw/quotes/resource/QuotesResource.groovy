package gw.quotes.resource

import gw.ast.Resource
import gw.ast.Inject

import gw.quotes.service.QuotesService

@Resource('/api/')
class QuotesResource {

    @Inject QuotesService service

    def 'GET/quotes/{id}'(Long id) {
        return service.get(id)
    }

    def 'GET/quotes'() {
        return service.list()
    }

    def 'POST/quotes'(String quote) {
        service.save(quote)
        return "quote"
    }
}
