package com.wolvencraft.MineReset.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.event.Listener;
import org.bukkit.material.MaterialData;

@SerializableAs("Blacklist")
public class Blacklist implements ConfigurationSerializable, Listener {

	private List<MaterialData> blocks;
	private boolean whitelist;
	private boolean enabled;
	
	public Blacklist() {
		blocks = new ArrayList<MaterialData>();
		whitelist = false;
		enabled = false;
	}
	
	public Blacklist(Map<String, Object> me) {
		whitelist = (Boolean) me.get("whitelist");
		enabled = (Boolean) me.get("enabled");
        blocks = (List<MaterialData>) me.get("blocks");
	}
	
	public Map<String, Object> serialize() {
		Map<String, Object> me = new HashMap<String, Object>();
		me.put("whitelist", whitelist);
		me.put("whitelist", whitelist);
		me.put("blocks", blocks);
		return me;
	}
	
	public List<MaterialData> getBlocks() {
		return blocks;
	}
	
	public boolean getWhitelist() {
		return whitelist;
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public void setBlocks(List<MaterialData> blocks) {
		this.blocks = blocks;
	}
	
	public void setWhitelist(boolean whitelist) {
		this.whitelist = whitelist;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
