/*
 * DataBlock.java
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
