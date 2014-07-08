package gw.my

import groovy.transform.CompileStatic

import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

import com.codahale.metrics.health.HealthCheck;

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

import gw.res.HelloGroovyResource

@CompileStatic
class MyApplication extends Application<MyConfiguration> {

    static void main(String[] args) {
        new MyApplication().run(['server'] as String[])
    }

    void initialize(Bootstrap<MyConfiguration> bootstrap) { }

    void run(MyConfiguration configuration, Environment environment) {
        Injector injector = createInjector(configuration)
        MyHolder holder = injector.getInstance(gw.my.MyHolder)
        Closure<?> registerResource = { resource ->
            environment.jersey().register(resource)
        }
        Closure<?> registerHealCheck = { HealthCheck healthCheck ->
            environment.healthChecks().register("name", healthCheck)
        }

        holder.resourceList.each(registerResource)
        holder.healthCheckList.each(registerHealCheck)
    }

    private Injector createInjector(final MyConfiguration conf) {
        return Guice.createInjector(new MyApplicationModule(conf))
    }

}

