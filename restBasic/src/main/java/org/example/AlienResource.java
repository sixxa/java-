package org.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("aliens")
public class AlienResource {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Alien getAlien() {
        System.out.println("get alien called");
        Alien a1 = new Alien();
        a1.setName("Toka");
        a1.setPoints(100);
        System.out.println("Returning alien: " + a1.getName() + ", Points: " + a1.getPoints());
        return a1;
    }
}
