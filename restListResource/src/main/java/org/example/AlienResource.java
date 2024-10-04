package org.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("aliens")
public class AlienResource {

    AlienRepository repo = new AlienRepository();

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Alien> getAliens() {

        System.out.println("get alien called");

        return repo.getAliens();
    }
    @POST
    @Path("alien")
    public Alien createAlien(Alien a1) {
        System.out.println(a1);
        repo.create(a1);
        return a1;
    }
}
