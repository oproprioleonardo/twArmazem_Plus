package me.nullpointer.armazemplus.commands;

import me.nullpointer.armazemplus.Main;
import me.nullpointer.armazemplus.api.API;
import me.nullpointer.armazemplus.cache.PlayerC;
import me.nullpointer.armazemplus.utils.Configuration;
import me.nullpointer.armazemplus.utils.InventoryBuilder;
import me.nullpointer.armazemplus.utils.Item;
import me.nullpointer.armazemplus.utils.armazem.Armazem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdFriends extends BukkitCommand {


    public CmdFriends() {
        super("armazemfriends", "Sistema de amigos", "/armazemfriends", Arrays.asList("friendsarmazem", "armazemamigos"));
    }

    public void openFriendsInventory(Player p, Armazem armazem, int page) {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(Main.getInstance(), 3, "§8Amigos");
        final List<String> friends = armazem.getFriends();
        inventoryBuilder.clear();
        inventoryBuilder.setCancellable(true);
        inventoryBuilder.setItem(4, new Item(Material.getMaterial(397), 1, (short) 3).setSkullOwner(p.getName()).name("§e" + p.getName()).lore("§fAmigos: " + friends.size()));
        final List<Integer> slots = Arrays.asList(11, 12, 13, 14, 15);
        final double pages = Math.ceil(friends.size() / 5.0);
        if (page > pages || page <= 0) {
            p.sendMessage("§cEssa página não existe.");
            return;
        }
        if (page != pages) {
            inventoryBuilder.setItem(16, new Item(Material.SKULL_ITEM, 1, (short) 3).name("§eAvançar").lore("§7Clique para ir a página " + (page + 1)).setSkullUrl("http://textures.minecraft.net/texture/715445da16fab67fcd827f71bae9c1d2f90c73eb2c1bd1ef8d8396cd8e8").onClick(inventoryClickEvent -> openFriendsInventory(p, armazem, page + 1)));
        }
        if (page != 1) {
            inventoryBuilder.setItem(10, new Item(Material.SKULL_ITEM, 1, (short) 3).name("§eVoltar").lore("§7Clique para ir a página " + (page - 1)).setSkullUrl("http://textures.minecraft.net/texture/eed78822576317b048eea92227cd85f7afcc44148dcb832733baccb8eb56fa1").onClick(inventoryClickEvent -> openFriendsInventory(p, armazem, page - 1)));
        }
        final int calculate = page - 1;
        final List<String> subListSpawners = friends.subList(calculate, Math.min(calculate + 5, friends.size()));
        int count = 0;
        while (count != subListSpawners.size()) {
            if (subListSpawners.get(count) != null && Bukkit.getOfflinePlayer(subListSpawners.get(count)) != null) {
                final OfflinePlayer player = Bukkit.getOfflinePlayer(subListSpawners.get(count));
                inventoryBuilder.setItem(slots.get(count), new Item(Material.getMaterial(397), 1, (short) 3).name("§f" + player.getName()).setSkullOwner(player.getName()).lore("§fEsse jogador está " + (player.isOnline() ? "§aOnline" : "§fOffline") + "§f.", "§cClique para remover da lista de amigos.").onClick(inventoryClickEvent -> {
                    p.closeInventory();
                    p.sendMessage(API.getConfiguration().getMessage("friends-remove").replace("{player}", p.getName()));
                    final List<String> lista = new ArrayList<>(armazem.getFriends());
                    lista.remove(player.getName().toLowerCase());
                    armazem.setFriends(lista);
                }));
                count++;
            }
        }
        inventoryBuilder.open(p);
    }

    @Override
    public boolean execute(CommandSender s, String lbl, String[] args) {
        if (s instanceof Player) {
            final Player p = (Player) s;
            final Configuration configuration = API.getConfiguration();
            if (args.length == 0) {
                final Armazem armazem = PlayerC.get(p.getName());
                if (armazem.getFriends().size() > 0) {
                    openFriendsInventory(p, armazem, 1);
                } else p.sendMessage(configuration.getMessage("friends-0"));
            } else if (args.length == 1) {
                p.sendMessage("§aComandos Armazém - Sistema de amigos");
                p.sendMessage("");
                p.sendMessage("§a/armazemfriends - Acessa a lista de amigos.");
                p.sendMessage("§a/armazemfriends ajuda - Mostra os comandos do sistema");
                p.sendMessage("§a/armazemfriends add (player) - Adiciona um jogador a lista de amigos.");
                p.sendMessage("§a/armazemfriends remove (player) - Remove um jogador da lista de amigos.");
            } else if (args.length == 2) {
                if (Bukkit.getPlayer(args[1]) == null) {
                    p.sendMessage(configuration.getMessage("player-offline"));
                    return true;
                }
                final Armazem armazem = PlayerC.get(p.getName());
                if (args[0].equalsIgnoreCase("add")) {
                    if (armazem.getFriends().contains(args[1].toLowerCase())) return true;
                    if (args[1].equalsIgnoreCase(p.getName())) return true;
                    if (armazem.getFriends().size() == API.getSettings().getFriendsMax()) {
                        p.sendMessage(configuration.getMessage("friends-max"));
                        return true;
                    }
                    armazem.getFriends().add(args[1].toLowerCase());
                    p.sendMessage(configuration.getMessage("friends-add").replace("{player}", p.getName()));
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (!armazem.getFriends().contains(args[1].toLowerCase())) return true;
                    final ArrayList<String> friends = new ArrayList<>(armazem.getFriends());
                    friends.remove(args[1].toLowerCase());
                    armazem.setFriends(friends);
                    p.sendMessage(configuration.getMessage("friends-remove").replace("{player}", p.getName()));
                }
            }
        }
        return true;
    }
}
