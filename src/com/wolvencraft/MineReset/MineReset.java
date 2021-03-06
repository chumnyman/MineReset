/*
 * MineReset.java
 * 
 * MineReset
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.MineReset;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.wolvencraft.AutoUpdater.Updater;
import com.wolvencraft.Metrics.Statistics;
import com.wolvencraft.MineReset.mine.Blacklist;
import com.wolvencraft.MineReset.mine.DataBlock;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.MineBlock;
import com.wolvencraft.MineReset.mine.SignClass;
import com.wolvencraft.MineReset.mine.SimpleLoc;
import com.wolvencraft.MineReset.util.MineUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.events.*;
import com.wolvencraft.MineReset.generation.BaseGenerator;
import com.wolvencraft.MineReset.util.ChatUtil;
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

public class MineReset extends JavaPlugin {
    private static WorldEditPlugin worldEditPlugin = null;
    
    public CommandManager manager;
    private FileConfiguration languageData = null;
    private File languageDataFile = null;
    private static List<Mine> mines;
    private static List<SignClass> signs;
    private static List<BaseGenerator> generators;
    private static Statistics stats;
    
    public void onEnable() {
        ChatUtil.log("MineReset is starting up");
        
        manager = new CommandManager(this);
        getCommand("mine").setExecutor(manager);
        
        worldEditPlugin = (WorldEditPlugin) this.getServer().getPluginManager().getPlugin("WorldEdit");
        if(worldEditPlugin != null) ChatUtil.log("WorldEdit found, using it for region selection");
        
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
        
        ConfigurationSerialization.registerClass(Mine.class, "Mine");
        ConfigurationSerialization.registerClass(MineBlock.class, "MineBlock");
        ConfigurationSerialization.registerClass(Blacklist.class, "Blacklist");
        ConfigurationSerialization.registerClass(SignClass.class, "SignClass");
        ConfigurationSerialization.registerClass(DataBlock.class, "DataBlock");
        ConfigurationSerialization.registerClass(SimpleLoc.class, "SimpleLoc");
        
        ChatUtil.debug("Registred serializable classes");
        
        mines = MineUtil.loadAll();
        signs = SignUtil.loadAll();
        generators = GeneratorUtil.loadAll();
        
        ChatUtil.debug("Loaded data from file");
        
        ChatUtil.debug("Starting PluginMetrics");
        stats = new Statistics(this);
        stats.gatherData();
        stats.getMetrics().start();
        
        ChatUtil.log("MineReset started");
        ChatUtil.log(mines.size() + " mine(s) and " + signs.size() + " signs found");
        
        Updater.checkVersion();
        
        ChatUtil.debug("Starting up the timer");
        final long checkEvery = getConfig().getLong("misc.check-time-every");
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                for(Mine curMine : mines) {
                    if(curMine.getAutomatic() && MineUtil.getMine(curMine.getParent()) == null) {
                        int nextReset = MineUtil.getNextReset(curMine);
                        List<Integer> warnTimes = curMine.getWarningTimes();
                        
                        if(!curMine.getSilent() && curMine.getWarned() && warnTimes.indexOf(new Integer(nextReset)) != -1)
                            ChatUtil.broadcast(Util.parseVars(Language.getString("reset.automatic-reset-warning"), curMine));
                        
                        if(nextReset <= 0) {
                            MineCommand.RESET.run(curMine.getName());
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
        MineUtil.saveAll();
        SignUtil.saveAll();
        
        getServer().getScheduler().cancelTasks(this);
        ChatUtil.log("MineReset stopped");
    }
    
    public void reloadLanguageData() {
        
        String lang = this.getConfig().getString("configuration.language");
        if(lang == null) lang = "english";
        lang = lang + ".yml";
        ChatUtil.log("Language file used: " + lang);
        
        if (languageDataFile == null) languageDataFile = new File(getDataFolder(), lang);
        languageData = YamlConfiguration.loadConfiguration(languageDataFile);
        
        InputStream defConfigStream = getResource(lang);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            languageData.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getLanguageData() {
        if (languageData == null) reloadLanguageData();
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