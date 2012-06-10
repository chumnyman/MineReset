package com.wolvencraft.MineReset.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.MineBlock;

public class MineUtils
{
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
	 * Teleports a player to the mine specified
	 * @param player Player to be teleported
	 * @param curMine The mine to be teleported to
	 */
	public static void warpToMine(Player player, Mine curMine) {
		player.teleport(curMine.getWarp());
	}
	
	public static List<String> getSortedList(Mine curMine)
	{
		List<MineBlock> blocks = curMine.getBlocks();
		List<String> finalList = new ArrayList<String>(blocks.size());
		
		MineBlock tempBlock;
		for(int j = blocks.size(); j > 0; j--)
		{
			for(int i = 0; i < j; i++)
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
			return getResetTime(curMine.getParent());
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
			return getNextReset(curMine.getParent());
	}
}
