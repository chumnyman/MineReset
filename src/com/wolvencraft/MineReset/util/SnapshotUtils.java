package com.wolvencraft.MineReset.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Snapshot;

public class SnapshotUtils {
	/**
	 * Saves the mine data to disc
	 * @param mine Individual mine to save
	 */
	public static void save(Snapshot snap) {
		File snapFile = new File(new File(CommandManager.getPlugin().getDataFolder(), "snapshots"), snap.getParent() + ".yml");
        FileConfiguration snapConf =  YamlConfiguration.loadConfiguration(snapFile);
        snapConf.set("snapshot", snap);
        try {
            snapConf.save(snapFile);
        } catch (IOException e) {
        	CommandManager.getPlugin().getLogger().severe("[MineReset] Unable to serialize snapshot '" + snap.getParent() + "'!");
            e.printStackTrace();
        }
	}
	
	/**
	 * Saves the mine data to disc
	 * @param mines List of mines to save
	 */
	public static void saveAll(List<Snapshot> snaps) {
		for (Snapshot snap : snaps) {
            File snapFile = new File(new File(CommandManager.getPlugin().getDataFolder(), "snapshots"), snap.getParent() + ".yml");
            FileConfiguration snapConf =  YamlConfiguration.loadConfiguration(snapFile);
            snapConf.set("snapshot", snap);
            try {
                snapConf.save(snapFile);
            } catch (IOException e) {
            	CommandManager.getPlugin().getLogger().severe("[MineReset] Unable to serialize snapshot '" + snap.getParent() + "'!");
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * Loads the mine data from disc
	 * @param mines List of mines to write the data to
	 * @return Loaded list of mines
	 */
	public static List<Snapshot> loadAll(List<Snapshot> snaps) {
		snaps.clear();
		File snapFolder = new File(CommandManager.getPlugin().getDataFolder(), "snapshots");
        if (!snapFolder.exists() || !snapFolder.isDirectory()) {
            snapFolder.mkdir();
        }
        File[] snapFiles = snapFolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().contains(".yml");
            }
        });

        for (File snapFile : snapFiles) {
            FileConfiguration snapConf = YamlConfiguration.loadConfiguration(snapFile);
            Object snapshot = snapConf.get("snapshot");
            if (snapshot instanceof Snapshot) {
                snaps.add((Snapshot) snapshot);
            }
        }
        return snaps;
	}
	
	public static boolean delete(Snapshot snap) {
		File snapFolder = new File(CommandManager.getPlugin().getDataFolder(), "snapshots");
		if(!snapFolder.exists() || !snapFolder.isDirectory()) return false;
		
		File[] snapFiles = snapFolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().contains(".yml");
            }
        });
		
		for(File snapFile : snapFiles) {
			if(snapFile.getName().equals(snap.getParent() + ".yml")) {
				return snapFile.delete();
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the object of the mine with the given id
	 * @param id Name of the mine being checked
	 * @return Mine object if it exists, null if it does not
	 */
	public static Snapshot getSnapshot(Mine parent)
	{
		for(Snapshot curSnap : MineReset.getSnapshots()) {
			if(curSnap.getParent().equals(parent)) return curSnap;
		}
		return null;
	}
}
