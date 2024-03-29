package me.nullpointer.armazemplus.utils.armazem.supliers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Limit {

    private String key;
    private ItemStack itemStack;
    private Double value;
    private String permission;

    public Limit(String key, ItemStack itemStack, Double value, String permission) {
        this.key = key;
        this.itemStack = itemStack;
        this.value = value;
        this.permission = permission;
    }

    public String getKey() {
        return key;
    }

    public boolean hasPermission(Player player) {
        if (!hasPermission()) return true;
        return player.hasPermission(permission);
    }

    public boolean hasPermission() {
        return permission.equalsIgnoreCase("");
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Double getValue() {
        return value;
    }

    public String getPermission() {
        return permission;
    }
}
