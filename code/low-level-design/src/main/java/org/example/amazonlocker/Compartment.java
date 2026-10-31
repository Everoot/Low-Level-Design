package org.example.amazonlocker;

/**
 * @Author: eve
 * @Date: 2026-10-12 15:35
 */
public class Compartment {
    private final String id;
    private final Size size;
    private boolean occupied;

    public Compartment(String id, Size size) {
        this.id = id;
        this.size = size;
        this.occupied = false;
    }

    public Size getSize() {
        return size;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void markOccupied() {
        this.occupied = true;
    }

    public void markFree(){
        this.occupied = false;
    }

    public void open(){
        // unlock physical compartment door
    }
}
