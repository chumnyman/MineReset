/*
 * SimpleLoc.java
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
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.event.Listener;

@SerializableAs("SimpleLoc")
public class SimpleLoc implements ConfigurationSerializable, Listener {
    
    private World world;
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
        world = loc.getWorld();
    }
    
    public SimpleLoc(Map<String, Object> me) {
        String worldString = (String) me.get("world");
        try {
            world = Bukkit.getWorld(UUID.fromString(worldString));
        }
        catch(IllegalArgumentException eie) {
            world = Bukkit.getWorld(worldString);
            if(world == null) throw new IllegalArgumentException("Mine file contains an invalid world for the warp location!");
        }
        x = ((Double) me.get("x")).doubleValue();
        y = ((Double) me.get("y")).doubleValue();
        z = ((Double) me.get("z")).doubleValue();
        yaw = ((Double) me.get("yaw")).floatValue();
        pitch = ((Double) me.get("pitch")).floatValue();
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> me = new HashMap<String, Object>();
        me.put("world", world.getUID().toString());
        me.put("x", x);
        me.put("y", y);
        me.put("z", z);
        me.put("yaw", yaw);
        me.put("pitch", pitch);
        return me;
    }
    
    public Location toLocation() {
        return new Location(world, x, y, z, yaw, pitch);
    }
}
