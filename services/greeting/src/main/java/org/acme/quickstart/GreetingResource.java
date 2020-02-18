package org.acme.quickstart;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/hello")
public class GreetingResource {

    @ConfigProperty(name = "username")
    String username;

    @GET
    @Path("/env")
    @Produces(MediaType.TEXT_PLAIN)
    public String username() {
        return "hello " + username;
    }
}