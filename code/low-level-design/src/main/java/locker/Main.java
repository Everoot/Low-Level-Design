package locker;

import locker.enums.LockerSize;
import locker.model.Locker;
import locker.model.Package;
import locker.service.LockerService;
import locker.strategy.FirstAvailableLockerStrategy;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Locker l1 = new Locker("L1", LockerSize.SMALL);
        Locker l2 = new Locker("L2", LockerSize.MEDIUM);

        LockerService service = new LockerService(
                List.of(l1, l2),
                new FirstAvailableLockerStrategy()
        );

        Package pkg = new Package("P1", LockerSize.SMALL);

        Locker assigned = service.assign(pkg);

        System.out.println("Assigned locker size: " + assigned.getSize());
    }
}
