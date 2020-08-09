package me.nullpointer.armazemplus;

import me.nullpointer.armazemplus.api.API;
import me.nullpointer.armazemplus.cache.BonusC;
import me.nullpointer.armazemplus.cache.BoosterC;
import me.nullpointer.armazemplus.cache.DropC;
import me.nullpointer.armazemplus.cache.LimitsC;
import me.nullpointer.armazemplus.commands.CmdBooster;
import me.nullpointer.armazemplus.commands.CmdFriends;
import me.nullpointer.armazemplus.commands.CmdLimit;
import me.nullpointer.armazemplus.commands.CmdSell;
import me.nullpointer.armazemplus.data.dao.ManagerDAO;
import me.nullpointer.armazemplus.enums.DropType;
import me.nullpointer.armazemplus.listeners.*;
import me.nullpointer.armazemplus.utils.Configuration;
import me.nullpointer.armazemplus.utils.Item;
import me.nullpointer.armazemplus.utils.Settings;
import me.nullpointer.armazemplus.utils.Utils;
import me.nullpointer.armazemplus.utils.armazem.supliers.Booster;
import me.nullpointer.armazemplus.utils.armazem.supliers.Drop;
import me.nullpointer.armazemplus.utils.armazem.supliers.Limit;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;
    public static Economy economy = null;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        new API();
        final Configuration configuration = API.getConfiguration();
        configuration.section("Drops").forEach(s -> {
            final String path = "Drops." + s + ".";
            final Drop drop = new Drop(s, DropType.valueOf(configuration.get(path + "type", false)), new ItemStack(configuration.getInt(path + "id"), 1, configuration.getInt(path + "data").shortValue()), configuration.has(path + "mob") ? EntityType.valueOf(configuration.get(path + "mob", false)) : null, configuration.isDouble(path+ "unit-sales-value") ? configuration.getDouble(path+ "unit-sales-value"): Utils.get(configuration.get(path + "unit-sales-value", false).split(",")), new Item(Material.getMaterial(configuration.getInt(path + "drop-item-menu.id")), configuration.getInt(path + "drop-item-menu.amount"), configuration.getInt(path + "drop-item-menu.data").shortValue()).name(configuration.get(path + "drop-item-menu.name", true)).lore(configuration.getList(path + "drop-item-menu.lore", true)), configuration.is(path + "canCollect"), configuration.is(path + "canSell"));
            DropC.put(drop);
        });
        configuration.section("Boosters.list").forEach(s -> {
            final String path = "Boosters.list." + s + ".";
            final Booster booster = new Booster(s, new Item(Material.getMaterial(configuration.getInt(path + "id")), 1, configuration.getInt(path + "data").shortValue()).name(configuration.get(path + "name", true)).lore(configuration.getList(path + "lore", true)).build(), configuration.getDouble(path + "multiplier"), configuration.getLong(path + "time"), configuration.get(path + "permission", false));
            BoosterC.put(booster);
        });
        configuration.section("Limits.list").forEach(s -> {
            final String path = "Limits.list." + s + ".";
            final Limit limit = new Limit(s, new Item(Material.getMaterial(configuration.getInt(path + "id")), 1, configuration.getInt(path + "data").shortValue()).name(configuration.get(path + "name", true)).lore(configuration.getList(path + "lore", true)).build(), configuration.getDouble(path + "value"), configuration.get(path + "permission", false));
            LimitsC.put(limit);
        });
        configuration.section("Bonus").forEach(s -> BonusC.put(s.replace("-", "."), configuration.getInt("Bonus." + s)));
        try {
            final ManagerDAO dao = new ManagerDAO();
            dao.createTable();
            dao.loadAll();
        }catch (Exception ignored){
            Bukkit.getConsoleSender().sendMessage("§ctwArmazem_Plus -> Não foi possível estabelecer conexão com o banco de dados.");
            Bukkit.getConsoleSender().sendMessage("§cO plugin não irá funcionar como desejado.");
        }
        final Settings settings = API.getSettings();
        Bukkit.getPluginManager().registerEvents(new SaveListeners(), this);
        Bukkit.getPluginManager().registerEvents(new Interact(), this);
        Bukkit.getPluginManager().registerEvents(new ChunkLoad(), this);
        Bukkit.getPluginManager().registerEvents(new TalkToCollect(), this);
        if (settings.isBreakBlock()) Bukkit.getPluginManager().registerEvents(new BreakBlock(), this);
        if (settings.isKillMob()) {
            Bukkit.getPluginManager().registerEvents(new KillMob(), this);
            Bukkit.getPluginManager().registerEvents(new AntiLag(), this);
        }
        if (settings.isPlotDrop()) Bukkit.getPluginManager().registerEvents(new SpawnItem(), this);
        ((CraftServer) this.getServer()).getCommandMap().register("limitedevenda", new CmdLimit());
        ((CraftServer) this.getServer()).getCommandMap().register("booster", new CmdBooster());
        ((CraftServer) this.getServer()).getCommandMap().register("vender", new CmdSell());
        if (settings.isFriends())
            ((CraftServer) this.getServer()).getCommandMap().register("armazemfriends", new CmdFriends());
    }

    @Override
    public void onDisable() {
        try {
            new ManagerDAO().saveAll();
        }catch (Exception ignored){
        }
    }
}
