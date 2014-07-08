package gw.my

import gw.ast.Inject
import gw.app.Resource
import com.codahale.metrics.health.HealthCheck

class MyHolder {

    @Inject Set<Resource> resourceList
    @Inject Set<HealthCheck> healthCheckList

}
