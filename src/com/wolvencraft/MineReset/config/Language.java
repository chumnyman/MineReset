package com.wolvencraft.MineReset.config;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;

public class Language
{
	/**
	 * Returns configuration data from the node
	 * @param node Configuration node
	 * @return String to be returned
	 */
	public static String getString(String node)
	{
		String stringToReturn = CommandManager.getPlugin().getLanguageData().getString(node);
		if(!CommandManager.getPlugin().getLanguageData().isSet(node))
			CommandManager.getPlugin().getLanguageData().set(node, stringToReturn);
		return stringToReturn;
	}
	
	/**
	 * Returns configuration data from the node
	 * @param node Configuration node
	 * @return boolean to be returned
	 */
	public static boolean getBoolean(String node)
	{
		boolean booleanToReturn = CommandManager.getPlugin().getLanguageData().getBoolean(node);
		if(!CommandManager.getPlugin().getLanguageData().isSet(node))
			CommandManager.getPlugin().getLanguageData().set(node, booleanToReturn);
		return booleanToReturn;
	}
	
	/**
	 * Returns configuration data from the node
	 * @param node Configuration node
	 * @return int to be returned
	 */
	public static int getInt(String node)
	{
		int intToReturn = CommandManager.getPlugin().getLanguageData().getInt(node);
		if(!CommandManager.getPlugin().getLanguageData().isSet(node))
			CommandManager.getPlugin().getLanguageData().set(node, intToReturn);
		return intToReturn;
	}
	
	/**
	 * Returns configuration data from the node
	 * @param node Configuration node
	 * @return double to be returned
	 */
	public static double getDouble(String node)
	{
		double doubleToReturn = CommandManager.getPlugin().getLanguageData().getDouble(node);
		if(!CommandManager.getPlugin().getLanguageData().isSet(node))
			CommandManager.getPlugin().getLanguageData().set(node, doubleToReturn);
		return doubleToReturn;
	}
	
	/**
	 * Returns configuration data from the node
	 * @param node Configuration node
	 * @return List<String> to be returned
	 */
	public static List<String> getList(String node)
	{
		List<String> listToReturn = CommandManager.getPlugin().getLanguageData().getStringList(node);
		if(!CommandManager.getPlugin().getLanguageData().isSet(node))
			CommandManager.getPlugin().getLanguageData().set(node, listToReturn);
		return listToReturn;
	}
}
