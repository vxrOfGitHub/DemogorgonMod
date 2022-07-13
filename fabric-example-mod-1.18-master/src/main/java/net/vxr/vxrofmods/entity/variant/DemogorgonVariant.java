package net.vxr.vxrofmods.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum DemogorgonVariant {
    DEFAULT(0),
    DARK(1),
    GREY(2),
    RED(3);

    private static final DemogorgonVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(DemogorgonVariant::getId)).toArray(DemogorgonVariant[]::new);
    private final int id;

    DemogorgonVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static DemogorgonVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
