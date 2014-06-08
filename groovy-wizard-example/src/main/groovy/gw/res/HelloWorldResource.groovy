package gw.res

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path('/hello-world')
@Produces(MediaType.APPLICATION_JSON)
class HelloWorldResource {

    final String version

    HelloWorldResource(final String applicationVersion) {
        this.version = applicationVersion
    }

    @GET
    String sayHello() {
        return "This application has version [${this.version}]"
    }

}
