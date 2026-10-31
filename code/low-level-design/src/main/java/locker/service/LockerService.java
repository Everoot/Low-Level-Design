package locker.service;

import locker.model.Locker;
import locker.model.Package;
import locker.strategy.LockerStrategy;

import java.util.List;

public class LockerService {
    private List<Locker> lockers;
    private LockerStrategy strategy;

    public LockerService(List<Locker> lockers, LockerStrategy strategy) {
        this.lockers = lockers;
        this.strategy = strategy;
    }

    public Locker assign(Package pkg) {
        Locker locker = strategy.findLocker(lockers, pkg);

        if (locker == null) {
            throw new RuntimeException("No locker available");
        }

        locker.assign(pkg);
        return locker;
    }
}
