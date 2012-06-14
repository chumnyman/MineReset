package com.wolvencraft.MineReset.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.MineBlock;

public class MineUtils
{
	/**
	 * Saves the mine data to disc
	 * @param mine Individual mine to save
	 */
	public static void save(Mine mine) {
		File mineFile = new File(new File(CommandManager.getPlugin().getDataFolder(), "mines"), mine.getName() + ".yml");
        FileConfiguration mineConf =  YamlConfiguration.loadConfiguration(mineFile);
        mineConf.set("mine", mine);
        try {
            mineConf.save(mineFile);
        } catch (IOException e) {
        	CommandManager.getPlugin().getLogger().severe("[MineReset] Unable to serialize mine '" + mine.getName() + "'!");
            e.printStackTrace();
        }
	}
	
	/**
	 * Saves the mine data to disc
	 * @param mines List of mines to save
	 */
	public static void saveAll(List<Mine> mines) {
		for (Mine mine : mines) {
            File mineFile = new File(new File(CommandManager.getPlugin().getDataFolder(), "mines"), mine.getName() + ".yml");
            FileConfiguration mineConf =  YamlConfiguration.loadConfiguration(mineFile);
            mineConf.set("mine", mine);
            try {
                mineConf.save(mineFile);
            } catch (IOException e) {
            	CommandManager.getPlugin().getLogger().severe("[MineReset] Unable to serialize mine '" + mine.getName() + "'!");
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * Loads the mine data from disc
	 * @param mines List of mines to write the data to
	 * @return Loaded list of mines
	 */
	public static List<Mine> loadAll(List<Mine> mines) {
		File mineFolder = new File(CommandManager.getPlugin().getDataFolder(), "mines");
        if (!mineFolder.exists() || !mineFolder.isDirectory()) {
            mineFolder.mkdir();
        }
        File[] mineFiles = mineFolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().contains(".yml");
            }
        });

        for (File mineFile : mineFiles) {
            FileConfiguration mineConf = YamlConfiguration.loadConfiguration(mineFile);
            Object mine = mineConf.get("mine");
            if (mine instanceof Mine) {
                mines.add((Mine) mine);
            }
        }
        return mines;
	}
	
	/**
	 * Returns the object of the mine with the given id
	 * @param id Name of the mine being checked
	 * @return Mine object if it exists, null if it does not
	 */
	public static Mine getMine(String id)
	{
		for(Mine curMine : MineReset.getMines())
		{
			if(curMine.getName().equalsIgnoreCase(id)) return curMine;
		}
		return null;
	}
	
	/**
	 * Sorts the list of blocks in the mine from the one with the largest percentage
	 * to the one with the lowest and returns the formatted list
	 * @param curMine Mine to check
	 * @return Formatted list
	 */
	public static List<String> getSortedList(Mine curMine)
	{
		List<MineBlock> blocks = curMine.getBlocks();
		List<String> finalList = new ArrayList<String>(blocks.size());
		
		MineBlock tempBlock;
		for(int j = blocks.size(); j > 0; j--)
		{
			for(int i = 0; i < (j - 1); i++)
			{
				if(blocks.get(i + 1).getChance() > blocks.get(i).getChance())
				{
					tempBlock = blocks.get(i).clone();
					blocks.set(i, blocks.get(i + 1).clone());
					blocks.set(i + 1, tempBlock.clone());
				}
				
			}
		}
		
		for(MineBlock block : blocks)
		{
			String blockName = block.getBlock().getItemType().toString().toLowerCase().replace("_", " ");
			if(block.getBlock().getData() != 0)
			{
				String[] tempBlockName = {block.getBlock().getItemTypeId() + "", block.getBlock().getData() + ""};
				blockName = Util.parseMetadata(tempBlockName, true) + " " + blockName;
			}
			String blockWeight = null;
			if((block.getChance() * 100) < 10)
				blockWeight = " " + (block.getChance() * 100) + "%";
			else
				blockWeight = (block.getChance() * 100) + "%";
			
			if(block.getChance() != 0)
				finalList.add(" - " + blockWeight + " " + ChatColor.GREEN + blockName);
		}
		
		return finalList;
		
	}
	
	/**
	 * Returns the most common block in the mine (with the largest chance)
	 * If two or moer most common blocks have the same chances, behavior is unpredictable
	 * @param curMine Mine to check
	 * @return MineBlock
	 */
	public static MineBlock getMostCommon(Mine curMine) {
		List<MineBlock> blocks = curMine.getBlocks();
		MineBlock mostCommon = blocks.get(0);
		for(MineBlock curBlock : blocks)
		{
			if(curBlock.getChance() > mostCommon.getChance())
				mostCommon = curBlock;
		}
		return mostCommon;
	}
	
	/**
	 * Returns the MineBlock with a specified MaterialData
	 * @param curMine Mine to check the blocks of
	 * @param block Block to search for
	 * @return MineBlock if it exists, otherwise null
	 */
	public static MineBlock getBlock(Mine curMine, MaterialData block) {
		if(block == null) return null;
		
		List<MineBlock> blocks = curMine.getBlocks();
		for(MineBlock thisBlock : blocks)
			if(thisBlock.getBlock().equals(block)) return thisBlock;
		
		return null;
	}
	
	/**
	 * Returns the time period at which a certain mine is reset
	 * @param curMine Mine to check
	 * @return Time period at which the mine is reset
	 */
	public static int getResetTime(Mine curMine)
	{
		if(curMine.getParent() == null)
			return curMine.getResetPeriod();
		else
			return getResetTime(getMine(curMine.getParent()));
	}
	
	/**
	 * Returns the time in which a certain mine is reset
	 * @param curMine Mine to check
	 * @return Seconds until the next reset
	 */
	public static int getNextReset(Mine curMine)
	{
		if(curMine.getParent() == null)
			return ((int)curMine.getNextAutomaticResetTick()) * 20;
		else
			return getNextReset(getMine(curMine.getParent()));
	}
}
