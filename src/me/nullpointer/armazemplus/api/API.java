package me.nullpointer.armazemplus.api;

import me.nullpointer.armazemplus.utils.Configuration;
import me.nullpointer.armazemplus.utils.Settings;
import me.nullpointer.armazemplus.utils.Utils;

public class API {

    public static Configuration configuration;
    public static Settings settings;

    public API() {
        configuration = new Configuration("config");
        Utils.suffix = configuration.getList("Settings.format", true).stream().map(s -> s.replace(s.split(",")[1], "").replace(",", "")).toArray(String[]::new);
        configuration.getList("Settings.format", true).forEach(s -> Utils.format.put(s.split(",")[0], Double.valueOf(s.split(",")[1])));
        settings = new Settings(configuration);
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static Settings getSettings() {
        return settings;
    }

    //todo fazer api
    //todo fazer eventos para desenvolvedores
    //todo adicionar preço de venda por rank
    //todo hook scoreboard
}
