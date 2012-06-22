package com.wolvencraft.MineReset.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.SignClass;

public class SignUtils {
	/**
	 * Saves the mine data to disc
	 * @param mine Individual mine to save
	 */
	public static void save(SignClass sign) {
		File signFile = new File(new File(CommandManager.getPlugin().getDataFolder(), "signs"), sign.getId() + ".yml");
        FileConfiguration signConf =  YamlConfiguration.loadConfiguration(signFile);
        signConf.set("signclass", sign);
        try {
            signConf.save(signFile);
        } catch (IOException e) {
        	CommandManager.getPlugin().getLogger().severe("[MineReset] Unable to serialize sign '" + sign.getId() + "'!");
            e.printStackTrace();
        }
	}
	
	/**
	 * Saves the mine data to disc
	 * @param mines List of mines to save
	 */
	public static void saveAll(List<SignClass> signs) {
		for (SignClass sign : signs) {
			File signFile = new File(new File(CommandManager.getPlugin().getDataFolder(), "signs"), sign.getId() + ".yml");
	        FileConfiguration signConf =  YamlConfiguration.loadConfiguration(signFile);
	        signConf.set("signclass", sign);
	        try {
	            signConf.save(signFile);
	        } catch (IOException e) {
	        	CommandManager.getPlugin().getLogger().severe("[MineReset] Unable to serialize sign '" + sign.getId() + "'!");
	            e.printStackTrace();
	        }
        }
	}
	
	/**
	 * Loads the mine data from disc
	 * @param mines List of mines to write the data to
	 * @return Loaded list of mines
	 */
	public static List<SignClass> loadAll(List<SignClass> signs) {
		File signFolder = new File(CommandManager.getPlugin().getDataFolder(), "signs");
        if (!signFolder.exists() || !signFolder.isDirectory()) {
            signFolder.mkdir();
        }
        File[] signFiles = signFolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().contains(".yml");
            }
        });
        
        signs.clear();
        for (File signFile : signFiles) {
            FileConfiguration mineConf = YamlConfiguration.loadConfiguration(signFile);
            Object sign = mineConf.get("signclass");
            if (sign instanceof SignClass) {
                signs.add((SignClass) sign);
            }
        }
        return signs;
	}
	
	public static boolean delete(SignClass sign) {
		File signFolder = new File(CommandManager.getPlugin().getDataFolder(), "signs");
		if(!signFolder.exists() || !signFolder.isDirectory()) return false;
		
		File[] signFiles = signFolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().contains(".yml");
            }
        });
		
		for(File signFile : signFiles) {
			if(signFile.getName().equals(sign.getId() + ".yml")) {
				return signFile.delete();
			}
		}
		
		return false;
	}
	
	public static String generateId() {
		Random r = new Random();
		return Long.toString(Math.abs(r.nextLong()), 36);
	}
	
	/**
	 * Checks if a sign exists at a specified location
	 * @param loc Location to check
	 * @return true if a sign exists, false otherwise
	 */
	public static boolean exists(Location loc) {
		List<SignClass> signs = MineReset.getSigns();
		for(SignClass sign : signs) {
			if(sign.getLocation().equals(loc)) return true;
		}
		return false;
	}
	
	/**
	 * Returns the sign at a specified location if it exists
	 * @param loc Location to check
	 * @return SignClass object if it exists, null otherwise
	 */
	public static SignClass getSignAt(Location loc) {
		List<SignClass> signs = MineReset.getSigns();
		for(SignClass sign : signs) {
			if(sign.getLocation().equals(loc)) return sign;
		}
		return null;
	}
	
	/**
	 * Checks if a specified sign is defined as a SignClass
	 * @param sign Sign to check
	 * @return SignClass object if it is defined, null otherwise
	 */
	public static SignClass getSign(Sign sign) {
		return getSignAt(sign.getLocation());
	}
	
	/**
	 * Updates a sign specified with the values of the variables of its parent mine
	 * @param signBlock Sign to update
	 * @return An updated sign
	 */
	public static Sign update(Sign signBlock) {
		SignClass sign = SignUtils.getSign(signBlock);
		List<String> lines = sign.getLines();
		for(int i = 0; i < lines.size(); i++) {
			Mine curMine = MineUtils.getMine(sign.getParent().getName());
			signBlock.setLine(i, Util.parseVars(lines.get(i), curMine));
		}
		
		return signBlock;
	}
	
	/**
	 * Updates all the signs related to the mine specified.<br />
	 * If no mine is specified, updates everything.
	 * @param curMine Mine to update the signs of or <i>null</i> to update all the signs
	 */
	public static void updateAll(Mine curMine) {
		List<SignClass> signList = MineReset.getSigns();

		if(curMine != null) {
			for(SignClass sign : signList) {
				if(sign.getParent().equals(curMine)) {
					BlockState b = sign.getLocation().getBlock().getState();
					if(b instanceof Sign) {
						Sign signBlock = (Sign) b;
						signBlock = update(signBlock);
						signBlock.update(true);
					}
				}
			}
		}
		else {
            Message.debug("Updating everything");
			for(SignClass sign : signList) {
				BlockState b = sign.getLocation().getBlock().getState();
				if(b instanceof Sign) {
					Sign signBlock = (Sign) b;
					signBlock = update(signBlock);
					signBlock.update(true);
				}
			}
		}
		return;
	}
}
