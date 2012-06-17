package com.wolvencraft.MineReset.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Snapshot")
public class Snapshot {
	private List<BlockState> blocks;
	
	public Snapshot() {
		blocks = new ArrayList<BlockState>();
	}
	
	@SuppressWarnings("unchecked")
	public Snapshot(Map<String, Object> me) {
		blocks = (List<BlockState>) me.get("blocks");
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
                    blocks.add(thisBlock.getState());
                }
            }
        }
	}
	
	public List<BlockState> getBlocks() {
		return blocks;
	}
}
