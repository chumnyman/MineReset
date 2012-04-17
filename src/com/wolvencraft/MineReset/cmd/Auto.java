package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.CommandManager;


public class Auto
{
	public static void run(String[] args)
	{
		if(args.length == 1)
		{
			Help.getBlacklist();
		}
		else if(args.length != 4)
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
			return;
		}
		else if(args[1].equalsIgnoreCase("time"))
		{
			return;
		}
		else if(args[1].equalsIgnoreCase("warning"))
		{
			return;
		}
		else
		{
			Util.sendInvalid(args);
			return;
		}
	}
}
