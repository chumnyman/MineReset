package com.wolvencraft.MineReset.mine;

import org.bukkit.material.MaterialData;

public class MineBlock {

    private MaterialData block;
    private double chance;

    public MineBlock(MaterialData block, double chance) {
        this.block = block;
        this.chance = chance;
    }

    public MaterialData getBlock() {
        return block;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }
}