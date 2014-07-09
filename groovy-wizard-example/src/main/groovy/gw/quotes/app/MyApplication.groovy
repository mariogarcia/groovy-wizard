package gw.quotes.app

import groovy.transform.CompileStatic

import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

import com.codahale.metrics.health.HealthCheck;

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

@CompileStatic
class MyApplication extends Application<MyConfiguration> {

    static void main(String[] args) {
        new MyApplication().run(args)
    }

    void initialize(Bootstrap<MyConfiguration> bootstrap) { }

    void run(MyConfiguration configuration, Environment environment) {
        register(configuration, environment)
    }

    void register(MyConfiguration configuration, Environment environment) {

        Injector injector = createInjector(configuration, environment)
        MyHolder holder = injector.getInstance(gw.quotes.app.MyHolder)
        Closure<?> registerResource = { resource ->
            environment.jersey().register(resource)
        }
        Closure<?> registerHealCheck = { HealthCheck healthCheck ->
            environment.healthChecks().register("name", healthCheck)
        }

        holder.resourceList.each(registerResource)
        holder.healthCheckList.each(registerHealCheck)

    }

    private Injector createInjector(final MyConfiguration conf, final Environment environment) {
        return Guice.createInjector(new MyApplicationModule(conf, environment))
    }

}

