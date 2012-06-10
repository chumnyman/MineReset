package com.wolvencraft.MineReset.mine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

@SerializableAs("SignClass")
public class SignClass implements ConfigurationSerializable, Listener  {
	private World world;
	private Location loc;
	private Mine parent;
	private boolean reset;
	private List<String> lines;
	
	public SignClass(Mine parent, Location loc, Sign sign) {
		this.parent = parent;
		world = loc.getWorld();
		this.loc = loc;
		for(int i = 0; i < sign.getLines().length; i++) {
			lines.add(sign.getLine(i));
		}
	}
	
	@SuppressWarnings("unchecked")
	public SignClass(Map<String, Object> me) {
        world = Bukkit.getWorld((String) me.get("world"));
        loc = ((Vector) me.get("loc")).toLocation(world);
        parent = (Mine) me.get("parent");
        reset = (Boolean) me.get("reset");
        lines = (List<String>) me.get("lines");
	}
	
    public Map<String, Object> serialize() {
        Map<String, Object> me = new HashMap<String, Object>();
        me.put("loc", loc.toVector());
        me.put("world", world.getName());
        me.put("parent", parent);
        me.put("reset", reset);
        me.put("lines", lines);
        return me;
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
    public Mine getParent() {
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
     * Changes the parent mine of the sign
     * @param parent New parent
     */
    public void setParent(Mine parent) {
    	this.parent = parent;
    }
    
    /**
     * Toggles whether a sign should reset a mine or not
     * @param reset New value for the reset
     */
    public void setReset(boolean reset) {
    	this.reset = reset;
    }
}
