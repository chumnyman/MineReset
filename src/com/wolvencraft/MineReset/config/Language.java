/*
 * Language.java
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

    /**
     * Replace a translated string's parameter values with real content.
     *
     * Example:
     * Language file has string "%0 will reset in %1 minutes!" under node "test".
     * getMessage("test", "Mine 0", "5") returns "Mine 0 will reset in 5 minutes!"
     * @param node
     * @param replacements
     * @return
     */
    public static String getMessage(String node, String... replacements) {
        String parsed = getString(node);
        for (int i = 0; i < replacements.length; i++) {
            parsed = parsed.replaceAll("%" + i, replacements[i]);
        }
        return parsed;
    }
}
