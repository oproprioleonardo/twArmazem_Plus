package me.nullpointer.twarmazemplus.cache.temporary;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CollectC {

    public static HashMap<Player, ItemStack> players = Maps.newHashMap();

    public static void add(Player p, ItemStack drop) {
        players.put(p, drop);
    }

    public static void remove(Player p) {
        players.remove(p);
    }

    public static boolean has(Player p) {
        return players.containsKey(p);
    }

    public static ItemStack getDrop(Player p){
        return players.get(p);
    }


}
