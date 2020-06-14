package me.nullpointer.armazemplus.utils;

import me.nullpointer.armazemplus.enums.StackMob;

public class Settings {

    private final StackMob stackMob;
    private final boolean breakBlock;
    private final boolean killMob;
    private final boolean plotDrop;
    private final boolean friends;
    private final int friendsMax;
    private final Double limitMax;
    private final String worldPlot;

    public Settings(Configuration configuration) {
        stackMob = StackMob.valueOf(configuration.get("Settings.stack-mobs", false));
        breakBlock = configuration.is("Settings.break");
        killMob = configuration.is("Settings.kill");
        plotDrop = configuration.is("Settings.plot-drop");
        friends = configuration.is("Settings.friends.toggle");
        friendsMax = configuration.getInt("Settings.friends.max");
        limitMax = configuration.getDouble("Limits.max");
        worldPlot = configuration.get("Settings.world-plot", true);
    }

    public String getWorldPlot() {
        return worldPlot;
    }

    public StackMob getStackMob() {
        return stackMob;
    }

    public boolean isBreakBlock() {
        return breakBlock;
    }

    public boolean isKillMob() {
        return killMob;
    }

    public boolean isPlotDrop() {
        return plotDrop;
    }

    public boolean isFriends() {
        return friends;
    }

    public int getFriendsMax() {
        return friendsMax;
    }

    public Double getLimitMax() {
        return limitMax;
    }
}
