package com.wolvencraft.MineReset.cmd;

import java.util.List;

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
			Help.getAuto();
			return;
		}
		else if(args.length < 2 || args.length > 4)
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
			Util.saveRegionData();
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
			Util.sendSuccess("The reset time for mine '" + mineName + "' has been set to " + time);
			Util.setRegionInt(baseNode + ".reset-time", time);
			Util.saveRegionData();
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
				Util.saveRegionData();
				return;
			}
			else if(args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("+"))
			{
				if(args.length != 4)
				{
					Util.sendInvalid(args);
					return;
				}
				if(!Util.isNumeric(args[3]))
				{
					Util.sendInvalid(args);
					return;
				}
				int time = (int)Double.parseDouble(args[3]);
				if(time <= 0)
				{
					Util.sendError("Time cannot be negative, dummy");
					return;
				}
				if(time > Util.getRegionInt(baseNode + ".reset-time"))
				{
					Util.sendError("Time cannot be set to a value greater then the reset time");
					return;
				}
				List<String> warnList = Util.getRegionList(baseNode + ".warn-times");
				warnList.add(time + "");
				Util.setRegionList(baseNode + ".warn-times", warnList);
				Util.saveRegionData();
				Util.sendSuccess(mineName + " will now send warnings " + time + " minute(s) before the reset");
				return;
			}
			else if(args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("-"))
			{

				if(args.length != 4)
				{
					Util.sendInvalid(args);
					return;
				}
				if(!Util.isNumeric(args[3]))
				{
					Util.sendInvalid(args);
					return;
				}
				int time = (int)Double.parseDouble(args[3]);
				List<String> warnList = Util.getRegionList(baseNode + ".warn-times");
				int index = warnList.indexOf(time + "");
				if(index == -1)
				{
					Util.sendError("The mine " + mineName + " does not send a warning at " + time);
					return;
				}
				warnList.remove(index);
				Util.setRegionList(baseNode + ".warn-times", warnList);
				Util.saveRegionData();
				Util.sendSuccess(mineName + " will no longer send warnings " + time + " minute(s) before the reset");
				return;
			}
			else
			{
				Util.sendInvalid(args);
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
