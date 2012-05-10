package com.wolvencraft.MineReset.config;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;

public class Configuration
{
	/**
	 * Returns configuration data from the node
	 * @param node Configuration node
	 * @return String to be returned
	 */
	public static String getString(String node)
	{
		String stringToReturn = CommandManager.getPlugin().getConfig().getString(node);
		if(!CommandManager.getPlugin().getConfig().isSet(node))
			CommandManager.getPlugin().getConfig().set(node, stringToReturn);
		return stringToReturn;
	}
	
	/**
	 * Returns configuration data from the node
	 * @param node Configuration node
	 * @return boolean to be returned
	 */
	public static boolean getBoolean(String node)
	{
		boolean booleanToReturn = CommandManager.getPlugin().getConfig().getBoolean(node);
		if(!CommandManager.getPlugin().getConfig().isSet(node))
			CommandManager.getPlugin().getConfig().set(node, booleanToReturn);
		return booleanToReturn;
	}
	
	/**
	 * Returns configuration data from the node
	 * @param node Configuration node
	 * @return int to be returned
	 */
	public static int getInt(String node)
	{
		int intToReturn = CommandManager.getPlugin().getConfig().getInt(node);
		if(!CommandManager.getPlugin().getConfig().isSet(node))
			CommandManager.getPlugin().getConfig().set(node, intToReturn);
		return intToReturn;
	}
	
	/**
	 * Returns configuration data from the node
	 * @param node Configuration node
	 * @return double to be returned
	 */
	public static double getDouble(String node)
	{
		double doubleToReturn = CommandManager.getPlugin().getConfig().getDouble(node);
		if(!CommandManager.getPlugin().getConfig().isSet(node))
			CommandManager.getPlugin().getConfig().set(node, doubleToReturn);
		return doubleToReturn;
	}
	
	/**
	 * Returns configuration data from the node
	 * @param node Configuration node
	 * @return List<String> to be returned
	 */
	public static List<String> getList(String node)
	{
		List<String> listToReturn = CommandManager.getPlugin().getConfig().getStringList(node);
		if(!CommandManager.getPlugin().getConfig().isSet(node))
			CommandManager.getPlugin().getConfig().set(node, listToReturn);
		return listToReturn;
	}
}
