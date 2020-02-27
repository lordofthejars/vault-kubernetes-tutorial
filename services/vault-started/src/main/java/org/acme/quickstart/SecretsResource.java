package org.acme.quickstart;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.vault.VaultTransitSecretEngine;

@Path("/secrets")
public class SecretsResource {

    // tag::transit[]
    @Inject
    public VaultTransitSecretEngine transitSecretEngine; // <1>

    @Path("/encrypt")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String encrypt(String content) {
        return transitSecretEngine.encrypt("my-encryption-key", content); // <2>
    }

    @Path("/decrypt")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String decrypt(String content) {
        return transitSecretEngine.decrypt("my-encryption-key", content).asString(); // <3>
    }
    // end::transit[]

    // tag::vault[]
    @ConfigProperty(name = "weather-api") // <1>
    String weatherApi;

    @Path("/weatherApi")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String weatherApi() {
        return weatherApi;
    }
    // end::vault[]
}