package gw.hc

import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheck.Result

public class HelloHealthCheck extends HealthCheck {

    final String template;

    HelloHealthCheck() {
        this.template = 'template';
    }

    Result check() throws Exception {
        final String saying =
            String.format(template, "TEST")

        return !saying.contains("TEST") ?
                    Result.unhealthy("template doesn't include a name") :
                    Result.healthy();
    }
}
