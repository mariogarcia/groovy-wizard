import gw.res.HelloWorldResource
import gw.res.HelloGroovyResource

resources {
    return [
        new HelloWorldResource("${configuration.version}"),
        new HelloGroovyResource()
    ]
}

