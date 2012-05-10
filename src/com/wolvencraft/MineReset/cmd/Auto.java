package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;


public class Auto
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("edit"))
		{
			Message.sendDenied(args);
			return;
		}
		
		if(args.length == 1)
		{
			Help.getAuto();
			return;
		}
		else if(args.length < 2 || args.length > 4)
		{
			Message.sendInvalid(args);
			return;
		}

		String mineName = CommandManager.getMine();
		if(mineName == null)
		{
			String error = Language.getString("general.mine-not-selected");
			Message.sendError(error);
			return;
		}
		
		String baseNode = "mines." + mineName + ".reset.auto";
		if(args[1].equalsIgnoreCase("toggle"))
		{
			if(Regions.getBoolean(baseNode + ".reset"))
			{
				Message.sendSuccess("'" + mineName + "' will no longer reset automatically.");
				Regions.setBoolean(baseNode + ".reset", false);
			}
			else
			{
				Message.sendSuccess("'" + mineName + "' will now reset automatically.");
				Regions.setBoolean(baseNode + ".reset", true);
			}
			Regions.saveData();
			return;
		}
		else if(args[1].equalsIgnoreCase("time"))
		{
			if(!Util.isNumeric(args[2]))
			{
				Message.sendInvalid(args);
				return;
			}
			int time = (int)Double.parseDouble(args[2]);
			Message.sendSuccess("'" + mineName + "' will now reset every " + time + " minute(s).");
			Regions.setInt(baseNode + ".reset-time", time);
			Regions.saveData();
			return;
		}
		else if(args[1].equalsIgnoreCase("warning"))
		{
			if(args[2].equalsIgnoreCase("toggle"))
			{
				if(Regions.getBoolean(baseNode + ".warn"))
				{
					Message.sendSuccess("Players will NOT be warned before '" + mineName + "' resets");
					Regions.setBoolean(baseNode + ".warn", false);
				}
				else
				{
					Message.sendSuccess("Players WILL be warned before '" + mineName + "' resets");
					Regions.setBoolean(baseNode + ".warn", true);
				}
				Regions.saveData();
				return;
			}
			else if(args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("+"))
			{
				if(args.length != 4)
				{
					Message.sendInvalid(args);
					return;
				}
				if(!Util.isNumeric(args[3]))
				{
					Message.sendInvalid(args);
					return;
				}
				int time = (int)Double.parseDouble(args[3]);
				if(time <= 0)
				{
					Message.sendError("Time cannot be negative, dummy");
					return;
				}
				if(time > Regions.getInt(baseNode + ".reset-time"))
				{
					Message.sendError("Time cannot be set to a value greater then the reset time");
					return;
				}
				List<String> warnList = Regions.getList(baseNode + ".warn-times");
				warnList.add(time + "");
				Regions.setList(baseNode + ".warn-times", warnList);
				Regions.saveData();
				Message.sendSuccess(mineName + " will now send warnings " + time + " minute(s) before the reset");
				return;
			}
			else if(args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("-"))
			{

				if(args.length != 4)
				{
					Message.sendInvalid(args);
					return;
				}
				if(!Util.isNumeric(args[3]))
				{
					Message.sendInvalid(args);
					return;
				}
				int time = (int)Double.parseDouble(args[3]);
				List<String> warnList = Regions.getList(baseNode + ".warn-times");
				int index = warnList.indexOf(time + "");
				if(index == -1)
				{
					Message.sendError("'" + mineName + "' does not send a warning " + time + " minute(s) before the reset");
					return;
				}
				warnList.remove(index);
				Regions.setList(baseNode + ".warn-times", warnList);
				Regions.saveData();
				Message.sendSuccess(mineName + " will no longer send a warning " + time + " minute(s) before the reset");
				return;
			}
			else
			{
				Message.sendInvalid(args);
				return;
			}
		}
		else
		{
			Message.sendInvalid(args);
			return;
		}
	}
}
