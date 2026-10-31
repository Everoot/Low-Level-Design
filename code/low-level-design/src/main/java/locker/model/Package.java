package locker.model;

import locker.enums.LockerSize;

public class Package {
    private String id;
    private LockerSize size;

    public Package(String id, LockerSize size) {
        this.id = id;
        this.size = size;
    }

    public LockerSize getSize() {
        return size;
    }
}
