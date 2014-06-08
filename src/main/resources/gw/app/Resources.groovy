import gw.res.HelloWorldResource

resources {
    return [
        new HelloWorldResource("${configuration.version}")
    ]
}

