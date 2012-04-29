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
		String stringToReturn = CommandManager.getPlugin().getSignData().getString(node);
		return stringToReturn;
	}
	
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return boolean to be returned
	 */
	public static boolean getBoolean(String node)
	{
		boolean booleanToReturn = CommandManager.getPlugin().getSignData().getBoolean(node);
		return booleanToReturn;
	}
	
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return int to be returned
	 */
	public static int getInt(String node)
	{
		int intToReturn = CommandManager.getPlugin().getSignData().getInt(node);
		return intToReturn;
	}
	
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return int to be returned
	 */
	public static double getDouble(String node)
	{
		double intToReturn = CommandManager.getPlugin().getSignData().getDouble(node);
		return intToReturn;
	}
	
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return List<String> to be returned
	 */
	public static List<String> getList(String node)
	{
		List<String> listToReturn = CommandManager.getPlugin().getSignData().getStringList(node);
		return listToReturn;
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
		List<String> mineList = Regions.getList("data.list-of-mines");
		for(String mineName : mineList)
		{
			int num = Signs.getInt(mineName + ".num");
			for(int i = 0; i < num; i++)
			{
				if(b.getWorld().getName().equals(getString(mineName + "." + i + ".world")))
				{
					if(b.getLocation().getBlockX() == getInt(mineName + "." + i + ".x") ||
							b.getLocation().getBlockY() == getInt(mineName + "." + i + ".y") ||
							b.getLocation().getBlockZ() == getInt(mineName + "." + i + ".z"))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static int getId(String mineName, Block b)
	{
		int num = Signs.getInt(mineName + ".num");
		for(int i = 0; i < num; i++)
		{
			if(b.getWorld().getName().equals(getString(mineName + "." + i + ".world")))
			{
				if(b.getLocation().getBlockX() == getInt(mineName + "." + i + ".x") ||
						b.getLocation().getBlockY() == getInt(mineName + "." + i + ".y") ||
						b.getLocation().getBlockZ() == getInt(mineName + "." + i + ".z"))
				{
					return i;
				}
			}
		}
		return -1;
	}
}
