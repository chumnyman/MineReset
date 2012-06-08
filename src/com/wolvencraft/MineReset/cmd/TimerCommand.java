package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;


public class TimerCommand {
	
	public static void run(String[] args) {
		if(!Util.senderHasPermission("edit")) {
			Message.sendDenied(args);
			return;
		}
		
		if(args.length == 1) {
			HelpCommand.getAuto();
			return;
		}
		
		if(args.length < 2 || args.length > 4) {
			Message.sendInvalid(args);
			return;
		}

		Mine curMine = CommandManager.getMine();
		if(curMine == null) {
			Message.sendError(Language.getString("general.mine-not-selected"));
			return;
		}
		
		if(args[1].equalsIgnoreCase("toggle")) {
			if(args.length != 1) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			if(curMine.getAutomatic()) {
				curMine.setAutomatic(false);
				Message.sendSuccess("'" + curMine.getName() + "' will no longer reset automatically.");
			}
			else {
				curMine.setAutomatic(true);
				Message.sendSuccess("'" + curMine.getName() + "' will now reset automatically.");
			}
			return;
		}
		else if(args[1].equalsIgnoreCase("time")) {
			if(args.length != 2) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			int time = Util.parseTime(args[2]);
			if(time <= 0) {
				Message.sendError("Invalid time provided");
				return;
			}
			curMine.setResetPeriod(time);
			Message.sendSuccess("'" + curMine.getName() + "' will now reset every " + time / 60 + " minute(s) " + time % 60 + " second(s)");
			return;
		}
		else if(args[1].equalsIgnoreCase("warning")) {
			if(args.length < 3) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			if(args[2].equalsIgnoreCase("toggle")) {
				if(args.length != 3) {
					Message.sendError("Invalid parameters. Check your argument count!");
					return;
				}
				
				if(curMine.getWarned())
				{
					curMine.setWarned(false);
					Message.sendSuccess("Players will no longer be warned before '" + curMine.getName() + "' resets");
				}
				else
				{
					curMine.setWarned(true);
					Message.sendSuccess("Players will now be warned before '" + curMine.getName() + "' resets");
				}
				return;
			}
			else if(args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("+")) {
				if(args.length != 4) {
					Message.sendError("Invalid parameters. Check your argument count!");
					return;
				}
				
				int time = Util.parseTime(args[3]);
				if(time <= 0) {
					Message.sendError("Invalid time provided");
					return;
				}
				if(time > curMine.getResetPeriod()) {
					Message.sendError("Time cannot be set to a value greater then the reset time");
					return;
				}
				
				List<Integer> warnList = curMine.getWarningTimes();
				warnList.add(time);
				curMine.setWarningTimes(warnList);
				Message.sendSuccess(curMine.getName() + " will now send warnings " + time / 60 + " minute(s) " + time % 60 + " second(s) before the reset");
				return;
			}
			else if(args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("-")) {
				if(args.length != 4) {
					Message.sendError("Invalid parameters. Check your argument count!");
					return;
				}
				
				int time = Util.parseTime(args[3]);
				if(time <= 0) {
					Message.sendError("Invalid time provided");
					return;
				}
				
				List<Integer> warnList = curMine.getWarningTimes();
				int index = warnList.indexOf(time);
				if(index == -1) {
					Message.sendError("'" + curMine.getName() + "' does not send a warning " + time + " minute(s) before the reset");
					return;
				}
				
				warnList.remove(index);
				curMine.setWarningTimes(warnList);
				Message.sendSuccess(curMine.getName() + " will no longer send a warning " + time + " minute(s) before the reset");
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
