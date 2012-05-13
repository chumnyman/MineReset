package com.wolvencraft.MineReset.config;

import java.util.List;

import org.bukkit.block.Block;

import com.wolvencraft.MineReset.CommandManager;

public class Signs
{
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return String to be returned
	 */
	public static String getString(String node)
	{
		return CommandManager.getPlugin().getSignData().getString(node);
	}
	
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return boolean to be returned
	 */
	public static boolean getBoolean(String node)
	{
		return CommandManager.getPlugin().getSignData().getBoolean(node);
	}
	
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return int to be returned
	 */
	public static int getInt(String node)
	{
		return CommandManager.getPlugin().getSignData().getInt(node);
	}
	
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return int to be returned
	 */
	public static double getDouble(String node)
	{
		return CommandManager.getPlugin().getSignData().getDouble(node);
	}
	
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return List<String> to be returned
	 */
	public static List<String> getList(String node)
	{
		return CommandManager.getPlugin().getSignData().getStringList(node);
	}
	
	/**
	 * Sets region data to a designated node
	 * @param node Configuration node
	 * @param data Data to be set
	 */
	public static void setString(String node, String data)
	{
		CommandManager.getPlugin().getSignData().set(node, data);
	}
	
	/**
	 * Sets region data to a designated node
	 * @param node Configuration node
	 * @param data Data to be set
	 */
	public static void setBoolean(String node, boolean data)
	{
		CommandManager.getPlugin().getSignData().set(node, data);
	}
	
	/**
	 * Sets region data to a designated node
	 * @param node Configuration node
	 * @param data Data to be set
	 */
	public static void setInt(String node, int data)
	{
		CommandManager.getPlugin().getSignData().set(node, data);
	}
	
	/**
	 * Sets region data to a designated node
	 * @param node Configuration node
	 * @param data Data to be set
	 */
	public static void setDouble(String node, double data)
	{
		CommandManager.getPlugin().getSignData().set(node, data);
	}
	
	/**
	 * Sets region data to a designated node
	 * @param node Configuration node
	 * @param data Data to be set
	 */
	public static void setList(String node, List<String> data)
	{
		CommandManager.getPlugin().getSignData().set(node, data);
	}

	/**
	 * Removes the node specified
	 * @param node
	 */
	public static void remove(String node)
	{
		CommandManager.getPlugin().getSignData().set(node, null);
	}
	
	/**
	 * Saves region data to a file.
	 * This should not be used too often due to possible server lag
	 */
	public static void saveData()
	{
		CommandManager.getPlugin().saveSignData();
	}
	
	/**
	 * Checks if a sign exists
	 * @param String Name of the mine
	 * @param String Name of a sign
	 * @return true if exists, false if it does not
	 */
	public static boolean signExists(Block b)
	{
		List<String> signList = Signs.getList("data.list-of-signs");
		for(String signName : signList)
		{
			if(Signs.getString("signs." + signName + ".world").equals(b.getLocation().getWorld().getName()) &&
				Signs.getInt("signs." + signName + ".x") == b.getLocation().getBlockX() &&
				Signs.getInt("signs." + signName + ".y") == b.getLocation().getBlockY() &&
				Signs.getInt("signs." + signName + ".z") == b.getLocation().getBlockZ())
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Determines the id of a sign for a particular mine
	 * @param mineName Mine to check against
	 * @param b Block to check
	 * @return Id if a sign is defined, -1 if not
	 */
	public static String getId(Block b)
	{
		List<String> signList = Signs.getList("data.list-of-signs");
		for(String signName : signList)
		{
			if(Signs.getString("signs." + signName + ".world").equals(b.getLocation().getWorld().getName()) &&
				Signs.getInt("signs." + signName + ".x") == b.getLocation().getBlockX() &&
				Signs.getInt("signs." + signName + ".y") == b.getLocation().getBlockY() &&
				Signs.getInt("signs." + signName + ".z") == b.getLocation().getBlockZ())
			{
				return signName;
			}
		}
		return null;
	}
}
