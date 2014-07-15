package gw.quotes.app

import gw.ast.Application

@Application(
    module=QuotesApplicationModule,
    configuration=QuotesConfiguration
)
class QuotesApplication {

    static void main(String[] args) {
        new QuotesApplication().run(args)
    }

}

