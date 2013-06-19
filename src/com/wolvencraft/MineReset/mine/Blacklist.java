/*
 * Blacklist.java
 * 
 * MineReset
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.MineReset.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        for(Map.Entry<Integer, Byte> entry : materials.entrySet()) {
            try {
                blocks.add(new MaterialData(Material.getMaterial(entry.getKey().intValue()), entry.getValue().byteValue()));
            } catch (ClassCastException cce) {
                blocks.add(new MaterialData(Material.getMaterial(entry.getKey().intValue())));
            }
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
