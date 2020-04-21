package me.nullpointer.twarmazemplus.utils;

import org.bukkit.inventory.ItemStack;

public class Booster {

    private String key;
    private ItemStack item;
    private Double multiplier;
    private Long time;
    private String permission;

    public Booster(String key, ItemStack item, Double multiplier, Long time, String permission) {
        this.key = key;
        this.item = item;
        this.multiplier = multiplier;
        this.time = time;
        this.permission = permission;
    }

    public String getKey() {
        return key;
    }

    public ItemStack getItem() {
        return item;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public Long getTime() {
        return time;
    }

    public String getPermission() {
        return permission;
    }
}
