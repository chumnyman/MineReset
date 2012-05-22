package com.wolvencraft.MineReset;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.cmd.*;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class CommandManager implements CommandExecutor
{
	private static CommandSender sender;
	private static MineReset plugin;
	private static Location[] loc = {null, null};
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
		Player player = null;
		if(!command.getName().equalsIgnoreCase("mine")) return false;
		
		if(Util.isPlayer())
		{
			player = (Player) sender;
		}
		
		if(args.length == 0)
		{
			Help.getHelp();
			return false;
		}
		else if(args[0].equalsIgnoreCase("auto") || args[0].equalsIgnoreCase("timer") || args[0].equalsIgnoreCase("tm"))
			Auto.run(args);
		else if(args[0].equalsIgnoreCase("blacklist") || args[0].equalsIgnoreCase("bl"))
			Blacklist.run(args);
		else if(args[0].equalsIgnoreCase("config") || args[0].equalsIgnoreCase("cf"))
			Config.run(args);
		else if(args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("none") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("+") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("-") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("name") || args[0].equalsIgnoreCase("silent") || args[0].equalsIgnoreCase("generator") || args[0].equalsIgnoreCase("link"))
			Edit.run(args);
		else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))
			Help.getHelp();
		else if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i"))
			Info.run(args);
		else if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("all"))
			MineList.run(args);
		else if(args[0].equalsIgnoreCase("protection") || args[0].equalsIgnoreCase("prot"))
			Protection.run(args);
		else if(args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("fill"))
			Reset.run(args, false, null);
		else if(args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("empty"))
			Reset.run(args, false, "empty");
		else if(args[0].equalsIgnoreCase("save") || args[0].equalsIgnoreCase("s"))
			Save.run(args);
		else if(args[0].equalsIgnoreCase("select") || args[0].equalsIgnoreCase("sel"))
			Select.run(args);
		else if(args[0].equalsIgnoreCase("sign") || args[0].equalsIgnoreCase("signs"))
			SignCmd.run(args);
		else if(args[0].equalsIgnoreCase("time"))
			Time.run(args);
		else if(args[0].equalsIgnoreCase("warp") || args[0].equalsIgnoreCase("tp"))
			Teleport.run(args);
		else Message.sendInvalid(args);
			
		String argString = "/mine";
        for (String arg : args) {
            argString = argString + " " + arg;
        }
		if (Util.isPlayer()) Message.debug(player.getName() + ": " + argString);
		
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
	 * @param id ID of a selection location (0 or 1)
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