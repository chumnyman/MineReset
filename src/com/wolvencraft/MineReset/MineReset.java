
package com.wolvencraft.MineReset;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.wolvencraft.AutoUpdater.Updater;
import com.wolvencraft.Metrics.Statistics;
import com.wolvencraft.MineReset.mine.Blacklist;
import com.wolvencraft.MineReset.mine.DataBlock;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.MineBlock;
import com.wolvencraft.MineReset.mine.Reset;
import com.wolvencraft.MineReset.mine.SignClass;
import com.wolvencraft.MineReset.mine.SimpleLoc;
import com.wolvencraft.MineReset.util.MineUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.MineReset.cmd.*;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.events.*;
import com.wolvencraft.MineReset.generation.BaseGenerator;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.GeneratorLoader;
import com.wolvencraft.MineReset.util.GeneratorUtil;
import com.wolvencraft.MineReset.util.SignUtil;
import com.wolvencraft.MineReset.util.Util;


/**
 * Mine Reset
 * Copyright (C) 2012 bitWolfy, jjkoletar
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

public class MineReset extends JavaPlugin
{
	private static WorldEditPlugin worldEditPlugin = null;
	
	Logger log;
	public CommandManager manager;
	private FileConfiguration languageData = null;
	private File languageDataFile = null;
	private static List<Mine> mines;
	private static List<SignClass> signs;
	private static List<BaseGenerator> generators;
	static Statistics stats;
	
	public void onEnable()
	{
		log = this.getLogger();
		
		log.info("MineReset is starting up");
		
		manager = new CommandManager(this);
		getCommand("mine").setExecutor(manager);
		
		worldEditPlugin = (WorldEditPlugin) this.getServer().getPluginManager().getPlugin("WorldEdit");
		if(worldEditPlugin != null) log.info("WorldEdit found, using it for region selection");
		
		ChatUtil.debug("Started up the command manager");
		
		new BlockBreakListener(this);
		new BlockPlaceListener(this);
		new BucketEmptyListener(this);
		new BucketFillListener(this);
		new PlayerInteractListener(this);
		new PlayerLoginListener(this);
		new PVPListener(this);
		ChatUtil.debug("Started up event listeners");
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		getLanguageData().options().copyDefaults(true);
		saveLanguageData();
		ChatUtil.debug("Loaded configuration");
		
		Updater.checkVersion();
		ChatUtil.debug("Checked plugin version");
		
		ConfigurationSerialization.registerClass(Mine.class, "Mine");
		ConfigurationSerialization.registerClass(MineBlock.class, "MineBlock");
		ConfigurationSerialization.registerClass(Blacklist.class, "Blacklist");
		ConfigurationSerialization.registerClass(SignClass.class, "SignClass");
		ConfigurationSerialization.registerClass(DataBlock.class, "DataBlock");
		ConfigurationSerialization.registerClass(SimpleLoc.class, "SimpleLoc");
		
		ChatUtil.debug("Registred serializable classes");
		
		mines = new ArrayList<Mine>();
		mines = MineUtil.loadAll(mines);
		
		signs = new ArrayList<SignClass>();
		signs = SignUtil.loadAll(signs);
		
		generators = new ArrayList<BaseGenerator>();
		generators = GeneratorUtil.loadDefault(generators);
		generators = GeneratorLoader.load(generators);
		
		ChatUtil.debug("Loaded data from file");
		
		ChatUtil.debug("Starting PluginMetrics");
		stats = new Statistics(this);
		stats.gatherData();
		stats.getMetrics().start();
		
		log.info("MineReset started");
		log.info(mines.size() + " mine(s) found");
		
		ChatUtil.debug("Starting up timer");
		final long checkEvery = getConfig().getLong("misc.check-time-every");
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for(Mine curMine : mines) {
					if(curMine.getAutomatic() && MineUtil.getMine(curMine.getParent()) == null) {
						int nextReset = MineUtil.getNextReset(curMine);
						List<Integer> warnTimes = curMine.getWarningTimes();
						
						if(!curMine.getSilent() && curMine.getWarned() && warnTimes.indexOf(new Integer(nextReset)) != -1) {
							ChatUtil.broadcast(Util.parseVars(Language.getString("reset.automatic-reset-warning"), curMine), curMine.getWorld());
						}
						
						if(nextReset <= 0) {
							String[] args = {null, curMine.getName()};
							ResetCommand.run(args, Reset.AUTOMATIC, "");
							stats.updateAutomatic();
						}						

						curMine.updateTimer(checkEvery);
					}
				
					if(curMine.getCooldown() && curMine.getCooldownTime() > 0)
						curMine.updateCooldown(checkEvery);
							
					SignUtil.updateAll(curMine);
				}
			}
		}, 0, checkEvery);
	}
	
	
	public void onDisable()
	{
		MineUtil.saveAll(mines);
		SignUtil.saveAll(signs);
		
		getServer().getScheduler().cancelTasks(this); // Got to stop the task
		log.info("MineReset stopped");
	}
	
	public void reloadLanguageData() {
		
		String lang = this.getConfig().getString("configuration.language");
		if(lang.equals(null)) lang = "english";
		lang = lang + ".yml";
		ChatUtil.log("Language file used: " + lang);
		
		if (languageDataFile == null) {
		languageDataFile = new File(getDataFolder(), lang);
		}
		languageData = YamlConfiguration.loadConfiguration(languageDataFile);
		
		InputStream defConfigStream = getResource(lang);
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			languageData.setDefaults(defConfig);
		}
	}
	
	public FileConfiguration getLanguageData() {
		if (languageData == null) {
			reloadLanguageData();
		}
		return languageData;
	}

	public void saveLanguageData() {
		if (languageData == null || languageDataFile == null) return;
		try {
			languageData.save(languageDataFile);
		} catch (IOException ex) {
			ChatUtil.log("Could not save config to " + languageDataFile);
		}
	}

	public static List<Mine> getMines() {
		return mines;
	}
	
	public static void setMines(List<Mine> mines) {
		MineReset.mines = mines;
	}
	
	public static List<SignClass> getSigns() {
		return signs;
	}
	
	public static void setSigns(List<SignClass> signs) {
		MineReset.signs = signs;
	}
	
	public static List<BaseGenerator> getGenerators() {
		return generators;
	}
	
	public static Statistics getStats() {
		return stats;
	}
	
	public static WorldEditPlugin getWorldEditPlugin() {
		return worldEditPlugin;
	}
}