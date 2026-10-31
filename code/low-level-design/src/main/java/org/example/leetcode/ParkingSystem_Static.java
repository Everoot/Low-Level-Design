package org.example.leetcode;

/**
 * @Author: eve
 * @Date: 2026-10-03 10:48
 */
public class ParkingSystem_Static {

    private static int big;
    private static int medium;
    private static int small;

    public static void ParkingSystem_Static(int big, int medium, int small) {
        ParkingSystem_Static.big = big;
        ParkingSystem_Static.medium = medium;
        ParkingSystem_Static.small = small;
    }

    public static boolean addCar(int carType) {
        if (carType == 1) {
            if (big > 0) {
                big--;
                return true;
            } else {
                return false;
            }
        } else if (carType == 2) {
            if (medium > 0) {
                medium--;
                return true;
            } else {
                return false;
            }
        } else if (carType == 3) {
            if (small > 0) {
                small--;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        ParkingSystem_Static parkingSystem = new ParkingSystem_Static();
        parkingSystem.ParkingSystem_Static(1, 1, 0);
        System.out.println(parkingSystem.addCar(1)); // return true because there is 1 available slot for a big car
        System.out.println(parkingSystem.addCar(2)); // return true because there is 1 available slot for a medium car
        System.out.println(parkingSystem.addCar(3)); // return false because there is no available slot for a small car
        System.out.println(parkingSystem.addCar(1)); // return false because there is no available slot for a big car. It is already occupied.
    }
}
