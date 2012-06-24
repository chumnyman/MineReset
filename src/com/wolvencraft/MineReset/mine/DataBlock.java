package com.wolvencraft.MineReset.mine;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.event.Listener;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

@SerializableAs("DataBlock")
public class DataBlock implements ConfigurationSerializable, Listener  {
	
	public static MaterialData data;
	public static Location loc;
	
	public DataBlock(BlockState block) {
		data = block.getData();
		loc = block.getLocation();
	}
	
	public DataBlock(Map<String, Object> me) {
        data = new MaterialData((Integer) me.get("blockId"), ((Integer) me.get("blockData")).byteValue());
		World world = Bukkit.getServer().getWorld((String) me.get("world"));
		loc = ((Vector) me.get("loc")).toLocation(world);
		
	}
	
	public Map<String, Object> serialize() {
        Map<String, Object> me = new HashMap<String, Object>();
        me.put("blockId", data.getItemTypeId());
        me.put("blockData", data.getData());
        me.put("world", loc.getWorld().getName());
        me.put("loc", loc.toVector());
		return me;
	}
	
	public MaterialData getData() {
		return data;
	}
	
	public Location getLocation() {
		return loc;
	}
}
