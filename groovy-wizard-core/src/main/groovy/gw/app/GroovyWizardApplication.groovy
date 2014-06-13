package gw.app

import groovy.transform.CompileStatic
import org.codehaus.groovy.control.CompilerConfiguration

import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

import com.codahale.metrics.health.HealthCheck;

@CompileStatic
class GroovyWizardApplication extends Application<GroovyWizardConfiguration> {

    static void main(String[] args) {
        new GroovyWizardApplication().run(['server'] as String[])
    }

    void initialize(Bootstrap<GroovyWizardConfiguration> bootstrap) { }

    void run(GroovyWizardConfiguration configuration, Environment environment) {
        loadResources(configuration, environment).each { resource ->
           environment.jersey().register(resource)
        }

		loadHealthChekers(configuration, environment).each { healthcheck ->
			environment.healthChecks().register("template",(HealthCheck)healthcheck)
		}
    }

    List<?> loadResources(
        GroovyWizardConfiguration configuration,
        Environment environment) {

        InputStream resourcesStream = getClass().getResourceAsStream('/gw/app/Resources.groovy')
        Reader resourcesReader = new InputStreamReader(resourcesStream)
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.setScriptBaseClass(GroovyWizardResourcesScript.class.name)
        Binding bindings = new Binding()

        bindings.setVariable('configuration', configuration)
        bindings.setVariable('environment', environment)

        GroovyShell shell = new GroovyShell(getClass().classLoader, bindings, compilerConfiguration)

        return (List<?>) shell.evaluate(resourcesReader)

    }


    List<?> loadHealthChekers(
        GroovyWizardConfiguration configuration,
        Environment environment) {

        InputStream resourcesStream = getClass().getResourceAsStream('/gw/app/HealthCheckers.groovy')
        Reader resourcesReader = new InputStreamReader(resourcesStream)
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.setScriptBaseClass(GroovyWizardHealthCheckerScript.class.name)
        Binding bindings = new Binding()

        bindings.setVariable('configuration', configuration)
        bindings.setVariable('environment', environment)

        GroovyShell shell = new GroovyShell(getClass().classLoader, bindings, compilerConfiguration)

        return (List<?>) shell.evaluate(resourcesReader)

    }

}
