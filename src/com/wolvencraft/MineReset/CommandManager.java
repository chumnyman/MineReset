/*
 * CommandManager.java
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

package com.wolvencraft.MineReset;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineError;

public class CommandManager implements CommandExecutor
{
    private static CommandSender sender;
    private static MineReset plugin;
    private static Location[] loc = {null, null};
    private static Mine curMine = null;
    
    public CommandManager(MineReset plugin) {
        CommandManager.plugin = plugin;
        plugin.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandManager.sender = sender;
        if(!command.getName().equalsIgnoreCase("mine")) return false;
        
        if(args.length == 0) {
            MineCommand.HELP.getHelp();
            CommandManager.sender = null;
            return true;
        }
        for(MineCommand cmd : MineCommand.values()) {
            if(cmd.isCommand(args[0])) {
                String argString = "/mine";
                for (String arg : args) {
                    argString = argString + " " + arg;
                }
                ChatUtil.debug(sender.getName() + ": " + argString);
                
                boolean result = cmd.run(args);
                CommandManager.sender = null;
                return result;
            }
        }
        
        ChatUtil.sendInvalid(MineError.INVALID, args);
        CommandManager.sender = null;
        return false;
    }
    
    /**
     * Returns the command sender
     * @return CommandSender
     */
    public static CommandSender getSender() {
        return sender;
    }
    
    /**
     * Returns the plugin
     * @return MineReset
     */
    public static MineReset getPlugin() {
        return plugin;
    }
    
    /**
     * Returns the location selected with either a command or a wand
     * @return Location[] if a selection was made, null if it was not
     */
    public static Location[] getLocation() {
        return loc;
    }
    
    /**
     * Sets the location selected with either a command or a wand
     * @param newLoc New selection location
     * @param id ID of a selection location (0 or 1)
     */
    public static void setLocation(Location newLoc, int id) {
        loc[id] = newLoc;
        return;
    }
    
    public static void resetLocation() {
        loc[0] = null;
        loc[1] = null;
    }
    
    /**
     * Returns the name of a mine that is being edited
     * @return String is a mine is selected, null if it is not
     */
    public static Mine getMine() {
        return curMine;
    }
    
    /**
     * Sets the name of a current mine to a value specified
     * @param newMine The newly selected mine name
     */
    public static void setMine(Mine newMine) {
        curMine = newMine;
        return;
    }
}