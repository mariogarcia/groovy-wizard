package gw.quotes.app

import gw.app.Application

class QuotesApplication extends Application<QuotesConfiguration> {

    QuotesApplication() {
        super(QuotesApplicationModule)
    }

    static void main(String[] args) {
        new QuotesApplication().run(args)
    }

}

