package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.mine.Mine;
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
		
		if(args.length < 2 || args.length > 4)
		{
			Message.sendInvalid(args);
			return;
		}

		Mine curMine = CommandManager.getMine();
		if(curMine == null)
		{
			Message.sendError(Language.getString("general.mine-not-selected"));
			return;
		}
		
		if(args[1].equalsIgnoreCase("toggle"))
		{
			if(curMine.)
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
			int time = Util.parseTime(args[2]);
			if(time <= 0)
			{
				Message.sendError("Invalid time provided");
				return;
			}
			Message.sendSuccess("'" + mineName + "' will now reset every " + time / 60 + " minute(s) " + time % 60 + " second(s)");
			Regions.setInt(baseNode + ".reset-every", time);
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
				int time = Util.parseTime(args[3]);
				if(time <= 0)
				{
					Message.sendError("Invalid time provided");
					return;
				}
				if(time > Regions.getInt(baseNode + ".reset-every"))
				{
					Message.sendError("Time cannot be set to a value greater then the reset time");
					return;
				}
				List<String> warnList = Regions.getList(baseNode + ".warn-times");
				warnList.add(time + "");
				Regions.setList(baseNode + ".warn-times", warnList);
				Regions.saveData();
				Message.sendSuccess(mineName + " will now send warnings " + time / 60 + " minute(s) " + time % 60 + " second(s) before the reset");
				return;
			}
			else if(args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("-"))
			{

				if(args.length != 4)
				{
					Message.sendInvalid(args);
					return;
				}
				int time = Util.parseTime(args[3]);
				if(time <= 0)
				{
					Message.sendError("Invalid time provided");
					return;
				}
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
