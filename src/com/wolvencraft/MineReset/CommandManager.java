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
		
		if(args.length == 0) MineCommand.HELP.getHelp();
		for(MineCommand cmd : MineCommand.values()) {
			if(cmd.isCommand(args[0])) {
				String argString = "/mine";
		        for (String arg : args) {
		            argString = argString + " " + arg;
		        }
				ChatUtil.debug(sender.getName() + ": " + argString);
				
				sender = null;
				return cmd.run(args);
			}
		}
		
		ChatUtil.sendInvalid(MineError.INVALID, args);
		sender = null;
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