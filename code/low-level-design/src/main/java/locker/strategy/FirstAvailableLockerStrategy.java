package locker.strategy;

import locker.model.Locker;
import locker.model.Package;

import java.util.List;

public class FirstAvailableLockerStrategy implements LockerStrategy {
    @Override
    public Locker findLocker(List<Locker> lockers, Package pkg) {
        for (Locker locker : lockers) {
            if (locker.isAvailable()) {
                return locker;
            }
        }
        return null;
    }
}
