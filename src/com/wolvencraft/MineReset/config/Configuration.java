/*
 * Configuration.java
 * 
 * MineReset
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

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
    
    /**
     * Checks if the node exists
     * @param node Node to check
     * @return Returns true if the node exists, false if it does not
     */
    public static boolean exists(String node)
    {
        if(CommandManager.getPlugin().getConfig().isSet(node))
            return true;
        else return false;
    }
    
    /**
     * Removes the data from the node
     * @param node Node to be cleared
     */
    public static void remove(String node)
    {
        CommandManager.getPlugin().getConfig().set(node, null);
        return;
    }
}
