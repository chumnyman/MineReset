package com.wolvencraft.MineReset.mine;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

public class Snapshot {
	private List<MaterialData> blocks;
	public Snapshot() {
		blocks = new ArrayList<MaterialData>();
	}
	
	public void save(World world, Location one, Location two) {
		for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
                    Block thisBlock = world.getBlockAt(x, y, z);
                    blocks.add(new MaterialData(thisBlock.getType(), thisBlock.getData()));
                }
            }
        }
	}
}
