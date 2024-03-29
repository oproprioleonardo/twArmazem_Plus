package me.nullpointer.armazemplus.commands;

import me.nullpointer.armazemplus.Main;
import me.nullpointer.armazemplus.api.API;
import me.nullpointer.armazemplus.cache.DropC;
import me.nullpointer.armazemplus.cache.PlayerC;
import me.nullpointer.armazemplus.cache.temporary.CollectC;
import me.nullpointer.armazemplus.enums.translate.ItemName;
import me.nullpointer.armazemplus.utils.Configuration;
import me.nullpointer.armazemplus.utils.InventoryBuilder;
import me.nullpointer.armazemplus.utils.Item;
import me.nullpointer.armazemplus.utils.Utils;
import me.nullpointer.armazemplus.utils.armazem.Armazem;
import me.nullpointer.armazemplus.utils.armazem.DropPlayer;
import me.nullpointer.armazemplus.utils.armazem.supliers.Drop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CmdSell extends BukkitCommand {

    public CmdSell() {
        super("armazem", "Comando de vender drops", "/armazem", Arrays.asList("vender", "drops", "sell"));
    }

    @Override
    public boolean execute(CommandSender s, String lbl, String[] args) {
        if (s instanceof Player) {
            final Player p = (Player) s;
            final Configuration configuration = API.getConfiguration();
            if (args.length == 0) {
                final Armazem armazem = PlayerC.get(p.getName());
                openSellInventory(p, armazem, configuration);
            } else if (args.length == 1) {
                if (PlayerC.exists(args[0])) {
                    final Armazem armazem = PlayerC.get(args[0]);
                    if (armazem.getFriends().stream().anyMatch(s1 -> s1.equalsIgnoreCase(p.getName())) || args[0].equalsIgnoreCase(p.getName())) {
                        openSellInventory(p, armazem, configuration);
                        return true;
                    }
                }
                p.sendMessage(configuration.getMessage("drops-no-friend"));
            }
        }
        return true;
    }

    public void openSellInventory(Player p, Armazem armazem, Configuration configuration) {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(Main.getInstance(), configuration.getInt("Inventory.sell.rows"), configuration.get("Inventory.sell.name", true));
        final List<Integer> slots = new ArrayList<>();
        Arrays.asList(configuration.get("Inventory.sell.slotsDropSell", true).split(",")).forEach(s1 -> slots.add(Integer.parseInt(s1)));
        inventoryBuilder.setCancellable(true);
        inventoryBuilder.clear();
        String path = "Inventory.sell.items.playerStats.";
        inventoryBuilder.setItem(configuration.getInt(path + "slot"), new Item(Material.getMaterial(configuration.getInt(path + "id")), 1, configuration.getInt(path + "data").shortValue()).name(configuration.get(path + "name", true).replace("{playername}", armazem.getCorrectNameOwner())).setSkullUrl(configuration.get(path + "skull-url", false)).setSkullOwner(configuration.get(path + "skull-owner", true).replace("{playername}", armazem.getCorrectNameOwner())).lore(configuration.getList(path + "lore", true).stream().map(s1 -> s1.replace("{limit}", Utils.format(armazem.getLimit())).replace("{bonus}", armazem.getBonus().toString()).replace("{multiplier}", armazem.getMultiplier().toString())).collect(Collectors.toList())));
        path = "Inventory.sell.items.sellAll.";
        inventoryBuilder.setItem(configuration.getInt(path + "slot"), new Item(Material.getMaterial(configuration.getInt(path + "id")), 1, configuration.getInt(path + "data").shortValue()).name(configuration.get(path + "name", true).replace("{playername}", armazem.getCorrectNameOwner())).setSkullUrl(configuration.get(path + "skull-url", false)).setSkullOwner(configuration.get(path + "skull-owner", true).replace("{playername}", armazem.getCorrectNameOwner())).lore(configuration.getList(path + "lore", true).stream().map(s1 -> s1.replace("{drops}", Utils.format(armazem.getAmountAll())).replace("{total}", Utils.format(armazem.getPriceAll() + (armazem.getPriceAll() * armazem.getBonus() / 100)))).collect(Collectors.toList())).onClick(inventoryClickEvent -> {
            if (!p.hasPermission("armazem.sellall")) {
                return;
            }
            if (armazem.getAmountAll() > 0) {
                Utils.sendActionBar(p, configuration.getMessage("drops-sellall").replace("{amount}", Utils.format(armazem.getAmountAll())).replace("{price}", Utils.format(armazem.getPriceAll() + (armazem.getPriceAll() * armazem.getBonus() / 100))));
                armazem.getDropPlayers().stream().filter(dropPlayer -> DropC.get(dropPlayer.getKeyDrop()).isCanSell()).forEach(dropPlayer -> dropPlayer.sell(p, armazem));
            }
            Bukkit.dispatchCommand(p, "armazem " + armazem.getOwner());
        }));
        path = "Inventory.sell.items.autoSell.";
        inventoryBuilder.setItem(configuration.getInt(path + "slot"), new Item(Material.getMaterial(configuration.getInt(path + "id")), 1, configuration.getInt(path + "data").shortValue()).name(configuration.get(path + "name", true).replace("{playername}", p.getName())).setSkullUrl(configuration.get(path + "skull-url", false)).setSkullOwner(configuration.get(path + "skull-owner", true).replace("{playername}", p.getName())).lore(configuration.getList(path + "lore", true).stream().map(s1 -> s1.replace("{state}", armazem.isAutoSellResult()).replace("{future-state}", "" + (!armazem.isAutoSell() ? "ativar" : "desativar"))).collect(Collectors.toList())).onClick(inventoryClickEvent -> {
            if (!p.hasPermission("armazem.autosell")) {
                return;
            }
            armazem.setAutoSell(!armazem.isAutoSell());
            Bukkit.dispatchCommand(p, "armazem " + armazem.getOwner());
        }));
        final List<DropPlayer> dropsPlayer = armazem.getDropPlayers().stream().filter(dropPlayer -> dropPlayer.getDropAmount() > 0).collect(Collectors.toList());
        if (dropsPlayer.size() != 0) {
            for (int count = 0; count < slots.size(); count++) {
                if (dropsPlayer.size() == count) break;
                final DropPlayer dropPlayer = dropsPlayer.get(count);
                final Drop drop = DropC.get(dropPlayer.getKeyDrop());
                final List<String> lore = drop.getMenuItem().getItemMeta().getLore();
                inventoryBuilder.setItem(slots.get(count), new Item(drop.getMenuItem().build().clone()).lore(lore.stream().map(s1 -> s1.replace("{price-sell-unit}", Utils.format(drop.getUnitPrice() + (drop.getUnitPrice() * armazem.getBonus() / 100))).replace("{amount}", Utils.format(dropPlayer.getDropAmount())).replace("{price-sell-all}", Utils.format(drop.getUnitPrice() * dropPlayer.getDropAmount() + (drop.getUnitPrice() * dropPlayer.getDropAmount() * armazem.getBonus() / 100)))).collect(Collectors.toList())).onClick(inventoryClickEvent -> {
                    if (dropPlayer.getDropAmount() > 0) {
                        if ((inventoryClickEvent.getClick() == ClickType.RIGHT && drop.isCanCollect()) || (drop.isCanCollect() && !drop.isCanSell())){
                            p.sendMessage(configuration.getMessage("can-collect"));
                            CollectC.add(p, drop.getDrop());
                            return;
                        }
                        Utils.sendActionBar(p, configuration.getMessage("drops-sell").replace("{amount}", Utils.format(dropPlayer.getDropAmount())).replace("{item}", ItemName.valueOf(drop.getDrop()).getName()).replace("{price}", Utils.format(dropPlayer.getDropAmount() * drop.getUnitPrice() + (dropPlayer.getDropAmount() * drop.getUnitPrice() * armazem.getBonus() / 100))));
                        dropPlayer.sell(p, armazem);
                    }
                    Bukkit.dispatchCommand(p, "armazem " + armazem.getOwner());
                }));
            }
        }
        inventoryBuilder.open(p);
    }
}
