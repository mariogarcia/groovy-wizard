package gw.app

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import com.google.inject.binder.LinkedBindingBuilder

import com.codahale.metrics.health.HealthCheck

abstract class Module extends AbstractModule {

    private Multibinder<Resource> resourceBinder
    private Multibinder<HealthCheck> healthCheckBinder

    void configure() {
        resourceBinder = Multibinder.newSetBinder(binder(), Resource)
        healthCheckBinder = Multibinder.newSetBinder(binder(), HealthCheck)
        configureModule()
    }

    abstract void configureModule()

    final LinkedBindingBuilder<Resource> bindResource() {
        return resourceBinder.addBinding()
    }

    final LinkedBindingBuilder<HealthCheck> bindHealthCheck() {
        return healthCheckBinder.addBinding()
    }

}
