package org.example.amazonlocker;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: eve
 * @Date: 2026-10-12 15:39
 */
public class Locker {

    private final List<Compartment> compartments;

    private final Map<String, AccessToken> tokens = new HashMap<>();

    public Locker(List<Compartment> compartments) {
        this.compartments = compartments;
    }

    public String depositPackage(Size size) {
        Compartment compartment = findAvailable(size);

        if (compartment == null){
            throw new RuntimeException("No available compartment");
        }

        compartment.open();
        compartment.markOccupied();

        AccessToken token = generateToken(compartment);

        tokens.put(token.getCode(), token);

        return token.getCode();

    }

    private Compartment findAvailable(Size size) {
        for (Compartment c : compartments){
            if (c.getSize() == size && !c.isOccupied()){
                return c;
            }
        }

        return null;
    }


    private AccessToken generateToken(Compartment compartment) {
        String code = UUID.randomUUID().toString();

        Instant expiration = Instant.now().plusSeconds(3600); // 1 hour expiration

        return new AccessToken(code, expiration, compartment);
    }

    public void pickup(String code) {
        AccessToken token = tokens.get(code);


        if (token == null){
            throw new RuntimeException("Invalid access code");
        }

        if (token.isExpired()){
            throw new RuntimeException("Access code expired");
        }

        Compartment compartment = token.getCompartment();

        compartment.open();
        compartment.markFree();

        tokens.remove(code);
    }

}
