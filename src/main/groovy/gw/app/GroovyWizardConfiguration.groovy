package gw.app

import groovy.transform.CompileStatic

import io.dropwizard.Configuration

@CompileStatic
class GroovyWizardConfiguration extends Configuration {

    static final Map CONFIGURATION =[version: 12]

    Object propertyMissing(String name) {
        CONFIGURATION[name]
    }


}
