package me.nullpointer.armazemplus.cache;

import com.google.common.collect.Lists;
import me.nullpointer.armazemplus.utils.armazem.supliers.Drop;

import java.util.List;

public class DropC {

    public static List<Drop> drops = Lists.newArrayList();

    public static void put(Drop drop) {
        drops.add(drop);
    }

    public static void remove(Drop drop) {
        drops.remove(drop);
    }

    public static void remove(String key) {
        remove(get(key));
    }

    public static boolean has(String key) {
        return drops.stream().anyMatch(drop -> drop.getKeyDrop().equalsIgnoreCase(key));
    }

    public static Drop get(String key) {
        return drops.stream().filter(drop -> drop.getKeyDrop().equalsIgnoreCase(key)).findFirst().orElseGet(() -> drops.get(0));
    }
}
