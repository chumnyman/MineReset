package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;

import com.wolvencraft.MineReset.CommandManager;

public class Blacklist
{
	public static void run(String[] args)
	{
		if(args.length == 1)
		{
			Help.getBlacklist();
			return;
		}
		else if(args.length > 3)
		{
			Util.sendInvalid(args);
			return;
		}
		
		String mineName = CommandManager.getMine();
		if(mineName == null)
		{
			Util.sendError("Select a mine first with /mine edit <name>");
			return;
		}
		
		if(args[1].equalsIgnoreCase("toggle"))
		{
			if(Util.getRegionBoolean("mines." + mineName + ".blacklist.enabled"))
			{
				Util.setRegionBoolean("mines." + mineName + ".blacklist.enabled", false);
				Util.sendSuccess("Blacklist turned OFF for mine '" + mineName + "'");
			}
			else
			{
				Util.setRegionBoolean("mines." + mineName + ".blacklist.enabled", true);
				Util.sendSuccess("Blacklist turned ON for mine '" + mineName + "'");
			}
			return;
		}
		else if(args[1].equalsIgnoreCase("whitelist"))
		{
			if(Util.getConfigBoolean("mines." + mineName + ".blacklist.whitelist"))
			{
				Util.setRegionBoolean("mines." + mineName + ".blacklist.whitelist", false);
				Util.sendSuccess("Blacklist is no longer treated as a whitelist for mine '" + mineName);
			}
			else
			{
				Util.setRegionBoolean("mines." + mineName + ".blacklist.whitelist", true);
				Util.sendSuccess("Blacklist is now treated as a whitelist for mine '" + mineName + "'");
			}
			return;
		}
		else if(args[1].equalsIgnoreCase("add"))
		{
			int blockId = Util.getBlockId(args[2]);
			if(blockId == -1)
			{
				Util.sendError("Block '"+ args[2] + "' does not exist");
				return;
			}
			
			List<String> blacklist = Util.getRegionList("mines." + mineName + ".blacklist.blocks");
			blacklist.add(blockId + "");
			Util.setRegionList("mines." + mineName + ".blacklist.blocks", blacklist);

			Util.sendSuccess("Block " + ChatColor.GREEN + args[2] + ChatColor.WHITE + " is now in the blacklist");
			return;
		}
		else if(args[1].equalsIgnoreCase("remove"))
		{
			int blockId = Util.getBlockId(args[2]);
			if(blockId == -1)
			{
				Util.sendError("Block '" + ChatColor.GREEN + args[2] + ChatColor.WHITE + "' does not exist");
				return;
			}
			
			List<String> blacklist = Util.getRegionList("mines." + mineName + ".blacklist.blocks");
			
			int index = blacklist.indexOf(blockId);
			if(index == -1)
			{
				Util.sendError("Block " + args[2] + " is not in the blacklist");
				return;
			}
			blacklist.remove(index);
			Util.setRegionList("mines." + mineName + ".blacklist.blocks", blacklist);
			Util.sendSuccess("Block " + args[2] + " has been removed from the blacklist");
			return;
		}
		else
		{
			Util.sendInvalid(args);
			return;
		}
	}
}
