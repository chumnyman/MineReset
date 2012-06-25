package com.wolvencraft.MineReset.mine;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.event.Listener;

@SerializableAs("SimpleLoc")
public class SimpleLoc implements ConfigurationSerializable, Listener {
	
	private String world;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	
	public SimpleLoc(Location loc) {
		x = loc.getX();
		y = loc.getY();
		z = loc.getZ();
		yaw = loc.getYaw();
		pitch = loc.getPitch();
		world = loc.getWorld().getName();
	}
	
	public SimpleLoc(Map<String, Object> me) {
		world = (String) me.get("world");
		x = ((Double) me.get("x")).doubleValue();
		y = ((Double) me.get("y")).doubleValue();
		z = ((Double) me.get("z")).doubleValue();
		yaw = ((Double) me.get("yaw")).floatValue();
		pitch = ((Double) me.get("pitch")).floatValue();
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> me = new HashMap<String, Object>();
		me.put("world", world);
		me.put("x", x);
		me.put("y", y);
		me.put("z", z);
		me.put("yaw", yaw);
		me.put("pitch", pitch);
		return me;
	}
	
	public Location toLocation() {
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
	}
}
