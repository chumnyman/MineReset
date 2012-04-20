package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.CommandManager;


public class Auto
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("edit"))
		{
			Util.sendDenied(args);
			return;
		}
		
		if(args.length == 1)
		{
			Help.getBlacklist();
		}
		else if(args.length < 2 || args.length > 3)
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
		
		String baseNode = "mines." + mineName + ".reset.auto";
		if(args[1].equalsIgnoreCase("toggle"))
		{
			if(Util.getRegionBoolean(baseNode + ".reset"))
			{
				Util.sendSuccess("Automatic resets turned OFF for mine '" + mineName + "'");
				Util.setRegionBoolean(baseNode + ".reset", false);
			}
			else
			{
				Util.sendSuccess("Automatic resets turned ON for mine '" + mineName + "'");
				Util.setRegionBoolean(baseNode + ".reset", true);
			}
			return;
		}
		else if(args[1].equalsIgnoreCase("time"))
		{
			if(!Util.isNumeric(args[2]))
			{
				Util.sendInvalid(args);
				return;
			}
			int time = (int)Double.parseDouble(args[2]);
			if(time < 5)
			{
				Util.sendError("Time cannot be set to less then 5 minutes");
				return;
			}
			if(time < Util.getRegionInt(baseNode + ".warn-time"))
			{
				Util.setRegionInt(baseNode + ".warn-time", time);
			}
			Util.setRegionInt(baseNode + ".reset-time", time);
			return;
		}
		else if(args[1].equalsIgnoreCase("warning"))
		{
			if(args[2].equalsIgnoreCase("toggle"))
			{
				if(Util.getRegionBoolean(baseNode + ".warn"))
				{
					Util.sendSuccess("Reset warnings turned OFF for mine '" + mineName + "'");
					Util.setRegionBoolean(baseNode + ".reset", false);
				}
				else
				{
					Util.sendSuccess("Reset warnings turned ON for mine '" + mineName + "'");
					Util.setRegionBoolean(baseNode + ".reset", true);
				}
				return;
			}
			else
			{
				if(!Util.isNumeric(args[2]))
				{
					Util.sendInvalid(args);
					return;
				}
				int time = (int)Double.parseDouble(args[2]);
				if(time < 5)
				{
					Util.sendError("Time cannot be set to less then 5 minutes");
					return;
				}
				if(time > Util.getRegionInt(baseNode + ".reset-time"))
				{
					Util.sendError("Time cannot be set to a value greater then the reset time");
					return;
				}
				Util.setRegionInt(baseNode + ".warn-time", time);
				return;
			}
		}
		else
		{
			Util.sendInvalid(args);
			return;
		}
	}
}
