package me.nullpointer.twarmazemplus.cache;

import com.google.common.collect.Lists;
import me.nullpointer.twarmazemplus.api.API;
import me.nullpointer.twarmazemplus.data.dao.ManagerDAO;
import me.nullpointer.twarmazemplus.utils.Configuration;
import me.nullpointer.twarmazemplus.utils.armazem.Armazem;
import me.nullpointer.twarmazemplus.utils.armazem.DropPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerC {

    public static List<Armazem> armazens = Lists.newArrayList();

    public static void put(Armazem armazem) {
        armazens.add(armazem);
    }

    public static void remove(Armazem armazem) {
        new ManagerDAO().save(armazem);
        armazens.remove(armazem);
    }

    public static void load(Player p) {
        if (!exists(p.getName())) {
            final ManagerDAO dao = new ManagerDAO();
            if (dao.exists(p.getName().toLowerCase())) {
                dao.load(p.getName().toLowerCase());
                final Armazem armazem = get(p.getName());
                DropC.drops.forEach(drop -> {
                    if (armazem.getDropPlayers().stream().noneMatch(dropPlayer -> dropPlayer.getKeyDrop().equalsIgnoreCase(drop.getKeyDrop()))) {
                        final List<DropPlayer> list = new ArrayList<>(armazem.getDropPlayers());
                        list.add(new DropPlayer(drop.getKeyDrop(), 0D));
                        armazem.setDropPlayers(list);
                    }
                });
            } else {
                final Configuration configuration = API.getConfiguration();
                final List<DropPlayer> drops = new ArrayList<>();
                DropC.drops.forEach(drop -> drops.add(new DropPlayer(drop.getKeyDrop(), 0D)));
                final Armazem armazem = new Armazem(p.getName().toLowerCase(), Double.valueOf(configuration.getList("Limits.default", false).stream().filter(s -> p.hasPermission(s.split(":")[0])).findFirst().orElse("0:0").split(":")[1]), Double.valueOf(configuration.getList("Boosters.default", false).stream().filter(s -> p.hasPermission(s.split(":")[0])).findFirst().orElse("0:1.0").split(":")[1]), new ArrayList<>(), drops, new ArrayList<>(), BonusC.get(p));
                put(armazem);
            }
        }
    }

    public static void remove(String owner) {
        final Armazem armazem = get(owner);
        new ManagerDAO().save(armazem);
        remove(armazem);
    }

    public static boolean exists(String owner) {
        return armazens.stream().anyMatch(armazem -> armazem.getOwner().equalsIgnoreCase(owner.toLowerCase()));
    }

    public static Armazem get(String owner) {
        return armazens.stream().filter(armazem -> armazem.getOwner().equalsIgnoreCase(owner.toLowerCase())).findFirst().get();
    }

}
