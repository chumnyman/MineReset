package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.MineBlock;
import com.wolvencraft.MineReset.util.MineUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineError;
import com.wolvencraft.MineReset.util.Util;

public class EditCommand {
	public static void run(String[] args) {
		if(!Util.hasPermission("edit.info")) {
			ChatUtil.sendInvalid(MineError.ACCESS, args);
				return;
		}
		
		if(!args[0].equalsIgnoreCase("none") && !args[0].equalsIgnoreCase("delete") && args.length == 1) {
			HelpCommand.getEdit();
			return;
		}
		if(args.length > 3) {
			ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
			return;
		}
		
		if(args[0].equalsIgnoreCase("edit")) {
			if(args.length != 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			
			Mine curMine = MineUtil.getMine(args[1]);
			if(curMine == null) {
				ChatUtil.sendInvalid(MineError.MINE_NAME, args, args[1]);
				return;
			}
			String message = Util.parseVars(Language.getString("editing.mine-selected-successfully"), curMine);
			CommandManager.setMine(curMine);
			ChatUtil.sendSuccess(message);
			return;
		}
		else if(args[0].equalsIgnoreCase("none")) {
			if(args.length != 1) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
				return;
			}
			
			String message = Util.parseVars(Language.getString("editing.mine-deselected-successfully"), curMine);
			CommandManager.setMine(null);
			ChatUtil.sendSuccess(message);
			return;
		}
		else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("+")) {
			if(args.length != 2 && args.length != 3) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
				return;
			}
			
			List<MineBlock> blocks = curMine.getBlocks();
			
			if(blocks.size() == 0)
				blocks.add(new MineBlock(new MaterialData(Material.AIR), 1));
			
			MaterialData block = Util.getBlock(args[1]);
			MineBlock air = MineUtil.getBlock(curMine, new MaterialData(Material.AIR));
			
			if(block == null) {
				ChatUtil.sendInvalid(MineError.INVALID_BLOCK, args, args[1]);
				return;
			}
			if(block.equals(air.getBlock())) {
				ChatUtil.sendError(Language.getString("error.removing-air"));
				return;
			}

			double percent;
			double percentAvailable = air.getChance();
			
			if(args.length == 3) {
				if(Util.isNumeric(args[2])) {
					percent = Double.parseDouble(args[2]);
				}
				else {
					ChatUtil.debug("Argument is not numeric, attempting to parse");
					try {
						percent = Double.parseDouble(args[2].replace("%", ""));
					}
					catch(NumberFormatException nfe) {
						ChatUtil.sendInvalid(MineError.INVALID, args);
						return;
					}
				}
				
				percent = percent / 100;
				ChatUtil.debug("Chance value is " + percent);
			}
			else {
				percent = percentAvailable;
			}
			
			if(percent <= 0) {
				ChatUtil.sendInvalid(MineError.INVALID, args);
				return;
			}
			
			if((percentAvailable - percent) < 0) {
				ChatUtil.sendError("Invalid percentage. Use /mine info " + curMine.getName() + " to review the percentages");
				return;
			}
			else percentAvailable -= percent;
			
			air.setChance(percentAvailable);
			
			MineBlock index = MineUtil.getBlock(curMine, block);
			
			if(index == null)
				blocks.add(new MineBlock(block, percent));
			else
				index.setChance(index.getChance() + percent);
			
