
package com.wolvencraft.MineReset;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.MineReset.cmd.*;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.events.*;
import com.wolvencraft.MineReset.util.Broadcast;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Mine;
import com.wolvencraft.MineReset.util.Util;

import couk.Adamki11s.AutoUpdater.Updater;

/**
 * Mine Reset
 * Copyright (C) 2012 bitWolfy
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
	private FileConfiguration regionData = null, languageData = null, signData = null;
	private File regionDataFile = null, languageDataFile = null, signDataFile = null;
	
	public static double curVer = 1.2;
	public static int curSubVer = 2;
	
	public void onEnable()
	{
		log = this.getLogger();
		
		manager = new CommandManager(this);
		getCommand("mine").setExecutor(manager);
		
		new BlockBreakListener(this);
		new BlockPlaceListener(this);
		//new BucketEmptyListener(this);
		new PlayerInteractListener(this);
		new PlayerLoginListener(this);
        new PVPListener(this);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		getLanguageData().options().copyDefaults(true);
		saveLanguageData();
		Updater.checkVersion();
		

		List<String> mineList = Regions.getList("data.list-of-mines");
		log.info("MineReset started");
		log.info(mineList.size() + " mine(s) found");
		
		if(!Configuration.exists("configuration.version") || !Configuration.getString("configuration.version").equals(curVer + "." + curSubVer))
		{
			Config.update();
			log.info("Configuration successufully updated to the new data format");
		}
		
		final long checkEvery = getConfig().getLong("lag.check-time-every");
		
		
		if(getConfig().getBoolean("lag.automatic-resets-enabled"))
		{
			Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
			{
	           	 public void run()
	           	 {
					List<String> mineList = Regions.getList("data.list-of-mines");
					
					if(mineList.size() != 0)
					{
		                String warnMessage = Language.getString("reset.automatic-reset-warning");
		                
						for(String mineName : mineList)
						{
							String parentMine = Regions.getString("mines." + mineName + ".parent");
							boolean parent = false;
							if(parentMine == null)
							{
								parentMine = mineName;
								parent = true;
							}
							
							if(Regions.getBoolean("mines." + parentMine + ".reset.auto.reset"))
							{
								int nextReset = Mine.getNextReset(parentMine);
								List<String> warnTimes = Regions.getList("mines." + parentMine + ".reset.auto.warn-times");
								
								if(parent)
								{
									nextReset -= (int)checkEvery;
									Regions.setInt("mines." + mineName + ".reset.auto.data.next", nextReset);
									Regions.saveData();
								}
								
								SignCmd.updateAll(mineName);
								
								if(parent)
								{
									if(warnTimes.indexOf(nextReset + "") != -1 && Regions.getBoolean("mines." + parentMine + ".reset.auto.warn") && !Regions.getBoolean("mines." + parentMine + ".silent"))
									{
										Broadcast.sendSuccess(Util.parseVars(warnMessage, mineName));
									}
									if(nextReset <= 0)
									{
										String[] args = {"", mineName};
										Reset.run(args, true, null);
									}
								}
							}
						}
		            }
	           	}
	        }, 0, (long)(checkEvery * 20));
		}
    }
	
	
	public void onDisable()
	{
		log.info("MineReset stopped");
	}
	
	public void reloadRegionData() {
	    if (regionDataFile == null) {
	    regionDataFile = new File(getDataFolder(), "regions.yml");
	    }
	    regionData = YamlConfiguration.loadConfiguration(regionDataFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = getResource("regions.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        regionData.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getRegionData() {
	    if (regionData == null) {
	        reloadRegionData();
	    }
	    return regionData;
	}

	public void saveRegionData() {
	    if (regionData == null || regionDataFile == null) return;
	    try {
	        regionData.save(regionDataFile);
	    } catch (IOException ex) {
	        Message.log("Could not save config to " + regionDataFile);
	    }
	}
	
	public void reloadLanguageData() {
		
		String lang = this.getConfig().getString("configuration.language") + ".yml";
		Message.log("Language file used: " + lang);
		
	    if (languageDataFile == null) {
	    languageDataFile = new File(getDataFolder(), lang);
	    }
	    languageData = YamlConfiguration.loadConfiguration(languageDataFile);
	 
	    // Look for defaults in the jar
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
	        Message.log("Could not save config to " + languageDataFile);
	    }
	}
	
	public void reloadSignData() {
	    if (signDataFile == null) {
	    signDataFile = new File(getDataFolder(), "signs.yml");
	    }
	    signData = YamlConfiguration.loadConfiguration(signDataFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = getResource("signs.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        signData.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getSignData() {
	    if (signData == null) {
	        reloadSignData();
	    }
	    return signData;
	}

	public void saveSignData() {
	    if (signData == null || signDataFile == null) return;
	    try {
	        signData.save(signDataFile);
	    } catch (IOException ex) {
	        Message.log("Could not save config to " + signDataFile);
	    }
	}
}