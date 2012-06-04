package com.wolvencraft.MineReset.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.config.Regions;

public class MineUtils
{
	/**
	 * Checks if the mine exists
	 * @param name Name of the mine being checked
	 * @return True if the mine exists, False if it does not
	 */
	public static boolean exists(String name)
	{
		List<String> mineList = Regions.getList("data.list-of-mines");
		if(mineList.indexOf(name) == -1) return false;
		else return true;
	}
	
	
	/**
	 * Determines if a specific player is in the mine
	 * @param player Player
	 * @param mineName Name of the mine
	 * @return true if a player is in the mine, false if he is not
	 */
	public static boolean playerInTheMine(Player player, String mineName) {
        return isLocationInMine(player.getLocation(), mineName);
	}

    public static boolean isBlockInMine(Block block, String mine) {
        return isLocationInMine(block.getLocation(), mine);
    }

    /**
     * Slightly more abstract method to determine if a mine contains a location or not
     * @param location location object
     * @param mine mine name
     * @return true if location is in mine
     */
    public static boolean isLocationInMine(Location location, String mine) {
        int[] x = {Regions.getInt("mines." + mine + ".coordinates.pos0.x"), Regions.getInt("mines." + mine + ".coordinates.pos1.x")};
        int[] y = {Regions.getInt("mines." + mine + ".coordinates.pos0.y"), Regions.getInt("mines." + mine + ".coordinates.pos1.y")};
        int[] z = {Regions.getInt("mines." + mine + ".coordinates.pos0.z"), Regions.getInt("mines." + mine + ".coordinates.pos1.z")};

        return (location.getX() >= x[0] && location.getX() <= x[1])     //if x matches
                && (location.getY() >= y[0] && location.getY() <= y[1]) //and y
                && (location.getZ() >= z[0] && location.getZ() <= z[1]);//and z
    }
	/**
	 * Teleports a player to the mine specified
	 * @param player Player to be teleported
	 * @param mineName The name of the mine
	 */
	public static void warpToMine(Player player, String mineName)
	{
		String newLocWorld = Regions.getString("mines." + mineName + ".coordinates.world");
		double[] coords = {
				Regions.getDouble("mines." + mineName + ".coordinates.pos2.x"),
				Regions.getDouble("mines." + mineName + ".coordinates.pos2.y"),
				Regions.getDouble("mines." + mineName + ".coordinates.pos2.z"),
				Regions.getDouble("mines." + mineName + ".coordinates.pos2.yaw"),
				Regions.getDouble("mines." + mineName + ".coordinates.pos2.pitch")
		};
		Location newLoc = new Location(Bukkit.getServer().getWorld(newLocWorld), coords[0], coords[1], coords[2], (float)coords[3], (float)coords[4]);
		
		player.teleport(newLoc);
	}
	
	public static List<String> getSortedList(String mineName)
	{
		List<String> itemList = Regions.getList("mines." + mineName + ".materials.blocks");
		List<String> weightList = Regions.getList("mines." + mineName + ".materials.weights");
		List<String> finalList = new ArrayList<String>(itemList.size());
		
		String tempItem = null, tempWeight = null;
		for(int j = weightList.size() - 1; j > 0 ; j--)
		{
			for(int i = 0; i < j; i++)
			{
				if(Double.parseDouble(weightList.get(i + 1)) > Double.parseDouble(weightList.get(i)))
				{
					tempItem = itemList.get(i);
					tempWeight = weightList.get(i);
	
					itemList.set(i, itemList.get(i + 1));
					weightList.set(i, weightList.get(i + 1));
	
					itemList.set(i + 1, tempItem);
					weightList.set(i + 1, tempWeight);
				}
				
			}
		}
		
		for(int i = 0; i < itemList.size(); i++)
		{
			String blockName = Material.getMaterial(Integer.parseInt(itemList.get(i).split(":")[0])).toString().toLowerCase().replace("_", " ");
			if(!itemList.get(i).split(":")[1].equalsIgnoreCase("0"))
			{
				blockName = Util.parseMetadata(itemList.get(i).split(":"), true) + " " + blockName;
			}
			String blockWeight = null;
			if(Double.parseDouble(weightList.get(i)) < 10)
				blockWeight = " " + weightList.get(i) + "%";
			else
				blockWeight = weightList.get(i) + "%";
			
			if(Double.parseDouble(weightList.get(i)) != 0)
				finalList.add(" - " + blockWeight + " " + ChatColor.GREEN + blockName);
		}
		
		return finalList;
		
	}
	
	
	/**
	 * Returns the time period at which a certain mine is reset
	 * @param mineName Mine to check
	 * @return Time period at which the mine is reset
	 */
	public static int getResetTime(String mineName)
	{
		if(Regions.getString("mines." + mineName + ".parent") == null)
			return Regions.getInt("mines." + mineName + ".reset.auto.reset-every");
		else
			return getNextReset(Regions.getString("mines." + mineName + ".parent"));
	}
	
	/**
	 * Returns the time in which a certain mine is reset
	 * @param mineName Mine to check
	 * @return Seconds until the next reset
	 */
	public static int getNextReset(String mineName)
	{
		if(Regions.getString("mines." + mineName + ".parent") == null)
			return Regions.getInt("mines." + mineName + ".reset.auto.data.next");
		else
			return getNextReset(Regions.getString("mines." + mineName + ".parent"));
	}
}
