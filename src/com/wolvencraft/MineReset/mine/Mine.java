package com.wolvencraft.MineReset.mine;

import org.bukkit.Location;

import java.util.List;

/**
 * @author jjkoletar
 */
public class Mine {
    private Location one;
    private Location two;
    private Location tpPoint;
    private String name;
    private List<MineBlock> blocks;
    private boolean silent;
    private boolean automatic;
    private int automaticSeconds;
    private boolean warned;
    private List<Integer> warningTimes;
    private List<Protection> enabledProtection;

    public Mine(Location one, Location two, Location tpPoint, String name, List<MineBlock> blocks, boolean isSilent, boolean isAutomatic, int automaticSeconds, boolean isWarned, List<Integer> warningTimes, List<Protection> enabledProtection) {
        this.one = one;
        this.two = two;
        this.tpPoint = tpPoint;
        this.name = name;
        this.blocks = blocks;
        silent = isSilent;
        automatic = isAutomatic;
        this.automaticSeconds = automaticSeconds;
        warned = isWarned;
        this.warningTimes = warningTimes;
        this.enabledProtection = enabledProtection;
    }
}
