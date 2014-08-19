package gw.app

import com.google.inject.Inject
import com.codahale.metrics.health.HealthCheck

class ArtifactsHolder {

    @Inject Set<Resource> resourceList
    @Inject Set<HealthCheck> healthCheckList

}