			ChatUtil.sendNote(curMine.getName(), Util.format(percent) + " of " + block.getItemType().toString().toLowerCase().replace("_", " ") + " added to the mine");
			ChatUtil.sendNote(curMine.getName(), "Reset the mine for the changes to take effect");
			MineUtil.save(curMine);
			return;
		}
		else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("-")) {
			if(args.length != 2 && args.length != 3) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
				return;
			}
			
			MineBlock blockData = MineUtil.getBlock(curMine, Util.getBlock(args[1]));
			MineBlock air = MineUtil.getBlock(curMine, new MaterialData(Material.AIR));
			if(blockData == null) {
				ChatUtil.sendError("There is no '" + args[1] + "' in mine '" + curMine + "'");
				return;
			}
			if(blockData.equals(air)) {
				ChatUtil.sendError(Language.getString("error.removing-air"));
				return;
			}
			
			double percent;
			
			if(args.length == 3) {
				if(Util.isNumeric(args[2])) {
					percent = Double.parseDouble(args[2]);
				}
				else {
					ChatUtil.debug("Argument is not numeric, attempting to parse");
					try {
						percent = Double.parseDouble(args[2].replace("%", ""));
					}
					catch(NumberFormatException nfe) {
						ChatUtil.sendInvalid(MineError.INVALID, args);
						return;
					}
				}
				
				percent = percent / 100;
				ChatUtil.debug("Chance value is " + percent);
				
				if(percent > blockData.getChance())
					percent = blockData.getChance();
				
				air.setChance(air.getChance() + percent);
				blockData.setChance(blockData.getChance() - percent);
				
				ChatUtil.sendNote(curMine.getName(), Util.format(percent) + " of " + args[1] + " was successfully removed from the mine");
			}
			else {
				List<MineBlock> blocks = curMine.getBlocks();

				air.setChance(air.getChance() + blockData.getChance());
				blocks.remove(blockData);
				
				curMine.setBlocks(blocks);
				
				ChatUtil.sendNote(curMine.getName(), args[1] + " was successfully removed from the mine");
			}
			MineUtil.save(curMine);
			return;
		}
		else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del")) {
			if(args.length > 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			
			Mine curMine;
			if(args.length == 1) {
				curMine = CommandManager.getMine();
				if(curMine == null) {
					ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
					return;
				}
			}
			else {
				curMine = MineUtil.getMine(args[1]);
				if(curMine == null) {
					ChatUtil.sendInvalid(MineError.MINE_NAME, args, args[1]);
					return;
				}
			}
			
			List<Mine> mines = MineReset.getMines();
			mines.remove(curMine);
			CommandManager.setMine(null);
			ChatUtil.sendNote(curMine.getName(), "Mine successfully deleted");
			MineUtil.delete(curMine);
			MineUtil.saveAll();
			return;
		}
		else if(args[0].equalsIgnoreCase("name")) {
			if(args.length < 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
				return;
			}
			
			String name = args[1];
			for(int i = 2; i < args.length; i++) {
				name = name + " " + args[i];
			}
			
			curMine.setDisplayName(name);
			ChatUtil.sendNote(curMine.getName(), "Mine now has a display name '" + ChatColor.GOLD + name + ChatColor.WHITE + "'");

			MineUtil.save(curMine);
			return;
		}
		else if(args[0].equalsIgnoreCase("silent")) {
			if(args.length != 1) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
				return;
			}
			
			
			if(curMine.getSilent()) {
				curMine.setSilent(false);
				ChatUtil.sendNote(curMine.getName(), "Silent mode " + ChatColor.RED + "off");
			}
			else
			{
				curMine.setSilent(true);
				ChatUtil.sendNote(curMine.getName(), "Silent mode " + ChatColor.GREEN + "on");
			}
			MineUtil.save(curMine);
			return;
		}
		else if(args[0].equalsIgnoreCase("cooldown")) {
			if(args.length != 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
				return;
			}
			if(args[1].equalsIgnoreCase("toggle")) {
				if(curMine.getCooldown()) {
					curMine.setCooldown(false);
					ChatUtil.sendNote(curMine.getName(), "Reset cooldown " + ChatColor.RED + "disabled");
				}
				else {
					curMine.setCooldown(true);
					ChatUtil.sendNote(curMine.getName(), "Reset cooldown " + ChatColor.GREEN + "enabled");
				}
			}
			else {
				try {
					int seconds = Util.parseTime(args[1]);
					if(seconds == -1) {
						ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
						return;
					}
					curMine.setCooldownTime(seconds);
					ChatUtil.sendNote(curMine.getName(), "Reset cooldown set to " + ChatColor.GREEN + Util.parseSeconds(seconds));
				}
				catch (NumberFormatException nfe) {
					ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				}
			}

			MineUtil.save(curMine);
			return;
		}
		else if(args[0].equalsIgnoreCase("generator")) {
			if(args.length != 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
				return;
			}
			
			curMine.setGenerator(args[1].toUpperCase());
			ChatUtil.sendNote(curMine.getName(), "Mine generator has been set to " + ChatColor.GREEN + args[1].toUpperCase());

			MineUtil.save(curMine);
			return;
		}
		else if(args[0].equalsIgnoreCase("link") || args[0].equalsIgnoreCase("setparent")) {
			if(args.length != 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
				return;
			}
			
			if(!args[1].equalsIgnoreCase("none") && MineUtil.getMine(args[1]) == null) {
				ChatUtil.sendInvalid(MineError.MINE_NAME, args, args[1]);
				return;
			}
			
			if(args[1].equalsIgnoreCase("none")) {
				ChatUtil.sendNote(curMine.getName(), "Mine is no longer linked to " + ChatColor.RED + curMine.getParent());
				curMine.setParent(null);
				MineUtil.save(curMine);
				return;
			}
			
			curMine.setParent(args[1]);
			ChatUtil.sendNote(curMine.getName(), "Mine will use the timers of " + ChatColor.GREEN + args[1]);
			MineUtil.save(curMine);
			return;
		}
		else {
			ChatUtil.sendInvalid(MineError.INVALID, args);
			return;
		}
	}
}
