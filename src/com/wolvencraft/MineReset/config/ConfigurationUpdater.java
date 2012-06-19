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
import com.wolvencraft.MineReset.util.MineUtils;
import com.wolvencraft.MineReset.util.SignUtils;

public class ConfigurationUpdater {
	
	private static FileConfiguration regionData = null;
	private static FileConfiguration signData = null;
	private static File regionDataFile = null;
	private static File signDataFile = null;
	
	public static boolean run() {
		if(Configuration.getString("configuration.version").equalsIgnoreCase("2.0.0")) return false;
		updateConfiguration();
		updateLanguage();
		updateRegions();
		updateSigns();
		return true;
	}
	
	public static void updateConfiguration() {
		CommandManager.getPlugin().getConfig().set("defaults", null);
		CommandManager.getPlugin().getConfig().set("versions.check-for-new-versions", null);
		CommandManager.getPlugin().getConfig().set("versions.check-for-recommended-builds", null);
		CommandManager.getPlugin().getConfig().set("versions.check-for-development-builds", null);
		CommandManager.getPlugin().getConfig().set("versions.remind-on-login", null);
		CommandManager.getPlugin().getConfig().set("versions.permissions-node", null);
		CommandManager.getPlugin().getConfig().set("versions", null);
		CommandManager.getPlugin().getConfig().set("configuration.teleport-out-of-the-mine-on-reset", null);
		
		if(!Configuration.getString("configuration.version").equalsIgnoreCase("1.2.1") && !Configuration.getString("configuration.version").equalsIgnoreCase("1.2.2")) {
			if(!CommandManager.getPlugin().getConfig().isSet("lag")) {
				CommandManager.getPlugin().getConfig().set("lag.automatic-resets-enabled", true);
				CommandManager.getPlugin().getConfig().set("lag.check-time-every", "20");
				CommandManager.getPlugin().getConfig().set("lag.protection-checks-enabled", "");
				CommandManager.getPlugin().getConfig().set("lag.teleport-out-of-the-mine-on-reset", "");
			}
			if(!CommandManager.getPlugin().getConfig().isSet("updater")) {
				CommandManager.getPlugin().getConfig().set("updater.channel", "db");
				CommandManager.getPlugin().getConfig().set("updater.remind-on-login", true);
				CommandManager.getPlugin().getConfig().set("updater.permission-node", "minereset.edit");
			}			
		}
		else {
			CommandManager.getPlugin().getConfig().set("lag.check-time-every", "20");
			if(!CommandManager.getPlugin().getConfig().isSet("updater")) {
				CommandManager.getPlugin().getConfig().set("updater.channel", "db");
				CommandManager.getPlugin().getConfig().set("updater.remind-on-login", true);
				CommandManager.getPlugin().getConfig().set("updater.permission-node", "minereset.edit");
			}			
		}
	}
	
	public static void updateLanguage() {
		CommandManager.getPlugin().getLanguageData().set("error.command", Language.getString("general.invalid-command"));
		CommandManager.getPlugin().getLanguageData().set("error.mine-name", Language.getString("general.mine-name-invalid"));
		CommandManager.getPlugin().getLanguageData().set("error.mine-not-selected", Language.getString("general.mine-not-selected"));
		CommandManager.getPlugin().getLanguageData().set("error.command", Language.getString("general.invalid-command"));
		CommandManager.getPlugin().getLanguageData().set("error.arguments", "Invalid parameters. Check your argument count!");
		CommandManager.getPlugin().getLanguageData().set("error.block-does-not-exist", "Block &c%BLOCK%&f does not exist");
		CommandManager.getPlugin().getLanguageData().set("error.removing-air", "This value is calculated automatically");
		
		CommandManager.getPlugin().getLanguageData().set("general.invalid-command", null);
		CommandManager.getPlugin().getLanguageData().set("general.mine-name-invalid", null);
		CommandManager.getPlugin().getLanguageData().set("general.mine-not-selected", null);
		CommandManager.getPlugin().getLanguageData().set("general.invalid-command", null);
		
		CommandManager.getPlugin().getLanguageData().set("misc.mine-teleport", Language.getString("teleportation.mine-teleport"));
		CommandManager.getPlugin().getLanguageData().set("teleportation.mine-teleport", null);
		
		CommandManager.getPlugin().getLanguageData().set("editing.mine-selected-successfully", Language.getString("general.mine-selected-successfully"));
		CommandManager.getPlugin().getLanguageData().set("editing.mine-deselected-successfully", Language.getString("general.mine-deselected-successfully"));
		
		CommandManager.getPlugin().getLanguageData().set("general.mine-selected-successfully", null);
		CommandManager.getPlugin().getLanguageData().set("general.mine-deselected-successfully", null);
	}
	
	public static void updateRegions() {
		List<String> mineList = getRegionData().getStringList("data.list-of-mines");
		List<Mine> mines = MineReset.getMines();
		for(String mine : mineList) {
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
			if(getRegionData().getBoolean("mines." + mine + ".protection.breaking.enabled")) enabledProt.add(Protection.BLOCK_BREAK);
			if(getRegionData().getBoolean("mines." + mine + ".protection.placement.enabled")) enabledProt.add(Protection.BLOCK_PLACE);
			if(getRegionData().getBoolean("mines." + mine + ".protection.PVP")) enabledProt.add(Protection.BLOCK_BREAK);
			
			mines.add(new Mine(one, two, world, tpPos, displayName, null, mine, blocks, gen, silent, automatic, automaticSeconds, false, 0, warnTimes, enabledProt, one, two));
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
			List<String> lines = new ArrayList<String>();
			for(int i = 0; i < 4; i++) {
				lines.add(getSignData().getString("signs." + sign + ".lines." + i));
			}
			
			signs.add(new SignClass(id, world, loc, parent, reset, lines));
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
