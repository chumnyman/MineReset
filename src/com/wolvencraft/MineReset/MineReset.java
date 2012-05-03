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
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.events.*;

import couk.Adamki11s.AutoUpdater.AUCore;


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
	private static AUCore core;
	public CommandManager manager;
	private FileConfiguration regionData = null, languageData = null, signData = null;
	private File regionDataFile = null, languageDataFile = null, signDataFile = null;
	
	public static double curVer = 1.1;
	public static int curSubVer = 5;
	
	public void onEnable()
	{
		log = this.getLogger();
		
		core = new AUCore("http://wolvencraft.com/plugins/MineReset/index.html", log);
		

		core.checkVersion();
		
		manager = new CommandManager(this);
		getCommand("mine").setExecutor(manager);
		
		new BlockBreakListener(this);
		new BlockPlaceListener(this);
		//new BucketEmptyListener(this);
		new PlayerInteractListener(this);
		new PlayerLoginListener(this);
		
		List<String> mineList = Regions.getList("data.list-of-mines");
		log.info("MineReset started");
		log.info(mineList.size() + " mine(s) found");
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
           	 public void run()
           	 {
				List<String> mineList = Regions.getList("data.list-of-mines");

               	int min;
                int sec;
                boolean reset;
                List<String> warnTimes;
                String warnMessage = Language.getString("reset.automatic-reset-warning");
                
				for(String mineName : mineList)
				{
					reset = Regions.getBoolean("mines." + mineName + ".reset.auto.reset");
					if(reset)
					{
						min = Regions.getInt("mines." + mineName + ".reset.auto.data.min");
						sec = Regions.getInt("mines." + mineName + ".reset.auto.data.sec");
						warnTimes = Regions.getList("mines." + mineName + ".reset.auto.warn-times");
						
						sec--;
						if(sec <= 0)
						{
							min--;
							sec = 60 + sec;
						}
						
						Regions.setInt("mines." + mineName + ".reset.auto.data.min", min);
						Regions.setInt("mines." + mineName + ".reset.auto.data.sec", sec);
						
						Regions.saveData();
						
						SignCmd.updateAll(mineName);
						
						int index = warnTimes.indexOf(min + "");
						if(index != -1 && sec == 1)
						{
							if(Regions.getBoolean("mines." + mineName + ".reset.auto.warn"))
							{
				               	warnMessage = Util.parseVars(warnMessage, mineName);
				                Util.broadcastSuccess(warnMessage);
							}
						}
						else if(min < 0 || (min == 0 && sec == 0))
						{
							String[] args = {"reset", mineName};
							Reset.run(args, true);
						}
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
	        Util.log("Could not save config to " + regionDataFile);
	    }
	}
	
	public void reloadLanguageData() {
		
		String lang = this.getConfig().getString("configuration.language") + ".yml";
		Util.log("Language file used: " + lang);
		
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
	        Util.log("Could not save config to " + languageDataFile);
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
	        Util.log("Could not save config to " + signDataFile);
	    }
	}
}