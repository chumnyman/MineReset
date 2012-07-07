package com.wolvencraft.MineReset.mine;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.GeneratorUtil;
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
import java.util.UUID;

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
    private String generator;
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
    	generator = "RANDOM";
    	blacklist = new Blacklist();
    	silent = false;
    	automatic = false;
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
    public Mine(Location one, Location two, World world, Location tpPoint, String displayName, String parent, String name, List<MineBlock> blocks, String generator, boolean isSilent, boolean isAutomatic, int automaticSeconds,  boolean cooldownEnabled, int cooldownSeconds, List<Integer> warningTimes, List<Protection> enabledProtection, Location protOne, Location protTwo) {
        this.one = one;
        this.two = two;
        this.world = world;
        this.tpPoint = tpPoint;
        this.displayName = displayName;
        this.parent = parent;
        this.name = name;
        this.blocks = blocks;
        this.generator = generator;
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
		String worldString = (String) me.get("world");
		try {
        world = Bukkit.getWorld(UUID.fromString(worldString));
		}
		catch(IllegalArgumentException eie) {
			world = Bukkit.getWorld(worldString);
        	if(world == null) {
        		world = Bukkit.getServer().getWorlds().get(0);
        		ChatUtil.sendError("Mine file contains an invalid world, substituting the default world. Contact the administrator immediately");
        	}
		}
        one = ((Vector) me.get("one")).toLocation(world);
        two = ((Vector) me.get("two")).toLocation(world);
        tpPoint = ((SimpleLoc) me.get("tpPoint")).toLocation();
        displayName = (String) me.get("displayName");
        name = (String) me.get("name");
        parent = (String) me.get("parent");
        blacklist = (Blacklist) me.get("blacklist");
        generator = (String) me.get("generator");
        silent = (Boolean) me.get("silent");
        automatic = (Boolean) me.get("automatic");
        automaticSeconds = (Integer) me.get("automaticResetTime");
    	nextAutomaticResetTick = automaticSeconds * 20;
        cooldownEnabled = (Boolean) me.get("cooldownEnabled");
        cooldownSeconds = (Integer) me.get("cooldownSeconds");
        nextCooldownTicks = cooldownSeconds * 20;
        warned = (Boolean) me.get("isWarned");
        warningTimes = (List<Integer>) me.get("warningTimes");
        blocks = (List<MineBlock>) me.get("blocks");
        List<String> names = (List<String>) me.get("protectionTypes");
        enabledProtection = new ArrayList<Protection>();
        for(String name : names)
        	enabledProtection.add(Protection.valueOf(name));
        protOne = ((Vector) me.get("protOne")).toLocation(world);
        protTwo = ((Vector) me.get("protTwo")).toLocation(world);
        breakBlacklist = (Blacklist) me.get("breakBlacklist");
        placeBlacklist = (Blacklist) me.get("placeBlacklist");
    }

    public boolean reset(String generator) {
        if(!removePlayers()) {
        	ChatUtil.getLogger().severe("Mine on file is located in an invalid world!");
        	return false;
        }
        try {
        	return GeneratorUtil.get(generator).run(this);
        } catch (NullPointerException npe) {
        	if(CommandManager.getSender() != null) ChatUtil.sendError("Invalid generator selected!");
        	else ChatUtil.getLogger().severe("Invalid generator selected!");
        	return false;
        }
    }

    private boolean removePlayers() {
    	if(world == null) return false;
        for (Player p : world.getPlayers()) {
            if (isLocationInMine(p.getLocation())) {
                p.teleport(tpPoint, PlayerTeleportEvent.TeleportCause.PLUGIN);
                ChatUtil.sendSuccess(p, Util.parseVars(Language.getString("misc.mine-teleport"), this));
            }
        }
        return true;
    }

    public boolean isLocationInMine(Location l) {
        return (l.getWorld().equals(world)
        		&& (l.getX() >= one.getX() && l.getX() <= two.getX())     //if x matches
                && (l.getY() >= one.getY() && l.getY() <= two.getY()) //and y
                && (l.getZ() >= one.getZ() && l.getZ() <= two.getZ())); //and z
    }
    
    public boolean isLocationInProtection(Location l) {
        return (l.getWorld().equals(world)
        		&& (l.getX() >= protOne.getX() && l.getX() <= protTwo.getX())     //if x matches
                && (l.getY() >= protOne.getY() && l.getY() <= protTwo.getY())     //and y
                && (l.getZ() >= protOne.getZ() && l.getZ() <= protTwo.getZ()));   //and z
    }

    public Map<String, Object> serialize() {
        Map<String, Object> me = new HashMap<String, Object>();
        me.put("one", one.toVector());
        me.put("two", two.toVector());
        me.put("world", world.getUID().toString());
        me.put("tpPoint", new SimpleLoc(tpPoint));
        me.put("displayName", displayName);
        me.put("name", name);
        me.put("parent", parent);
        me.put("blacklist", blacklist);
        me.put("generator", generator);
        me.put("silent", silent);
        me.put("automatic", automatic);
        me.put("automaticResetTime", automaticSeconds);
        me.put("cooldownEnabled", cooldownEnabled);
        me.put("cooldownSeconds", cooldownSeconds);
        me.put("isWarned", warned);
        me.put("warningTimes", warningTimes);
        List<String> names = new ArrayList<String>();
        for(Protection prot : enabledProtection)
        	names.add(prot.name());
        me.put("protectionTypes", names);
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
    
    public String getName() {
    	return name;
    }
    
    public String getDisplayName() {
    	return displayName;
    }
    
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
    
    public String getGenerator() {
    	return generator.toUpperCase();
    }
    
    public boolean getCooldown() {
    	return cooldownEnabled;
    }
    
    public int getCooldownTime() {
    	return cooldownSeconds;
    }
    
    public int getNextCooldown() {
    	return (int)(nextCooldownTicks / 20);
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
    
    public void setGenerator(String generator) {
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
