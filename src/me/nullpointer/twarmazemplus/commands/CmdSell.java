package me.nullpointer.twarmazemplus.commands;

import me.nullpointer.twarmazemplus.Main;
import me.nullpointer.twarmazemplus.api.API;
import me.nullpointer.twarmazemplus.cache.DropC;
import me.nullpointer.twarmazemplus.cache.PlayerC;
import me.nullpointer.twarmazemplus.enums.translate.ItemName;
import me.nullpointer.twarmazemplus.utils.Configuration;
import me.nullpointer.twarmazemplus.utils.InventoryBuilder;
import me.nullpointer.twarmazemplus.utils.Item;
import me.nullpointer.twarmazemplus.utils.Utils;
import me.nullpointer.twarmazemplus.utils.armazem.Armazem;
import me.nullpointer.twarmazemplus.utils.armazem.DropPlayer;
import me.nullpointer.twarmazemplus.utils.armazem.supliers.Drop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CmdSell extends BukkitCommand {

    public CmdSell() {
        super("vender", "Comando de vender drops", "/vender", Arrays.asList("armazem", "drops", "sell"));
    }

    @Override
    public boolean execute(CommandSender s, String lbl, String[] args) {
        if (s instanceof Player){
            final Configuration configuration = API.getConfiguration();
            final Player p = (Player) s;
            final Armazem armazem = PlayerC.get(p.getName());
            final InventoryBuilder inventoryBuilder = new InventoryBuilder(Main.getInstance(), configuration.getInt("Inventory.sell.rows"), configuration.get("Inventory.sell.name", true));
            final List<Integer> slots = new ArrayList<>();
            Arrays.asList(configuration.get("Inventory.sell.slotsDropSell", true).split(",")).forEach(s1 -> slots.add(Integer.parseInt(s1)));
            inventoryBuilder.setCancellable(true);
            inventoryBuilder.clear();
            String path = "Inventory.sell.items.playerStats.";
            inventoryBuilder.setItem(configuration.getInt(path + "slot"), new Item(Material.getMaterial(configuration.getInt(path + "id")), 1, configuration.getInt(path + "data").shortValue()).name(configuration.get(path + "name", true).replace("{reference-player}", p.getName())).setSkullUrl(configuration.get(path + "skull-url", false)).setSkullOwner(configuration.get(path + "skull-owner", true).replace("{reference-player}", p.getName())).lore(configuration.getList(path + "lore", true)));
            path = "Inventory.sell.items.sellAll.";
            inventoryBuilder.setItem(configuration.getInt(path + "slot"), new Item(Material.getMaterial(configuration.getInt(path + "id")), 1, configuration.getInt(path + "data").shortValue()).name(configuration.get(path + "name", true).replace("{reference-player}", p.getName())).setSkullUrl(configuration.get(path + "skull-url", false)).setSkullOwner(configuration.get(path + "skull-owner", true).replace("{reference-player}", p.getName())).lore(configuration.getList(path + "lore", true)).onClick(inventoryClickEvent -> {
                Utils.sendActionBar(p, configuration.getMessage("drops-sell").replace("{amount}", Utils.format(armazem.getAmountAll())).replace("{price}", Utils.format(armazem.getPriceAll())));
                armazem.getDropPlayers().forEach(dropPlayer -> {
                    dropPlayer.sell(p);
                });
                Bukkit.dispatchCommand(p, "vender");
            }));
            path = "Inventory.sell.items.autoSell.";
            inventoryBuilder.setItem(configuration.getInt(path + "slot"), new Item(Material.getMaterial(configuration.getInt(path + "id")), 1, configuration.getInt(path + "data").shortValue()).name(configuration.get(path + "name", true).replace("{reference-player}", p.getName())).setSkullUrl(configuration.get(path + "skull-url", false)).setSkullOwner(configuration.get(path + "skull-owner", true).replace("{reference-player}", p.getName())).lore(configuration.getList(path + "lore", true)).onClick(inventoryClickEvent -> {

            }));
            final List<DropPlayer> dropsPlayer = armazem.getDropPlayers().stream().filter(dropPlayer -> dropPlayer.getDropAmount() > 0).collect(Collectors.toList());
            for (int count = 0; count < slots.size(); count++){
                if (dropsPlayer.get(count) == null) break;
                final DropPlayer dropPlayer = dropsPlayer.get(count);
                final Drop drop = DropC.get(dropPlayer.getKeyDrop());
                final List<String> lore = drop.getMenuItem().getItemMeta().getLore();
                inventoryBuilder.setItem(count, new Item(drop.getMenuItem().build()).lore(lore.stream().map(s1 -> s1.replace("{price-sell-unit}", Utils.format(drop.getUnitPrice())).replace("{amount}", Utils.format(dropPlayer.getDropAmount())).replace("{price-sell-all}", Utils.format(drop.getUnitPrice() * dropPlayer.getDropAmount()))).collect(Collectors.toList())).onClick(inventoryClickEvent -> {
                    Utils.sendActionBar(p, configuration.getMessage("drops-sell").replace("{amount}", Utils.format(dropPlayer.getDropAmount())).replace("{item}", ItemName.valueOf(drop.getDrop()).getName()).replace("{price}", Utils.format(dropPlayer.getDropAmount() * drop.getUnitPrice())));
                    dropPlayer.sell(p);
                    Bukkit.dispatchCommand(p, "vender");
                }));
            }
            inventoryBuilder.open(p);
        }
        return true;
    }
}
