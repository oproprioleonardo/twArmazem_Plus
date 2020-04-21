package me.nullpointer.twarmazemplus.utils;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.SplittableRandom;

public class Utils {

    public static HashMap<String, Double> format = Maps.newHashMap();
    public static String[] suffix;
    public static SplittableRandom rand = new SplittableRandom();

    public static double getMultiplier(String txt) {
        txt = txt.replaceAll("\\d", "").replace(" ", "");
        for (String key : format.keySet()) {
            if (key.equalsIgnoreCase(txt)) {
                return format.get(key);
            }
        }
        return 1.0;
    }

    public static Double getValue(String txt) {
        txt = txt.replaceAll("\\D", "").replace(" ", "");
        return Double.valueOf(txt);
    }

    private static double getMultipliedValue(String txt) {
        return getValue(txt) * getMultiplier(txt);
    }

    public static Double get(String[] s) {
        double value = 0.0;
        for (String vls : s) {
            value += getMultipliedValue(vls);
        }
        return value;
    }

    public static String format(Double value) {
        int index;
        for (index = 0; value / 1000.0 >= 1.0; value /= 1000.0, ++index) ;
        final DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("pt", "BR")));
        try {
            decimalFormat.setRoundingMode(RoundingMode.UNNECESSARY);
            return decimalFormat.format(value) + suffix[index];
        } catch (Exception e) {
            decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);
            return decimalFormat.format(value) + suffix[index];
        }
    }

    public static Double multiplyDrops(final Player killer, final Double mobAmount) {
        return getCorrectDropAmount(generateRandom(mobAmount), killer);
    }

    public static Double generateRandom(final Double mobAmount) {
        return Math.ceil(rand.nextDouble(1.0, 5.0) * mobAmount);
    }

    public static Double getCorrectDropAmount(final Double randomAmount, final Player killer) {
        return 0.0 + Math.round(randomAmount * ((rand.nextInt(killer.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) + 1) + 2) / 1.75));
    }

    public static void sendActionBar(final Player p, final String text) {
        final PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

}
