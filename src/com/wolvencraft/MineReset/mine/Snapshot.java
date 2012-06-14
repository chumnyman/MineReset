package com.wolvencraft.MineReset.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.MaterialData;

@SerializableAs("Snapshot")
public class Snapshot {
	private List<MaterialData> blocks;
	
	public Snapshot() {
		blocks = new ArrayList<MaterialData>();
	}
	
	@SuppressWarnings("unchecked")
	public Snapshot(Map<String, Object> me) {
		blocks = (List<MaterialData>) me.get("blocks");
	}
	
	public Map<String, Object> serialize() {
		Map<String, Object> me = new HashMap<String, Object>();
		me.put("blocks", blocks);
		return me;
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
	
	public List<MaterialData> getBlocks() {
		return blocks;
	}
}
