package me.nullpointer.twarmazemplus;

import me.nullpointer.twarmazemplus.api.API;
import me.nullpointer.twarmazemplus.cache.BoosterC;
import me.nullpointer.twarmazemplus.cache.DropC;
import me.nullpointer.twarmazemplus.cache.LimitsC;
import me.nullpointer.twarmazemplus.commands.CmdBooster;
import me.nullpointer.twarmazemplus.commands.CmdLimit;
import me.nullpointer.twarmazemplus.commands.CmdSell;
import me.nullpointer.twarmazemplus.data.dao.ManagerDAO;
import me.nullpointer.twarmazemplus.enums.DropType;
import me.nullpointer.twarmazemplus.listeners.*;
import me.nullpointer.twarmazemplus.utils.*;
import me.nullpointer.twarmazemplus.utils.armazem.supliers.Booster;
import me.nullpointer.twarmazemplus.utils.armazem.supliers.Drop;
import me.nullpointer.twarmazemplus.utils.armazem.supliers.Limit;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;
    public static Economy economy = null;

    @Override
    public void onEnable() {
        instance = this;
        economy = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class).getProvider();
        new API();
        final Configuration configuration = API.getConfiguration();
        loadDrops(configuration);
        loadBoosters(configuration);
        loadLimits(configuration);
        final ManagerDAO dao = new ManagerDAO();
        dao.createTable();
        dao.loadAll();
        final Settings settings = API.getSettings();
        Bukkit.getPluginManager().registerEvents(new SaveEvents(), this);
        Bukkit.getPluginManager().registerEvents(new Interact(), this);
        Bukkit.getPluginManager().registerEvents(new ChunkLoad(), this);
        if (settings.isBreakBlock()) Bukkit.getPluginManager().registerEvents(new BreakBlock(), this);
        if (settings.isKillMob()) {
            Bukkit.getPluginManager().registerEvents(new KillMob(), this);
            Bukkit.getPluginManager().registerEvents(new AntiLag(), this);
        }
        if (settings.isPlotDrop()) Bukkit.getPluginManager().registerEvents(new SpawnItem(), this);
        ((CraftServer) this.getServer()).getCommandMap().register("limitedevenda", new CmdLimit());
        ((CraftServer) this.getServer()).getCommandMap().register("booster", new CmdBooster());
        ((CraftServer)this.getServer()).getCommandMap().register("vender", new CmdSell());
    }

    @Override
    public void onDisable() {
        new ManagerDAO().saveAll();
    }

    public static Main getInstance() {
        return instance;
    }

    public void loadDrops(Configuration configuration) {
        configuration.section("Drops").forEach(s -> {
            final String path = "Drops." + s + ".";
            final Drop drop = new Drop(s, DropType.valueOf(configuration.get(path+"type", false)), new ItemStack(configuration.getInt(path + "id"), 1, configuration.getInt(path + "data").shortValue()), configuration.has(path + "mob") ? EntityType.valueOf(configuration.get(path + "mob", false)): null, Utils.get(configuration.get(path + "unit-sales-value", false).split(",")), new Item(Material.getMaterial(configuration.getInt(path + "drop-item-menu.id")), configuration.getInt(path + "drop-item-menu.amount"), configuration.getInt(path + "drop-item-menu.data").shortValue()).name(configuration.get(path + "drop-item-menu.name", true)).lore(configuration.getList(path + "drop-item-menu.lore", true)));
            DropC.put(drop);
        });
    }

    public void loadBoosters(Configuration configuration) {
        configuration.section("Boosters.list").forEach(s -> {
            final String path = "Boosters.list." + s + ".";
            final Booster booster = new Booster(s, new Item(Material.getMaterial(configuration.getInt(path + "id")), 1, configuration.getInt(path+"data").shortValue()).name(configuration.get(path+"name", true)).lore(configuration.getList(path+"lore", true)).build(), configuration.getDouble(path+"multiplier"), configuration.getLong(path+"time"), configuration.get(path + "permission", false));
            BoosterC.put(booster);
        });
    }

    public void loadLimits(Configuration configuration) {
        configuration.section("Limits.list").forEach(s -> {
            final String path = "Limits.list." + s + ".";
            final Limit limit = new Limit(s, new Item(Material.getMaterial(configuration.getInt(path + "id")), 1, configuration.getInt(path+"data").shortValue()).name(configuration.get(path+"name", true)).lore(configuration.getList(path+"lore", true)).build(), configuration.getDouble(path+"value"), configuration.get(path + "permission", false));
            LimitsC.put(limit);
        });
    }
}
