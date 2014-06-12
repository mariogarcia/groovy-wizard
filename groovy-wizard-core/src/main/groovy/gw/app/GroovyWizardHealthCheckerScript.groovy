package gw.app

abstract class GroovyWizardHealthCheckerScript extends Script {

    def healthcheck(Closure closure) {
        this.binding.with(closure)
    }

}
