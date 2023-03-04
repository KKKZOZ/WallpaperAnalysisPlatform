package org.jff.Entity;

public enum SearchBy {
    IS_LATEST(1),
    IS_HOTTEST(2),
    IS_BEST(3);

    private final int type;

    SearchBy(int type) {
        this.type = type;
    }
}
