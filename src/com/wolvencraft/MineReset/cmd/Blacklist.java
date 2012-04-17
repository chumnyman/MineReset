package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;

public class Blacklist
{
	public static void run(String[] args)
	{
		if(args.length == 1)
		{
			Help.getBlacklist();
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
				Util.sendSuccess("Blacklist turned OFF for mine " + mineName);
			}
			else
			{
				Util.setRegionBoolean("mines." + mineName + ".blacklist.enabled", true);
				Util.sendSuccess("Blacklist turned ON for mine " + mineName);
			}
			return;
		}
		else if(args[1].equalsIgnoreCase("whitelist"))
		{
			if(Util.getConfigBoolean("mines." + mineName + ".blacklist.whitelist"))
			{
				Util.setRegionBoolean("mines." + mineName + ".blacklist.whitelist", false);
				Util.sendSuccess("Blacklist is no longer treated as a whitelist for mine " + mineName);
			}
			else
			{
				Util.setRegionBoolean("mines." + mineName + ".blacklist.whitelist", true);
				Util.sendSuccess("Blacklist is now treated as a whitelist for mine " + mineName);
			}
			return;
		}
		else if(args[1].equalsIgnoreCase("add"))
		{
			int blockID = Util.getBlockId(args[2]);
			if(blockID == -1)
			{
				Util.sendError("Block '"+ args[2] + "' does not exist");
				return;
			}
			
			List<String> blacklist = Util.getRegionList("mines." + mineName + ".blacklist.blocks");
			blacklist.add("" + blockID);
			Util.setRegionList("mines." + mineName + ".blacklist.blocks", blacklist);

			Util.sendSuccess("Block " + args[1] + " is now in the blacklist");
			return;
		}
		else if(args[1].equalsIgnoreCase("remove"))
		{
			int blockID = Util.getBlockId(args[2]);
			if(blockID == -1)
			{
				Util.sendError("Block '"+ args[2] + "' does not exist");
				return;
			}
			
			List<String> blacklist = Util.getRegionList("mines." + mineName + ".blacklist.blocks");
			
			int index = blacklist.indexOf(blockID);
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
