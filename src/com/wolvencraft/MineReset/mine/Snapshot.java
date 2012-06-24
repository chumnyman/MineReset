package com.wolvencraft.MineReset.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Snapshot")
public class Snapshot implements ConfigurationSerializable  {
	private String parent;
	private List<DataBlock> blocks;
	
	public Snapshot(String parent) {
		this.parent = parent;
		blocks = new ArrayList<DataBlock>();
	}
	
	@SuppressWarnings("unchecked")
	public Snapshot(Map<String, Object> me) {
		parent = (String) me.get("parent");
		blocks = (List<DataBlock>) me.get("blocks");
	}
	
	public Map<String, Object> serialize() {
		Map<String, Object> me = new HashMap<String, Object>();
		me.put("parent", parent);
		me.put("blocks", blocks);
		return me;
	}
	
	public void setBlocks(World world, Location one, Location two) {
		blocks = new ArrayList<DataBlock>();
		for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
                    blocks.add(new DataBlock(world.getBlockAt(x, y, z).getState()));
                }
            }
        }
	}
	
	public String getParent() {
		return parent;
	}
	
	public List<DataBlock> getBlocks() {
		return blocks;
	}
}
