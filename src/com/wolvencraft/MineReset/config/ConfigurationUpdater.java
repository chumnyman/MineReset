package com.wolvencraft.MineReset.config;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.Generator;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.MineBlock;
import com.wolvencraft.MineReset.mine.Protection;
import com.wolvencraft.MineReset.mine.SignClass;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.MineUtils;
import com.wolvencraft.MineReset.util.SignUtils;

public class ConfigurationUpdater {
	
	private static FileConfiguration regionData = null;
	private static FileConfiguration signData = null;
	private static File regionDataFile = null;
	private static File signDataFile = null;
	
	public static void updateRegions() {
		List<String> mineList = getRegionData().getStringList("data.list-of-mines");
		List<Mine> mines = MineReset.getMines();
		for(String mine : mineList) {
			if(MineUtils.getMine(mine) != null) {
				Message.sendError("Mine '" + mine + "' is already in the system and was not imported");
				continue;
			}
			String displayName = getRegionData().getString("mines." + mine + ".display-name");
			boolean silent = getRegionData().getBoolean("mines." + mine + ".silent");
			World world = CommandManager.getPlugin().getServer().getWorld(getRegionData().getString("mines." + mine + ".coordinates.world"));
			Location one = new Location(world, getRegionData().getInt("mines." + mine + ".coordinates.pos0.x"), getRegionData().getInt("mines." + mine + ".coordinates.pos0.y"), getRegionData().getInt("mines." + mine + ".coordinates.pos0.z"));
			Location two = new Location(world, getRegionData().getInt("mines." + mine + ".coordinates.pos1.x"), getRegionData().getInt("mines." + mine + ".coordinates.pos1.y"), getRegionData().getInt("mines." + mine + ".coordinates.pos1.z"));
			Location tpPos = new Location(world, getRegionData().getDouble("mines." + mine + ".coordinates.pos2.x"), getRegionData().getDouble("mines." + mine + ".coordinates.pos2.y"), getRegionData().getDouble("mines." + mine + ".coordinates.pos2.z"), (float)getRegionData().getDouble("mines." + mine + ".coordinates.pos2.yaw"), (float)getRegionData().getDouble("mines." + mine + ".coordinates.pos2.pitch"));
			
			List<MineBlock> blocks = new ArrayList<MineBlock>();
			List<String> iBlocks = getRegionData().getStringList("mines." + mine + ".materials.blocks");
			List<Double> iWeight = getRegionData().getDoubleList("mines." + mine + ".materials.weights");
			
			for(int i = 0; i < iBlocks.size(); i++) {
				String[] parts = iBlocks.get(i).split(":");
				blocks.add(new MineBlock(new MaterialData(Integer.parseInt(parts[0]),Byte.parseByte(parts[1])), iWeight.get(i) / 100));
			}
			
			Generator gen = Generator.valueOf(getRegionData().getString("mines." + mine + ".reset.generator"));
			boolean automatic = getRegionData().getBoolean("mines." + mine + ".reset.auto.reset");
			int automaticSeconds = getRegionData().getInt("mines." + mine + ".reset.auto.reset-every");
			List<Integer> warnTimes = getRegionData().getIntegerList("mines" + mine + ".reset.auto.warn-times");
			List<Protection> enabledProt = new ArrayList<Protection>();
			if(getRegionData().getBoolean("mines." + mine + ".protection.PVP")) enabledProt.add(Protection.BLOCK_BREAK);
			
			mines.add(new Mine(one, two, world, tpPos, displayName, null, mine, blocks, gen, silent, automatic, automaticSeconds, false, 0, warnTimes, enabledProt, one, two));
			Message.sendSuccess("Mine '" + mine + "' was successfully imported into the system");
		}
		MineUtils.saveAll(mines);
	}
	
	public static void updateSigns() {
		List<String> signList = getSignData().getStringList("data.list-of-signs");
		List<SignClass> signs = MineReset.getSigns();
		
		for(String sign : signList) {
			String id = SignUtils.generateId();
			Mine parent = MineUtils.getMine(getSignData().getString("signs." + sign + ".mine"));
			boolean reset = getSignData().getBoolean("signs." + sign + ".reset");
			World world = CommandManager.getPlugin().getServer().getWorld(getSignData().getString("signs." + sign + ".world"));
			Location loc = new Location(world, getSignData().getInt("signs." + sign + ".x"), getSignData().getInt("signs." + sign + ".y"), getSignData().getInt("signs." + sign + ".z"));
			if(SignUtils.getSignAt(loc) != null) {
				Message.sendError("A sign is already defined at (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")!");
				continue;
			}
			List<String> lines = new ArrayList<String>();
			for(int i = 0; i < 4; i++) {
				lines.add(getSignData().getString("signs." + sign + ".lines." + i));
			}
			
			signs.add(new SignClass(id, world, loc, parent, reset, lines));
			Message.sendSuccess("A sign at (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ") was imported into the system!");
		}
		
		SignUtils.saveAll(signs);
	}
	
	public static void reloadRegionData() {
	    if (regionDataFile == null)
	    regionDataFile = new File(CommandManager.getPlugin().getDataFolder(), "regions.yml");
	    regionData = YamlConfiguration.loadConfiguration(regionDataFile);

	    InputStream defConfigStream = CommandManager.getPlugin().getResource("regions.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        regionData.setDefaults(defConfig);
	    }
	}

	public static FileConfiguration getRegionData() {
	    if (regionData == null)
	        reloadRegionData();
	    return regionData;
	}
	
	public static void reloadSignData() {
	    if (signDataFile == null)
	    	signDataFile = new File(CommandManager.getPlugin().getDataFolder(), "signs.yml");
	    signData = YamlConfiguration.loadConfiguration(signDataFile);

	    InputStream defConfigStream = CommandManager.getPlugin().getResource("signs.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        signData.setDefaults(defConfig);
	    }
	}

	public static FileConfiguration getSignData() {
	    if (signData == null)
	        reloadSignData();
	    return signData;
	}
}
