
package com.wolvencraft.MineReset;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.MineBlock;
import com.wolvencraft.MineReset.mine.SignClass;
import com.wolvencraft.MineReset.util.MineUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.MineReset.cmd.*;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.events.*;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.SignUtils;
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
	private FileConfiguration languageData = null;
	private File languageDataFile = null;
    private static List<Mine> mines;
    private static List<SignClass> signs;
	
	public static double curVer = 1.2;
	public static int curSubVer = 2;
	
	public void onEnable()
	{
		log = this.getLogger();
		
		manager = new CommandManager(this);
		getCommand("mine").setExecutor(manager);
		
		new BlockBreakListener(this);
		new BlockPlaceListener(this);
		new BucketEmptyListener(this);
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
        
		mines = new ArrayList<Mine>();
        //TODO: Load mines into new data structure

		log.info("MineReset started");
		log.info(mines.size() + " mine(s) found");
		
		final long checkEvery = getConfig().getLong("lag.check-time-every");
		
		
		if(getConfig().getBoolean("lag.automatic-resets-enabled")) {
			Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
	           	 public void run() {
					if(mines.size() != 0) {
		                String warnMessage = Language.getString("reset.automatic-reset-warning");
		                for(Mine curMine : mines) {
							Mine parentMine = curMine.getParent();
							if(parentMine == null) parentMine = curMine;
							if(curMine.getAutomatic()) {
								int nextReset = MineUtils.getNextReset(curMine);
								List<Integer> warnTimes = parentMine.getWarningTimes();
								
								if(parentMine.equals(curMine)) {
									curMine.updateTimer(checkEvery);
									SignUtils.updateAll(parentMine);
									
									if(warnTimes.indexOf(nextReset) != -1 && curMine.getWarned() && !curMine.getSilent())
										Message.broadcast(Util.parseVars(warnMessage, curMine));
									if(nextReset <= 0)
									{
										String[] args = {"", curMine.getName()};
										ResetCommand.run(args, true, null);
									}
								}
							}
						}
		            }
	           	}
	        }, 0, checkEvery);
		}
    }
	
	
	public void onDisable()
	{
		//TODO Save the data to file
		log.info("MineReset stopped");
	}
	
	public void reloadLanguageData() {
		
		String lang = this.getConfig().getString("configuration.language") + ".yml";
		if(lang.equals(null)) lang = "english";
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
}