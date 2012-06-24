package com.wolvencraft.MineReset.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
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
	
	@SuppressWarnings("unchecked")
	public Blacklist(Map<String, Object> me) {
		whitelist = (Boolean) me.get("whitelist");
		enabled = (Boolean) me.get("enabled");
        Map<Integer, Byte> materials = (Map<Integer, Byte>) me.get("blocks");
        blocks = new ArrayList<MaterialData>();
        Iterator<Entry<Integer, Byte>> it = materials.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Byte> pairs = it.next();
            blocks.add(new MaterialData(Material.getMaterial(pairs.getKey()), pairs.getValue()));
            it.remove();
        }
	}
	
	public Map<String, Object> serialize() {
		Map<String, Object> me = new HashMap<String, Object>();
		me.put("whitelist", whitelist);
		me.put("enabled", enabled);
        Map<Integer, Byte> materials = new HashMap<Integer, Byte>();
        for(MaterialData block : blocks) {
        	materials.put(block.getItemTypeId(), block.getData());
        }
        me.put("blocks", materials);
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
