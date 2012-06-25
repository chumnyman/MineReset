
package com.wolvencraft.MineReset;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.wolvencraft.AutoUpdater.Updater;
import com.wolvencraft.Metrics.Statistics;
import com.wolvencraft.MineReset.mine.Blacklist;
import com.wolvencraft.MineReset.mine.DataBlock;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.MineBlock;
import com.wolvencraft.MineReset.mine.SignClass;
import com.wolvencraft.MineReset.mine.SimpleLoc;
import com.wolvencraft.MineReset.mine.Snapshot;
import com.wolvencraft.MineReset.util.MineUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.MineReset.cmd.*;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.events.*;
import com.wolvencraft.MineReset.generation.BaseGenerator;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.GeneratorLoader;
import com.wolvencraft.MineReset.util.GeneratorUtil;
import com.wolvencraft.MineReset.util.SignUtil;
import com.wolvencraft.MineReset.util.SnapshotUtil;
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
	Logger log;
	public CommandManager manager;
	private FileConfiguration languageData = null;
	private File languageDataFile = null;
	private static List<Mine> mines;
	private static List<SignClass> signs;
	private static List<Snapshot> snapshots;
	private static List<BaseGenerator> generators;
	
	public void onEnable()
	{
		log = this.getLogger();
		
		manager = new CommandManager(this);
		getCommand("mine").setExecutor(manager);
		
		new BlockBreakListener(this);
		new BlockPlaceListener(this);
		new BucketEmptyListener(this);
		new BucketFillListener(this);
		new PlayerInteractListener(this);
		new PlayerLoginListener(this);
		new PVPListener(this);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		getLanguageData().options().copyDefaults(true);
		saveLanguageData();
		Updater.checkVersion();

		ConfigurationSerialization.registerClass(Mine.class, "Mine");
		ConfigurationSerialization.registerClass(MineBlock.class, "MineBlock");
		ConfigurationSerialization.registerClass(Blacklist.class, "Blacklist");
		ConfigurationSerialization.registerClass(SignClass.class, "SignClass");
		ConfigurationSerialization.registerClass(Snapshot.class, "Snapshot");
		ConfigurationSerialization.registerClass(DataBlock.class, "DataBlock");
		ConfigurationSerialization.registerClass(SimpleLoc.class, "SimpleLoc");
		
		mines = new ArrayList<Mine>();
		mines = MineUtil.loadAll(mines);
		
		signs = new ArrayList<SignClass>();
		signs = SignUtil.loadAll(signs);
		
		snapshots = new ArrayList<Snapshot>();
		snapshots = SnapshotUtil.loadAll(snapshots);
		
		generators = new ArrayList<BaseGenerator>();
		generators = GeneratorUtil.loadDefault(generators);
		generators = GeneratorLoader.load(generators);
		
		log.info("MineReset started");
		log.info(mines.size() + " mine(s) found");
		
		if(Configuration.getBoolean("configuration.metrics-enabled")) {
			log.info("Starting PluginMetrics");
			Statistics stats = new Statistics(this);
			stats.gatherData();
			stats.getMetrics().start();
		}
		
		final long checkEvery = getConfig().getLong("lag.check-time-every");
		
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			public void run() {
				for(Mine curMine : mines) {
					if(curMine.getAutomatic() && MineUtil.getMine(curMine.getParent()) == null) {
						int nextReset = MineUtil.getNextReset(curMine);
						List<Integer> warnTimes = curMine.getWarningTimes();
						curMine.updateTimer(checkEvery);
									
						if(!curMine.getSilent() && curMine.getWarned() && warnTimes.indexOf(nextReset) != -1)
							ChatUtil.broadcast(Util.parseVars(Language.getString("reset.automatic-reset-warning"), curMine));
						
						if(nextReset <= 0) {
							String[] args = {null, curMine.getName()};
							ResetCommand.run(args, true, null);
						}
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
		SnapshotUtil.saveAll(snapshots);
		
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
	
	public static List<Snapshot> getSnapshots() {
		return snapshots;
	}
	
	public static void setSnapshots(List<Snapshot> snapshots) {
		MineReset.snapshots = snapshots;
	}
	
	public static List<BaseGenerator> getGenerators() {
		return generators;
	}
}