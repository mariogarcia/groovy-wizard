package gw.app

abstract class GroovyWizardResourcesScript extends Script {

    def resources(Closure closure) {
        this.binding.with(closure)
    }

}
