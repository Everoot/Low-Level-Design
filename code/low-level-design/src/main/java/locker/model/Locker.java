package locker.model;

import locker.enums.LockerSize;

public class Locker {
    private String id;
    private LockerSize size;
    private Package pkg;

    public Locker(String id, LockerSize size) {
        this.id = id;
        this.size = size;
    }

    public boolean isAvailable() {
        return pkg == null;
    }

    public void assign(Package pkg) {
        this.pkg = pkg;
    }

    public Package pickup() {
        Package result = pkg;
        pkg = null;
        return result;
    }

    public LockerSize getSize() {
        return size;
    }
}
