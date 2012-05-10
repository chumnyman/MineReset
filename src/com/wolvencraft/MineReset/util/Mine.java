package com.wolvencraft.MineReset.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.config.Regions;

public class Mine
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
	public static boolean playerInTheMine(Player player, String mineName)
	{
		int[] x = {Regions.getInt("mines." + mineName + ".coordinates.pos0.x"), Regions.getInt("mines." + mineName + ".coordinates.pos1.x")};
		int[] y = {Regions.getInt("mines." + mineName + ".coordinates.pos0.y"), Regions.getInt("mines." + mineName + ".coordinates.pos1.y")};
		int[] z = {Regions.getInt("mines." + mineName + ".coordinates.pos0.z"), Regions.getInt("mines." + mineName + ".coordinates.pos1.z")};
		Location loc = player.getLocation();
		if((loc.getBlockX() > x[0] && loc.getBlockX() < x[1])
				&& (loc.getBlockY() > y[0] && loc.getBlockY() < y[1])
				&& (loc.getBlockZ() > z[0] && loc.getBlockZ() < z[1]))
				{
					return true;
				}
				else return false;
	}
	
	/**
	 * Teleports a player to the mine specified
	 * @param player Player to be teleported
	 * @param mienName The name of the mine
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
			String blockName = Material.getMaterial(Integer.parseInt(itemList.get(i))).toString().toLowerCase().replace("_", " ");
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
}
