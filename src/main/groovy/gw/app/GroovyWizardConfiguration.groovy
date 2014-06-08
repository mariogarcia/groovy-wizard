package gw.app

import groovy.transform.CompileStatic

import io.dropwizard.Configuration

@CompileStatic
class GroovyWizardConfiguration extends Configuration {

    Map configuration = [:]

    Object propertyMissing(final String name) {
        configuration[name] ?: getFreshValue(name)
    }

    Object getFreshValue(final String key) {
        URL resourceURL =
            this.getClass().classLoader.getResource('gw/app/Config.groovy')
        configuration = new ConfigSlurper().parse(resourceURL)
        return configuration[key]
    }

}
