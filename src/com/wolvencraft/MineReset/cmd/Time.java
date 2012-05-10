package com.wolvencraft.MineReset.cmd;

import org.bukkit.ChatColor;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Mine;
import com.wolvencraft.MineReset.util.Util;

public class Time
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("time"))
		{
			Message.sendDenied(args);
			return;
		}
		
		String mineName = null;
		if(args.length == 1)
		{
			if(CommandManager.getMine() != null)
			{
				mineName = CommandManager.getMine();
			}
			else
			{
				Help.getInfo();
				return;
			}
		}
		if(args.length > 2)
		{
			Message.sendInvalid(args);
			return;
		}
		
		if(args.length != 1 && !Mine.exists(args[1]))
		{
			Message.sendError("Mine '" + args[1] + "' does not exist. Use " + ChatColor.GREEN + "/mine help" + ChatColor.WHITE + " for help");
			return;
		}
		
		if(mineName == null) mineName = args[1];
		
		String displayName = Regions.getString("mines." + mineName + ".display-name");
		if(displayName.equals("")) displayName = mineName;
		
		// Reset
		boolean autoReset = Regions.getBoolean("mines." + mineName + ".reset.auto.reset");
		int autoResetTime = Regions.getInt("mines." + mineName + ".reset.auto.reset-time");
		int nextResetMin = Regions.getInt("mines." + mineName + ".reset.auto.data.min");
		int nextResetSec = Regions.getInt("mines." + mineName + ".reset.auto.data.sec");
		
		if(autoReset)
		{
			Message.sendSuccess(displayName + " resets every " + ChatColor.GOLD +  autoResetTime + ChatColor.WHITE + " minutes. Next reset in " + ChatColor.GOLD + nextResetMin + ChatColor.WHITE + " minutes " + ChatColor.GOLD + nextResetSec + ChatColor.WHITE + " seconds");
		}
		else
		{
			Message.sendSuccess(displayName + " has to be reset manually");
		}
	}
}
