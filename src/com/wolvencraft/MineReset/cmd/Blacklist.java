package com.wolvencraft.MineReset.cmd;

import java.util.List;

public class Blacklist
{
	public static void run(String[] args)
	{
		if(args.length == 1)
		{
			Help.getBlacklist();
		}
		else if(args.length > 4 || args.length == 2)
		{
			Util.sendInvalid(args);
			return;
		}
		
		String mineName = "";
		if(!Util.mineExists(args[1]))
		{
			Util.sendError("Mine " + args[1] + " does not exist");
			return;
		}
		else mineName = args[1];
		
		if(args[2].equalsIgnoreCase("toggle"))
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
		else if(args[2].equalsIgnoreCase("whitelist"))
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
		else if(args[2].equalsIgnoreCase("add"))
		{
			int blockID = Util.getBlockId(args[3]);
			if(blockID == -1)
			{
				Util.sendError("Block '"+ args[3] + "' does not exist");
				return;
			}
			
			List<String> blacklist = Util.getRegionList("mines." + mineName + ".blacklist.blocks");
			blacklist.add("" + blockID);
			Util.setRegionList("mines." + mineName + ".blacklist.blocks", blacklist);

			Util.sendSuccess("Block " + args[2] + " is now in the blacklist");
			return;
		}
		else if(args[2].equalsIgnoreCase("remove"))
		{
			int blockID = Util.getBlockId(args[3]);
			if(blockID == -1)
			{
				Util.sendError("Block '"+ args[3] + "' does not exist");
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
