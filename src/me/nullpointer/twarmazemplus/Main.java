package me.nullpointer.twarmazemplus;

import me.nullpointer.twarmazemplus.api.API;
import me.nullpointer.twarmazemplus.cache.BoosterCACHE;
import me.nullpointer.twarmazemplus.cache.DropCACHE;
import me.nullpointer.twarmazemplus.cache.LimitsCACHE;
import me.nullpointer.twarmazemplus.enums.DropType;
import me.nullpointer.twarmazemplus.utils.*;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        new API();
        final Configuration configuration = API.getConfiguration();
        loadDrops(configuration);
        loadBoosters(configuration);
        loadLimits(configuration);
    }

    @Override
    public void onDisable() {

    }

    public static Main getInstance() {
        return instance;
    }

    public void loadDrops(Configuration configuration) {
        configuration.section("Drops").forEach(s -> {
            final String path = "Drops." + s + ".";
            final Drop drop = new Drop(s, DropType.valueOf(configuration.get(path+"type", false)), new ItemStack(configuration.getInt(path + "id"), 1, configuration.getInt(path + "data").shortValue()), configuration.has(path + "mob") ? EntityType.valueOf(configuration.get(path + "mob", false)): null, Utils.get(configuration.get(path + "unit-sales-price", false).split(",")), new Item(Material.getMaterial(configuration.getInt(path + "drop-item-menu.id")), configuration.getInt(path + "drop-item-menu.amount"), configuration.getInt(path + "drop-item-menu.data").shortValue()).name(configuration.get(path + "drop-item-menu.name", true)).lore(configuration.getList(path + "drop-item-menu.lore", true)));
            DropCACHE.put(drop);
        });
    }

    public void loadBoosters(Configuration configuration) {
        configuration.section("Boosters.list").forEach(s -> {
            final String path = "Boosters.list." + s + ".";
            final Booster booster = new Booster(s, new Item(Material.getMaterial(configuration.getInt(path + "id")), 1, configuration.getInt(path+"data").shortValue()).name(configuration.get(path+"name", true)).lore(configuration.getList(path+"lore", true)).build(), configuration.getDouble(path+"multiplier"), configuration.getLong(path+"time"), configuration.get(path + "permission", false));
            BoosterCACHE.put(booster);
        });
    }

    public void loadLimits(Configuration configuration) {
        configuration.section("Limits.list").forEach(s -> {
            final String path = "Limits.list." + s + ".";
            final Limit limit = new Limit(s, new Item(Material.getMaterial(configuration.getInt(path + "id")), 1, configuration.getInt(path+"data").shortValue()).name(configuration.get(path+"name", true)).lore(configuration.getList(path+"lore", true)).build(), configuration.getDouble(path+"value"), configuration.get(path + "permission", false));
            LimitsCACHE.put(limit);
        });
    }
}
