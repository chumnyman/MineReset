package com.wolvencraft.MineReset;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.wolvencraft.MineReset.cmd.*;

public class CommandManager implements CommandExecutor
{
	private static CommandSender sender;
	private static MineReset plugin;
	private static Location[] loc = null;
	private static String curMine = null;
	
	public CommandManager(MineReset plugin)
	{
		CommandManager.plugin = plugin;
		plugin.getLogger();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		CommandManager.sender = sender;
		
		if(!command.getName().equalsIgnoreCase("mine")) return false;
		
		if(args.length == 0)
			Help.getHelp();
		else if(args[0].equalsIgnoreCase("auto"))
			Util.sendError("This command does not yet exist");
		else if(args[0].equalsIgnoreCase("blacklist"))
			Blacklist.run(args);
		else if(args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("cooldown") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete"))
			Edit.run(args);
		if(args[0].equalsIgnoreCase("help"))
			Help.getHelp();
		if(args[0].equalsIgnoreCase("info"))
			Info.run(args);
		else if(args[0].equalsIgnoreCase("list"))
			Util.sendError("This command does not yet exist");
		else if(args[0].equalsIgnoreCase("protection") || args[0].equalsIgnoreCase("prot"))
			Protection.run(args);
		else if(args[0].equalsIgnoreCase("reset"))
			Reset.run(args);
		else if(args[0].equalsIgnoreCase("save"))
			Save.run(args);
		else if(args[0].equalsIgnoreCase("select") || args[0].equalsIgnoreCase("sel"))
			Select.run(args);
		else if(args[0].equalsIgnoreCase("warp"))
			Util.sendError("This command does not yet exist");
		else Util.sendInvalid(args);
			
		return true;
	}
	
	/**
	 * Returns the command sender
	 * @return CommandSender
	 */
	public static CommandSender getSender()
	{
		return sender;
	}
	
	/**
	 * Returns the plugin
	 * @return MineReset
	 */
	public static MineReset getPlugin()
	{
		return plugin;
	}
	
	/**
	 * Returns the location selected with either a command or a wand
	 * @return Location[] if a selection was made, null if it was not
	 */
	public static Location[] getLocation()
	{
		return loc;
	}
	
	/**
	 * Sets the location selected with either a command or a wand
	 * @param newLoc New selection location
	 */
	public static void setLocation(Location newLoc, int id)
	{
		loc[id] = newLoc;
		return;
	}
	
	/**
	 * Returns the name of a mine that is being edited
	 * @return String is a mine is selected, null if it is not
	 */
	public static String getMine()
	{
		return curMine;
	}
	
	/**
	 * Sets the name of a current mine to a value specified
	 * @param newMine The newly selected mine name
	 */
	public static void setMine(String newMine)
	{
		curMine = newMine;
		return;
	}
}