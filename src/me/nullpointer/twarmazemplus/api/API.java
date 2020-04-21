package me.nullpointer.twarmazemplus.api;

import me.nullpointer.twarmazemplus.utils.Configuration;
import me.nullpointer.twarmazemplus.utils.Settings;
import me.nullpointer.twarmazemplus.utils.Utils;

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
}
