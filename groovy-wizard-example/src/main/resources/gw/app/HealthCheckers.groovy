import gw.hc.HelloWorldHealthChecker

healthcheck {
    return [
        new HelloWorldHealthChecker("${configuration.template}")
    ]
}

// import gw.hc.HelloGroovyHealthChecker
//		,new HelloGroovyHealthChecker()

