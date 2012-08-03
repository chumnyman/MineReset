package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineError;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;


public class TimerCommand  implements BaseCommand {
	public boolean run(String[] args) {
		if(!Util.hasPermission("edit.timer")) {
			ChatUtil.sendInvalid(MineError.ACCESS, args);
			return false;
		}
		
		if(args.length == 1) {
			getHelp();
			return true;
		}

		Mine curMine = CommandManager.getMine();
		if(curMine == null) {
			ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
			return false;
		}
		
		if(args[1].equalsIgnoreCase("toggle")) {
			if(args.length != 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
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
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
			}
			int time = Util.parseTime(args[2]);
			if(time <= 0) {
				ChatUtil.sendError("Invalid time provided");
				return false;
			}
			curMine.setResetPeriod(time);
			String parsedTime = Util.parseSeconds(time);
			ChatUtil.sendNote(curMine.getName(), "Mine will now reset every " + ChatColor.GOLD + parsedTime + ChatColor.WHITE + " minute(s)");
		}
		else if(args[1].equalsIgnoreCase("warning")) {
			if(args.length < 3) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
			}
			
			if(args[2].equalsIgnoreCase("toggle")) {
				if(args.length != 3) {
					ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
					return false;
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
					ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
					return false;
				}
				
				int time = Util.parseTime(args[3]);
				if(time <= 0) {
					ChatUtil.sendError("Invalid time provided");
					return false;
				}
				if(time > curMine.getResetPeriod()) {
					ChatUtil.sendError("Time cannot be set to a value greater then the reset time");
					return false;
				}
				
				List<Integer> warnList = curMine.getWarningTimes();
				warnList.add(time);
				curMine.setWarningTimes(warnList);
				String parsedTime = Util.parseSeconds(time);
				ChatUtil.sendSuccess(curMine.getName() + " will now send warnings " + ChatColor.GOLD + parsedTime + ChatColor.WHITE + " minute(s) before the reset");
			}
			else if(args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("-")) {
				if(args.length != 4) {
					ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
					return false;
				}
				
				int time = Util.parseTime(args[3]);
				if(time <= 0) {
					ChatUtil.sendError("Invalid time provided");
					return false;
				}
				
				List<Integer> warnList = curMine.getWarningTimes();
				int index = warnList.indexOf(time);
				if(index == -1) {
					ChatUtil.sendError("'" + curMine.getName() + "' does not send a warning " + ChatColor.GOLD + Util.parseSeconds(time) + ChatColor.WHITE + " minute(s) before the reset");
					return false;
				}
				
				warnList.remove(index);
				curMine.setWarningTimes(warnList);
				ChatUtil.sendSuccess(curMine.getName() + " will no longer send a warning " + ChatColor.GOLD + Util.parseSeconds(time) + ChatColor.WHITE + " minute(s) before the reset");
			}
			else {
				ChatUtil.sendInvalid(MineError.INVALID, args);
				return false;
			}
		}
		else {
			ChatUtil.sendInvalid(MineError.INVALID, args);
			return false;
		}
		
		MineUtil.save(curMine);
		return true;
	}

	public void getHelp() {
		ChatUtil.formatHeader(20, "Timer");
		ChatUtil.formatHelp("timer", "toggle", "Toggles the automatic resets on and off");
		ChatUtil.formatHelp("timer", "set <time>", "Changes the automatic reset time to the value specified");
		ChatUtil.formatHelp("timer", "warning toggle", "Toggles reset warnings on and off");
		ChatUtil.formatHelp("timer", "warning + <time>", "Adds a warning at time specified");
		ChatUtil.formatHelp("timer", "warning - <time>", "Adds a warning at time specified");
		return;
	}
}
