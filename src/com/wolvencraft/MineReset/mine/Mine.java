package com.wolvencraft.MineReset.mine;

import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.generation.EmptyGenerator;
import com.wolvencraft.MineReset.generation.RandomGenerator;
import com.wolvencraft.MineReset.generation.SnapshotGenerator;
import com.wolvencraft.MineReset.generation.SurfaceGenerator;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private String parent;
    private List<MineBlock> blocks;
    private Generator generator;
    private Snapshot snapshot;
    private Blacklist blacklist;
    private boolean silent;
    private boolean automatic;
    private int automaticSeconds;
    private long nextAutomaticResetTick;
    private boolean cooldownEnabled;
    private int cooldownSeconds;
    private long nextCooldownTicks;
    private boolean warned;
    private List<Integer> warningTimes;
    private List<Protection> enabledProtection;
    private Location protOne;
    private Location protTwo;
    private Blacklist breakBlacklist;
    private Blacklist placeBlacklist;

    /**
     * Constuctor for new mines.
     * @param one First mine point, with the *smaller* X, Y, and Z values.
     * @param two Second mine point, with the larger coordinate values.
     * @param tpPoint Teleport point to TP players out of mine to.
     * @param world World mine resides in.
     * @param name Name for the mine.
     * @param automaticSeconds Number of seconds between automatic resets.
     */
    public Mine(Location one, Location two, Location tpPoint, World world, String name, int automaticSeconds) {
    	this.one = one;
    	this.two = two;
    	this.tpPoint = tpPoint;
    	this.world = world;
    	displayName = "";
    	parent = null;
    	this.name = name;
    	blocks = new ArrayList<MineBlock>();
    	blocks.add(new MineBlock(new MaterialData(Material.AIR), 1.0));
    	generator = Generator.RANDOM;
    	snapshot = null;
    	blacklist = new Blacklist();
    	silent = false;
    	automatic = true;
    	this.automaticSeconds = automaticSeconds;
    	nextAutomaticResetTick = automaticSeconds * 20;
    	cooldownEnabled = false;
    	cooldownSeconds = 0;
    	nextCooldownTicks = 0;
    	warned = true;
    	warningTimes = new ArrayList<Integer>();
    	enabledProtection = new ArrayList<Protection>();
    	protOne = one;
    	protTwo = two;
    	breakBlacklist = new Blacklist();
    	placeBlacklist = new Blacklist();
    }

    /**
     * Full constructor.
     * @param one First mine point, with the *smaller* X, Y, and Z values.
     * @param two Second mine point, with the larger coordinate values.
     * @param world World mine resides in.
     * @param tpPoint Teleport point to TP players out of mine to.
     * @param displayName Display name for the mine.
     * @param parent Mines's parent, if any.
     * @param name Internal name for the mine.
     * @param blocks List of blocks and their weights.
     * @param isSilent Will the mine reset silently?
     * @param isAutomatic Will the mine automatically reset?
     * @param automaticSeconds Number of seconds between automatic resets.
     * @param warningTimes List of seconds before reset to warn over chat.
     * @param enabledProtection List of protection types enabled for the mine.
     * @param protOne First protection region point
     * @param protTwo Second protection region point
     */
    public Mine(Location one, Location two, World world, Location tpPoint, String displayName, String parent, String name, List<MineBlock> blocks, Generator generator, boolean isSilent, boolean isAutomatic, int automaticSeconds,  boolean cooldownEnabled, int cooldownSeconds, List<Integer> warningTimes, List<Protection> enabledProtection, Location protOne, Location protTwo) {
        this.one = one;
        this.two = two;
        this.world = world;
        this.tpPoint = tpPoint;
        this.displayName = displayName;
        this.parent = parent;
        this.name = name;
        this.blocks = blocks;
        this.generator = generator;
        snapshot = null;
    	blacklist = new Blacklist();
        silent = isSilent;
        automatic = isAutomatic;
        this.automaticSeconds = automaticSeconds;
    	nextAutomaticResetTick = automaticSeconds * 20;
    	this.cooldownEnabled = cooldownEnabled;
    	this.cooldownSeconds = cooldownSeconds;
    	nextCooldownTicks = cooldownSeconds;
        warned = warningTimes != null && warningTimes.size() > 0;
        this.warningTimes = warningTimes;
        this.enabledProtection = enabledProtection;
        this.protOne = protOne;
        this.protTwo = protTwo;
    	breakBlacklist = new Blacklist();
    	placeBlacklist = new Blacklist();
    }

    /**
     * Deserialize a mine from its YML form
     * @param me Bukkitian map of strings to objects. <b>Incorrect object types for values are not tolerated by the code!</b>
     */
    @SuppressWarnings("unchecked")
	public Mine(Map<String, Object> me) {
        world = Bukkit.getWorld((String) me.get("world"));
        one = ((Vector) me.get("one")).toLocation(world);
        two = ((Vector) me.get("two")).toLocation(world);
        tpPoint = ((Vector) me.get("tpPoint")).toLocation(world);
        displayName = (String) me.get("displayName");
        name = (String) me.get("name");
        parent = (String) me.get("parent");
        blacklist = (Blacklist) me.get("blacklist");
        snapshot = (Snapshot) me.get("snapshot");
        String generatorString = (String) me.get("generator");
        generator = Generator.valueOf(generatorString);
        silent = (Boolean) me.get("silent");
        automatic = (Boolean) me.get("automatic");
        automaticSeconds = (Integer) me.get("automaticResetTime");
        cooldownEnabled = (Boolean) me.get("cooldownEnabled");
        cooldownSeconds = (Integer) me.get("cooldownSeconds");
        nextCooldownTicks = (Integer) me.get("nextCooldownTicks");
        warned = (Boolean) me.get("isWarned");
        warningTimes = (List<Integer>) me.get("warningTimes");
        blocks = (List<MineBlock>) me.get("blocks");
        enabledProtection = (List<Protection>) me.get("protectionTypes");
        protOne = ((Vector) me.get("protOne")).toLocation(world);
        protTwo = ((Vector) me.get("protTwo")).toLocation(world);
        breakBlacklist = (Blacklist) me.get("breakBlacklist");
        placeBlacklist = (Blacklist) me.get("placeBlacklist");
    }

    public void reset(Generator generator) {
        removePlayers();
        if(generator.equals(Generator.EMPTY))
        	EmptyGenerator.reset(this);
        else if(generator.equals(Generator.SURFACE))
        	SurfaceGenerator.reset(this);
        else if(generator.equals(Generator.SNAPSHOT))
        	SnapshotGenerator.reset(this);
        else
	        RandomGenerator.reset(this);
    }

    private void removePlayers() {
        for (Player p : world.getPlayers()) {
            if (isLocationInMine(p.getLocation())) {
                p.teleport(tpPoint, PlayerTeleportEvent.TeleportCause.PLUGIN);
                Message.sendPlayer(p, Util.parseVars(Language.getString("misc.mine-teleport"), this));
            }
        }
    }

    public boolean isLocationInMine(Location l) {
        return (l.getX() >= one.getX() && l.getX() <= two.getX())     //if x matches
                && (l.getY() >= one.getY() && l.getY() <= two.getY()) //and y
                && (l.getZ() >= one.getZ() && l.getZ() <= two.getZ()) //and z
                && l.getWorld().equals(world);//and world
    }
    
    public boolean isLocationInProtection(Location l) {
        return (l.getX() >= protOne.getX() && l.getX() <= protTwo.getX())     //if x matches
                && (l.getY() >= protOne.getY() && l.getY() <= protTwo.getY()) //and y
                && (l.getZ() >= protOne.getZ() && l.getZ() <= protTwo.getZ()) //and z
                && l.getWorld().equals(world);//and world
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
        me.put("blacklist", blacklist);
        me.put("snapshot", snapshot);
        me.put("generator", generator.toString());
        me.put("silent", silent);
        me.put("automatic", automatic);
        me.put("automaticResetTime", automaticSeconds);
        me.put("cooldownEnabled", cooldownEnabled);
        me.put("cooldownSeconds", cooldownSeconds);
        me.put("nextCooldownTicks", nextCooldownTicks);
        me.put("isWarned", warned);
        me.put("warningTimes", warningTimes);
        me.put("protectionTypes", enabledProtection);
        me.put("protOne", protOne.toVector());
        me.put("protTwo", protTwo.toVector());
        me.put("blocks", blocks);
        me.put("breakBlacklist", breakBlacklist);
        me.put("placeBlacklist", placeBlacklist);
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
    
    public Snapshot getSnapshot() {
    	return snapshot;
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
    public String getParent() {
    	return parent;
    }
    
    public boolean hasParent() {
    	return (parent != null);
    }
    
    public Blacklist getBlacklist() {
    	return blacklist;
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
    
    public Generator getGenerator() {
    	return generator;
    }
    
    public boolean getCooldown() {
    	return cooldownEnabled;
    }
    
    public int getCooldownTime() {
    	return cooldownSeconds;
    }
    
    public int getNextCooldown() {
    	return (int)(nextCooldownTicks * 20);
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
    
    public Location getFirstProtPoint() {
    	return protOne;
    }
    
    public Location getSecondProtPoint() {
    	return protTwo;
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
    
    public void setParent(String parent) {
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
    
    public void setGenerator(Generator generator) {
    	this.generator = generator;
    }
    
    public void setCooldown(boolean cooldownEnabled) {
    	this.cooldownEnabled = cooldownEnabled;
    }
    
    public void setCooldownTime(int cooldownSeconds) {
    	this.cooldownSeconds = cooldownSeconds;
    }
    
    public void updateCooldown(long ticks) {
    	this.nextCooldownTicks -= ticks;
    }
    
    public void resetCooldown() {
    	this.nextCooldownTicks = cooldownSeconds * 20;
    }
    
    public void setResetPeriod(int automaticSeconds) {
    	this.automaticSeconds = automaticSeconds;
    }
    
    public void updateTimer(long ticks) {
    	this.nextAutomaticResetTick -= ticks;
    }
    
    public void resetTimer() {
    	nextAutomaticResetTick = automaticSeconds * 20;
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

    public void setProtectionRegion(Location[] coords) {
    	protOne = coords[0];
    	protTwo = coords[1];
    }

    public long getNextAutomaticResetTick() {
        return nextAutomaticResetTick;
    }
    
    public Blacklist getBreakBlacklist() {
    	return breakBlacklist;
    }
    
    public Blacklist getPlaceBlacklist() {
    	return placeBlacklist;
    }
}
