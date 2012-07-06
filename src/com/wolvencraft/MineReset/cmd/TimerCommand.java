package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;


public class TimerCommand {
	
	public static void run(String[] args) {
		if(!Util.hasPermission("edit.timer")) {
			ChatUtil.sendDenied(args);
			return;
		}
		
		if(args.length == 1) {
			HelpCommand.getTimer();
			return;
		}

		Mine curMine = CommandManager.getMine();
		if(curMine == null) {
			ChatUtil.sendMineNotSelected();
			return;
		}
		
		if(args[1].equalsIgnoreCase("toggle")) {
			if(args.length != 2) {
				ChatUtil.sendInvalidArguments(args);
				return;
			}
			
			if(curMine.getAutomatic()) {
				curMine.setAutomatic(false);
				ChatUtil.sendNote(curMine.getName(), "Automatic mine reset is " + ChatColor.RED + "off");
			}
			else {
				curMine.setAutomatic(true);
				if(curMine.getResetPeriod() == 0) curMine.setResetPeriod(900);
				ChatUtil.sendNote(curMine.getName(), "Automatic mine reset is " + ChatColor.GREEN + "on");
			}
		}
		else if(args[1].equalsIgnoreCase("set")) {
			if(args.length != 3) {
				ChatUtil.sendInvalidArguments(args);
				return;
			}
			int time = Util.parseTime(args[2]);
			if(time <= 0) {
				ChatUtil.sendError("Invalid time provided");
				return;
			}
			curMine.setResetPeriod(time);
			String parsedTime = Util.parseSeconds(time);
			ChatUtil.sendNote(curMine.getName(), "Mine will now reset every " + ChatColor.GOLD + parsedTime + ChatColor.WHITE + " minute(s)");
		}
		else if(args[1].equalsIgnoreCase("warning")) {
			if(args.length < 3) {
				ChatUtil.sendInvalidArguments(args);
				return;
			}
			
			if(args[2].equalsIgnoreCase("toggle")) {
				if(args.length != 3) {
					ChatUtil.sendInvalidArguments(args);
					return;
				}
				
				if(curMine.getWarned()) {
					curMine.setWarned(false);
					ChatUtil.sendNote(curMine.getName(), "Reset warnings are " + ChatColor.RED + "off");
				}
				else {
					curMine.setWarned(true);
					ChatUtil.sendNote(curMine.getName(), "Reset warnings are " + ChatColor.GREEN + "on");
				}
			}
			else if(args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("+")) {
				if(args.length != 4) {
					ChatUtil.sendInvalidArguments(args);
					return;
				}
				
				int time = Util.parseTime(args[3]);
				if(time <= 0) {
					ChatUtil.sendError("Invalid time provided");
					return;
				}
				if(time > curMine.getResetPeriod()) {
					ChatUtil.sendError("Time cannot be set to a value greater then the reset time");
					return;
				}
				
				List<Integer> warnList = curMine.getWarningTimes();
				warnList.add(time);
				curMine.setWarningTimes(warnList);
				String parsedTime = Util.parseSeconds(time);
				ChatUtil.sendSuccess(curMine.getName() + " will now send warnings " + ChatColor.GOLD + parsedTime + ChatColor.WHITE + " minute(s) before the reset");
			}
			else if(args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("-")) {
				if(args.length != 4) {
					ChatUtil.sendInvalidArguments(args);
					return;
				}
				
				int time = Util.parseTime(args[3]);
				if(time <= 0) {
					ChatUtil.sendError("Invalid time provided");
					return;
				}
				
				List<Integer> warnList = curMine.getWarningTimes();
				int index = warnList.indexOf(time);
				if(index == -1) {
					ChatUtil.sendError("'" + curMine.getName() + "' does not send a warning " + ChatColor.GOLD + Util.parseSeconds(time) + ChatColor.WHITE + " minute(s) before the reset");
					return;
				}
				
				warnList.remove(index);
				curMine.setWarningTimes(warnList);
				ChatUtil.sendSuccess(curMine.getName() + " will no longer send a warning " + ChatColor.GOLD + Util.parseSeconds(time) + ChatColor.WHITE + " minute(s) before the reset");
			}
			else
			{
				ChatUtil.sendInvalid(args);
				return;
			}
		}
		else
		{
			ChatUtil.sendInvalid(args);
			return;
		}
		
		MineUtil.save(curMine);
		return;
	}
}
