package locker.strategy;

import locker.model.Locker;
import locker.model.Package;

import java.util.List;

public interface LockerStrategy {
    Locker findLocker(List<Locker> lockers, Package pkg);
}
