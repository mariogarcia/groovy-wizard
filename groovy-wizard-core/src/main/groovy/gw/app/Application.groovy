package gw.app

import gw.app.Module

import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

import com.codahale.metrics.health.HealthCheck;

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

abstract class Application<T extends Configuration> extends io.dropwizard.Application<T> {

    final Class<? extends Module> moduleClazz

    Application(final Class<? extends Module> moduleClazz) {
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
        Module module = moduleClazz.newInstance([conf, environment] as Object[])
        return Guice.createInjector(module)
    }

}

