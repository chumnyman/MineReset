package com.wolvencraft.MineReset.mine;

import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.RandomBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jjkoletar
 */
@SerializableAs("Mine")
public class Mine implements ConfigurationSerializable, Listener {
    private Location one;
    private Location two;
    private World world;
    private Location tpPoint;
    private String name;
    private String displayName;
    private Mine parent;
    private List<MineBlock> blocks;
    private boolean silent;
    private boolean automatic;
    private int automaticSeconds;			// Change to ticks for uniformity maybe?
    private long nextAutomaticResetTick;
    private boolean warned;
    private List<Integer> warningTimes;
    private List<Protection> enabledProtection;

    public Mine(Location one, Location two, Location tpPoint, World world, String name)
    {
    	this.one = one;
    	this.two = two;
    	this.tpPoint = tpPoint;
    	this.world = world;
    	displayName = "";
    	parent = null;
    	this.name = name;
    	blocks = null;
    	silent = false;
    	automatic = true;
    	automaticSeconds = 900;	//TODO Do something about the magic numbers
    	warned = true;
    	warningTimes = new ArrayList<Integer>();
    	warningTimes.add(300); //TODO Same here
    	enabledProtection = new ArrayList<Protection>();
    }
    
    public Mine(Location one, Location two, World world, Location tpPoint, String displayName, Mine parent, String name, List<MineBlock> blocks, boolean isSilent, boolean isAutomatic, int automaticSeconds, boolean isWarned, List<Integer> warningTimes, List<Protection> enabledProtection) {
        this.one = one;
        this.two = two;
        this.world = world;
        this.tpPoint = tpPoint;
        this.displayName = displayName;
        this.parent = parent;
        this.name = name;
        this.blocks = blocks;
        silent = isSilent;
        automatic = isAutomatic;
        this.automaticSeconds = automaticSeconds;
        warned = isWarned;
        this.warningTimes = warningTimes;
        this.enabledProtection = enabledProtection;
    }

    /**
     * Deserialize a mine from its YML form
     * @param me Bukkitian map of strings to objects. <b>Incorrect object types for values are not tolerated by the code!</b>
     */
    public Mine(Map<String, Object> me) {
        world = Bukkit.getWorld((String) me.get("world"));
        one = ((Vector) me.get("one")).toLocation(world);
        two = ((Vector) me.get("two")).toLocation(world);
        tpPoint = ((Vector) me.get("tpPoint")).toLocation(world);
        displayName = (String) me.get("displayName");
        name = (String) me.get("name");
        parent = (Mine) me.get("parent");
        silent = (Boolean) me.get("silent");
        automatic = (Boolean) me.get("automatic");
        automaticSeconds = (Integer) me.get("automaticResetTime");
        warned = (Boolean) me.get("isWarned");
        warningTimes = (List<Integer>) me.get("warningTimes");
        blocks = (List<MineBlock>) me.get("blocks");
        enabledProtection = (List<Protection>) me.get("protectionTypes");
    }

    public void reset() {
        removePlayers();
        //TODO: Implement whitelist/blacklist
        RandomBlock pattern = new RandomBlock(blocks);
        for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
                    MaterialData block = pattern.next();
                    world.getBlockAt(x, y, z).setType(block.getItemType());
                    world.getBlockAt(x, y, z).setData(block.getData());
                }
            }
        }
    }

    private void removePlayers() {
        for (Player p : world.getPlayers()) {
            if (isLocationInMine(p.getLocation())) {
                p.teleport(tpPoint, PlayerTeleportEvent.TeleportCause.PLUGIN);
                Message.sendPlayer(p, Language.getMessage("teleportation.mine-teleport", name));
            }
        }
    }

    public boolean isLocationInMine(Location l) {
        return (l.getX() >= one.getX() && l.getX() <= two.getX())     //if x matches
                && (l.getY() >= one.getY() && l.getY() <= two.getY()) //and y
                && (l.getZ() >= one.getZ() && l.getZ() <= two.getZ());//and z
    }

    public Map<String, Object> serialize() {
        Map<String, Object> me = new HashMap<String, Object>();
        me.put("one", one.toVector());
        me.put("two", two.toVector());
        me.put("world", world.getName());
        me.put("tpPoint", tpPoint.toVector());
        me.put("displayName", displayName);
        me.put("name", name);
        me.put("parent", parent);
        me.put("silent", silent);
        me.put("automatic", automatic);
        me.put("automaticResetTime", automaticSeconds);
        me.put("isWarned", warned);
        me.put("warningTimes", warningTimes);
        me.put("protectionTypes", enabledProtection);
        me.put("blocks", blocks);
        return me;
    }
    
    public Location getFirstPoint() {
    	return one;
    }
    
    public Location getSecondPoint() {
    	return two;
    }
    
    public World getWorld() {
    	return world;
    }
    
    public Location getWarp() {
    	return tpPoint;
    }
    
    public String getName() {
    	return name;
    }
    
    public String getDisplayName() {
    	return displayName;
    }
    
    /**
     * If a mine has a parent, returns its object.<br />
     * Otherwise, returns the mine object itself
     * @return Mine parent
     */
    public Mine getParent() {
    	if(parent == null)
    		return this;
    	else return parent;
    }
    
    public boolean getSilent() {
    	return silent;
    }
    
    public List<MineBlock> getBlocks() {
    	return blocks;
    }
    
    public boolean getAutomatic() {
    	return automatic;
    }
    
    public int getResetPeriod() {
    	return automaticSeconds;
    }
    
    public long getNextReset() {
    	return nextAutomaticResetTick;
    }
    
    public boolean getWarned() {
    	return warned;
    }
    
    public List<Integer> getWarningTimes() {
    	return warningTimes;
    }
    
    public List<Protection> getProtection() {
    	return enabledProtection;
    }
    
    public void setFirstPoint(Location position) {
    	one = position;
    }
    
    public void setSecondPoint(Location position) {
    	two = position;
    }
    
    public void setWarp(Location tpPoint) {
    	this.tpPoint = tpPoint;
    }
    
    public void setWorld(World world) {
    	this.world = world;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public void setDisplayName(String displayName) {
    	this.displayName = displayName;
    }
    
    public void setParent(Mine parent) {
    	this.parent = parent;
    }
    
    public void setSilent(boolean silent) {
    	this.silent = silent;
    }
    
    public void setBlocks(List<MineBlock> blocks) {
    	this.blocks = blocks;
    }
    
    public void setAutomatic(boolean automatic) {
    	this.automatic = automatic;
    }
    
    public void setResetPeriod(int automaticSeconds) {
    	this.automaticSeconds = automaticSeconds;
    }
    
    public void updateTimer(long ticks) {
    	this.nextAutomaticResetTick -= ticks;
    }
    
    public void setWarned(boolean warned) {
    	this.warned = warned;
    }
    
    public void setWarningTimes(List<Integer> warningTimes) {
    	this.warningTimes = warningTimes;
    }
    
    public void setProtection(List<Protection> enabledProtection) {
    	this.enabledProtection = enabledProtection;
    }

    public long getNextAutomaticResetTick() {
        return nextAutomaticResetTick;
    }
}
