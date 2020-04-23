package me.nullpointer.twarmazemplus.cache;

import me.nullpointer.twarmazemplus.utils.armazem.supliers.Limit;

import java.util.ArrayList;
import java.util.List;

public class LimitsC {

    public static List<Limit> limits = new ArrayList<>();

    public static void put(Limit limit) {
        limits.add(limit);
    }

    public static void remove(Limit limit) {
        limits.remove(limit);
    }

    public static void remove(String key) {
        remove(get(key));
    }

    public static boolean has(String key) {
        return limits.stream().anyMatch(limit -> limit.getKey().equalsIgnoreCase(key));
    }

    public static Limit get(String key) {
        return limits.stream().filter(limit -> limit.getKey().equalsIgnoreCase(key)).findFirst().orElseGet(() -> limits.get(0));
    }
}
