package gw.app

import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

import com.codahale.metrics.health.HealthCheck;

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

abstract class Application<T extends Configuration, M extends Module>
    extends io.dropwizard.Application<T> {

    final Class<M> moduleClazz

    Application(final Class<M> moduleClazz) {
        this.moduleClazz = moduleClazz
    }

    void initialize(Bootstrap<T> bootstrap) { }

    void run(T configuration, Environment environment) {
        register(configuration, environment)
    }

    void register(T configuration, Environment environment) {

        Injector injector = createInjector(configuration, environment)
        ArtifactsHolder holder = injector.getInstance(ArtifactsHolder)

        Closure<?> registerResource = { resource ->
            environment.jersey().register(resource)
        }
        Closure<?> registerHealCheck = { HealthCheck healthCheck ->
            environment.healthChecks().register("name", healthCheck)
        }

        holder.resourceList.each(registerResource)
        holder.healthCheckList.each(registerHealCheck)

    }

    private Injector createInjector(final T conf, final Environment environment) {
        M module = moduleClazz.newInstance([conf, environment] as Object[])
        println module
        return Guice.createInjector(module)
    }

}

