package com.wolvencraft.MineReset.cmd;

import org.bukkit.ChatColor;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.MineUtils;
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
		
		if(args.length != 1 && !MineUtils.exists(args[1]))
		{
			Message.sendError("Mine '" + args[1] + "' does not exist. Use " + ChatColor.GREEN + "/mine help" + ChatColor.WHITE + " for help");
			return;
		}
		
		if(mineName == null) mineName = args[1];
		
		String displayName = Regions.getString("mines." + mineName + ".display-name");
		if(displayName.equals("")) displayName = mineName;
		
		// Reset
		String parentMine = Regions.getString("mines." + mineName + ".parent");
		if(parentMine == null)
			parentMine = mineName;
		
		boolean autoReset = Regions.getBoolean("mines." + parentMine + ".reset.auto.reset");
		int autoResetTime = Regions.getInt("mines." + parentMine + ".reset.auto.reset-every");
		String autoResetFormatted = autoResetTime / 60 + ":";
		if(autoResetTime % 60 < 10)
			autoResetFormatted = autoResetFormatted + "0" + autoResetTime % 60;
		else
			autoResetFormatted = autoResetFormatted + autoResetTime % 60;
		
		int nextResetTime = Regions.getInt("mines." + parentMine + ".reset.auto.data.next");
		String nextResetFormatted = nextResetTime / 60 + ":";
		if(nextResetTime % 60 < 10)
			nextResetFormatted = nextResetFormatted + "0" + nextResetTime % 60;
		else
			nextResetFormatted = nextResetFormatted + nextResetTime % 60;
		
		if(autoReset)
		{
			Message.sendSuccess(displayName + " resets every " + ChatColor.GOLD +  autoResetFormatted + ChatColor.WHITE + " minutes. Next reset in " + ChatColor.GOLD + nextResetFormatted + ChatColor.WHITE + " minutes.");
		}
		else
		{
			Message.sendSuccess(displayName + " has to be reset manually");
		}
	}
}
