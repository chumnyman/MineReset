package com.wolvencraft.MineReset.config;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;

public class Regions
{
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return String to be returned
	 */
	public static String getString(String node)
	{
		String stringToReturn = CommandManager.getPlugin().getRegionData().getString(node);
		if(stringToReturn == null)
			stringToReturn = Configuration.getString(node);
		return stringToReturn;
	}
	
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return boolean to be returned
	 */
	public static boolean getBoolean(String node)
	{
		return CommandManager.getPlugin().getRegionData().getBoolean(node);
	}
	
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return int to be returned
	 */
	public static int getInt(String node)
	{
		Integer intToReturn = CommandManager.getPlugin().getRegionData().getInt(node);
		if(intToReturn == null)
			intToReturn = Configuration.getInt(node);
		return intToReturn.intValue();
	}
	
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return int to be returned
	 */
	public static double getDouble(String node)
	{
		Double doubleToReturn = CommandManager.getPlugin().getRegionData().getDouble(node);
		if(doubleToReturn == null)
			doubleToReturn = Configuration.getDouble(node);
		return doubleToReturn.doubleValue();
	}
	
	/**
	 * Returns region data from the node
	 * @param node Configuration node
	 * @return List<String> to be returned
	 */
	public static List<String> getList(String node)
	{
		List<String> listToReturn = CommandManager.getPlugin().getRegionData().getStringList(node);
		if(listToReturn == null)
			listToReturn = Configuration.getList(node);
		return listToReturn;
	}
	
	/**
	 * Sets region data to a designated node
	 * @param node Configuration node
	 * @param data Data to be set
	 */
	public static void setString(String node, String data)
	{
		CommandManager.getPlugin().getRegionData().set(node, data);
	}
	
	/**
	 * Sets region data to a designated node
	 * @param node Configuration node
	 * @param data Data to be set
	 */
	public static void setBoolean(String node, boolean data)
	{
		CommandManager.getPlugin().getRegionData().set(node, data);
	}
	
	/**
	 * Sets region data to a designated node
	 * @param node Configuration node
	 * @param data Data to be set
	 */
	public static void setInt(String node, int data)
	{
		CommandManager.getPlugin().getRegionData().set(node, data);
	}
	
	/**
	 * Sets region data to a designated node
	 * @param node Configuration node
	 * @param data Data to be set
	 */
	public static void setDouble(String node, double data)
	{
		CommandManager.getPlugin().getRegionData().set(node, data);
	}
	
	/**
	 * Sets region data to a designated node
	 * @param node Configuration node
	 * @param data Data to be set
	 */
	public static void setList(String node, List<String> data)
	{
		CommandManager.getPlugin().getRegionData().set(node, data);
	}
	
	/**
	 * Saves region data to a file.
	 * This should not be used too often due to possible server lag
	 */
	public static void saveData()
	{
		CommandManager.getPlugin().saveRegionData();
	}
	
	/**
	 * Checks if the node exists
	 * @param node Node to check
	 * @return Returns true if the node exists, false if it does not
	 */
	public static boolean exists(String node)
	{
		if(CommandManager.getPlugin().getRegionData().isSet(node))
			return true;
		else return false;
	}
	
	/**
	 * Removes the data from the node
	 * @param node Node to be cleared
	 */
	public static void remove(String node)
	{
		CommandManager.getPlugin().getRegionData().set(node, null);
		return;
	}
	
	public static int getResetTime(String mineName)
	{
		if(getString("mines." + mineName + ".link") == null)
			return getInt("mines." + mineName + ".reset.data.next");
		else
			return getResetTime(getString("mines." + mineName + ".link"));
	}
}
