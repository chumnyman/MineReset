/*
 * SignClass.java
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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.SignUtil;
import com.wolvencraft.MineReset.util.Util;

@SerializableAs("SignClass")
public class SignClass implements ConfigurationSerializable, Listener  {
    private String id;
    private World world;
    private Location loc;
    private String parent;
    private boolean reset;
    private List<String> lines;
    
    public SignClass(String parent, Location loc, Sign sign) {
        id = SignUtil.generateId();
        this.parent = parent;
        world = loc.getWorld();
        this.loc = loc;
        lines = new ArrayList<String>();
        Mine parentMine = MineUtil.getMine(parent);
        for(int i = 0; i < sign.getLines().length; i++) {
            String line = sign.getLine(i);
            lines.add(line);
            sign.setLine(i, Util.parseVars(line, parentMine));
        }
        sign.update(true);
    }
    
    public SignClass(String id, World world, Location loc, String parent, boolean reset, List<String> lines) {
        this.id = id;
        this.world = world;
        this.loc = loc;
        this.parent = parent;
        this.reset = reset;
        this.lines = lines;
        Mine parentMine = MineUtil.getMine(parent);
        
        BlockState block = world.getBlockAt(loc).getState();
        if(!(block instanceof Sign)) return; 
        Sign sign = (Sign) block;
        for(int i = 0; i < lines.size(); i++) {
            sign.setLine(i, Util.parseVars(lines.get(i), parentMine));
        }
        sign.update(true);
    }
    
    @SuppressWarnings("unchecked")
    public SignClass(Map<String, Object> me) {
        id = (String) me.get("id");
        world = Bukkit.getWorld((String) me.get("world"));
        loc = ((Vector) me.get("loc")).toLocation(world);
        parent = (String) me.get("parent");
        reset = (Boolean) me.get("reset");
        lines = (List<String>) me.get("lines");
    }
    
    public Map<String, Object> serialize() {
        Map<String, Object> me = new HashMap<String, Object>();
        me.put("id", id);
        me.put("loc", loc.toVector());
        me.put("world", world.getName());
        me.put("parent", parent);
        me.put("reset", reset);
        me.put("lines", lines);
        return me;
    }
    
    /**
     * Returns the unique ID of the sign
     * @return Sign id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns the location of the sign
     * @return The sign location
     */
    public Location getLocation() {
        return loc;
    }
    
    /**
     * Returns the parent mine of the specific sign
     * @return The parent mine
     */
    public String getParent() {
        return parent;
    }
    
    /**
     * Checks if a sign should reset its parent mine on right-click
     * @return true if reset is enabled, false otherwise
     */
    public boolean getReset() {
        return reset;
    }
    
    /**
     * Gets the unmodified lines for the signs
     * @return the original text on the sign
     */
    public List<String> getLines() {
        return lines;
    }
    
    /**
     * Toggles whether a sign should reset a mine or not
     * @param reset New value for the reset
     */
    public void setReset(boolean reset) {
        this.reset = reset;
    }
}
