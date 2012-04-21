/**
 * 
 */
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
import com.wolvencraft.MineReset.events.*;


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
	public CommandManager manager;
	private FileConfiguration regionData = null;
	private File regionDataFile = null;
	private FileConfiguration oldConfig = null;
	private File oldConfigFile = null;
	Logger log;
	
	public void onEnable()
	{
		log = this.getLogger();
		
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		getRegionData().options().copyDefaults(true);
		saveRegionData();

		manager = new CommandManager(this);
		getCommand("mine").setExecutor(manager);
		
		new BlockBreakListener(this);
		new BlockPlaceListener(this);
		new PlayerInteractListener(this);
		
		List<String> mineList = Util.getRegionList("data.list-of-mines");
		log.info("MineReset started");
		log.info(mineList.size() + " mine(s) found");
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
           	 public void run()
           	 {
				List<String> mineList = Util.getRegionList("data.list-of-mines");

               	int min;
                int sec;
                List<String> warnTimes;
                String warnMessage = Util.getConfigString("messages.automatic-mine-reset-warning");
                
				for(String mineName : mineList)
				{
					min = Util.getRegionInt("mines." + mineName + ".reset.auto.data.min");
					sec = Util.getRegionInt("mines." + mineName + ".reset.auto.data.sec");
					warnTimes = Util.getRegionList("mines." + mineName + ".reset.auto.warn-times");
					
					sec--;
					if(sec <= 0)
					{
						min--;
						sec = 60 + sec;
					}
					
					Util.setRegionInt("mines." + mineName + ".reset.auto.data.min", min);
					Util.setRegionInt("mines." + mineName + ".reset.auto.data.sec", sec);
					Util.saveRegionData();
					
					int index = warnTimes.indexOf(min + "");
					if(index != -1 && sec == 30)
					{
		               	warnMessage = Util.parseString(warnMessage, "%MINE%", mineName);
		                warnMessage = Util.parseString(warnMessage, "%TIME%", warnTimes.get(index) + "");
		                Util.sendSuccess(warnMessage);
					}
					else if(min < 0 || (min == 0 && sec == 0))
					{
						String[] args = {"reset", mineName};
						Reset.run(args, true);
						min = Util.getRegionInt("mines." + mineName + ".reset.auto.reset-time");
						Util.setRegionInt("mines." + mineName + ".reset.auto.data.min", min);
						Util.setRegionInt("mines." + mineName + ".reset.auto.data.sec", 0);
						Util.saveRegionData();
					}
				}
            }
        }, 0, 20L);
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
	        Util.logWarning("Could not save config to " + regionDataFile);
	    }
	}
	
	public void reloadOldConfig() {
	    if (oldConfigFile == null) {
	    oldConfigFile = new File(getDataFolder(), "config.old.yml");
	    }
	    oldConfig = YamlConfiguration.loadConfiguration(regionDataFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = getResource("config.old.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        oldConfig.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getOldConfig() {
	    if (oldConfig == null) {
	        reloadOldConfig();
	    }
	    return oldConfig;
	}

	public void saveOldConfig() {
	    if (oldConfig == null || oldConfigFile == null) return;
	    try {
	        oldConfig.save(oldConfigFile);
	    } catch (IOException ex) {
	        Util.logWarning("Could not save config to " + oldConfigFile);
	    }
	}
}