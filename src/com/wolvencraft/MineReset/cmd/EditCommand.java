package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.mine.Generator;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.MineBlock;
import com.wolvencraft.MineReset.util.MineUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class EditCommand {
	public static void run(String[] args) {
		if(!Util.senderHasPermission("edit")) {
				Message.sendDenied(args);
				return;
			}
		
		if(!args[0].equalsIgnoreCase("none") && args.length == 1) {
			HelpCommand.getEdit();
			return;
		}
		if(args.length > 3) {
			Message.sendInvalidArguments(args);
		}
		
		if(args[0].equalsIgnoreCase("edit")) {
			if(args.length != 2) {
				Message.sendInvalidArguments(args);
				return;
			}
			
			Mine curMine = MineUtils.getMine(args[1]);
			if(curMine == null) {
				Message.sendInvalidMineName(args[1]);
				return;
			}
			String message = Util.parseVars(Language.getString("editing.mine-selected-successfully"), curMine);
			CommandManager.setMine(curMine);
			Message.sendSuccess(message);
		}
		else if(args[0].equalsIgnoreCase("none")) {
			if(args.length != 1) {
				Message.sendInvalidArguments(args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendMineNotSelected();
				return;
			}
			
			String message = Util.parseVars(Language.getString("editing.mine-deselected-successfully"), curMine);
			CommandManager.setMine(null);
			Message.sendSuccess(message);
		}
		else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("+")) {
			if(args.length != 3) {
				Message.sendInvalidArguments(args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendMineNotSelected();
				return;
			}
			
			List<MineBlock> blocks = curMine.getBlocks();
			
			if(blocks.size() == 0)
				blocks.add(new MineBlock(new MaterialData(Material.AIR), 1));
			
			MaterialData block = Util.getBlock(args[1]);
			MineBlock air = MineUtils.getBlock(curMine, new MaterialData(Material.AIR));
			
			if(block == null) {
				Message.sendBlockDoesNotExist(args[1]);
				return;
			}
			if(block.equals(air.getBlock())) {
				Message.sendError(Language.getString("error.removing-air"));
				return;
			}

			
			double percent;
			if(Util.isNumeric(args[2])) {
				percent = Double.parseDouble(args[2]);
				if(percent <= 0) {
					Message.sendInvalid(args);
					return;
				}
				percent = (double)(Math.round(percent * 1000)) / 1000;
			}
			else {
				Message.debug("Argument is not numeric, attempting to parse");
				try {
					percent = Double.parseDouble(args[2].replace("%", ""));
				}
				catch(NumberFormatException nfe) {
					Message.sendInvalid(args);
					return;
				}
			}
			percent = percent / 100;
			Message.debug("Chance value is " + percent);
			
			double percentAvailable = air.getChance();
			if((percentAvailable - percent) <= 0) {
				Message.sendError("Invalid percentage. Use /mine info " + curMine.getName() + " to review the percentages");
				return;
			}
			else percentAvailable -= percent;
			
			air.setChance(percentAvailable);
			
			MineBlock index = MineUtils.getBlock(curMine, block);
					
			if(index == null)
				blocks.add(new MineBlock(block, percent));
			else
				index.setChance(index.getChance() + percent);
			
			Message.sendNote(curMine.getName(), percent + "% of " + block.getItemType().toString().toLowerCase().replace("_", " ") + " added to the mine");
			Message.sendNote(curMine.getName(), "Reset the mine for the changes to take effect");
			return;
		}
		else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("-")) {
			if(args.length != 3) {
				Message.sendInvalidArguments(args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendMineNotSelected();
				return;
			}
			
			MineBlock blockData = MineUtils.getBlock(curMine, Util.getBlock(args[1]));
			MineBlock air = MineUtils.getBlock(curMine, new MaterialData(Material.AIR));
			if(blockData == null) {
				Message.sendError("There is no '" + args[2] + "' in mine '" + curMine + "'");
				return;
			}
			if(blockData.equals(air)) {
				Message.sendError(Language.getString("error.removing-air"));
				return;
			}
			
			List<MineBlock> blocks = curMine.getBlocks();

			air.setChance(air.getChance() + blockData.getChance());
			blocks.remove(blockData);
			
			curMine.setBlocks(blocks);
			
			Message.sendNote(curMine.getName(), args[1] + " was successfully removed from the mine");
			return;
		}
		else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del")) {
			if(args.length != 1) {
				Message.sendInvalidArguments(args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendMineNotSelected();
				return;
			}
			
			List<Mine> mines = MineReset.getMines();
			mines.remove(curMine);
			CommandManager.setMine(null);
			Message.sendNote(args[1], "Mine successfully deleted");
			return;
		}
		else if(args[0].equalsIgnoreCase("name")) {
			if(args.length < 2) {
				Message.sendInvalidArguments(args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendMineNotSelected();
				return;
			}
			
			String name = args[1];
			for(int i = 2; i < args.length; i++) {
				name = name + " " + args[i];
			}
			
			curMine.setDisplayName(name);
			Message.sendNote(curMine.getName(), "Mine now has a display name '" + ChatColor.GOLD + name + ChatColor.WHITE + "'");
			return;
		}
		else if(args[0].equalsIgnoreCase("silent")) {
			if(args.length != 1) {
				Message.sendInvalidArguments(args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendMineNotSelected();
				return;
			}
			
			
			if(curMine.getSilent()) {
				curMine.setSilent(false);
				Message.sendNote(curMine.getName(), "Silent mode " + ChatColor.RED + "off");
			}
			else
			{
				curMine.setSilent(true);
				Message.sendNote(curMine.getName(), "Silent mode " + ChatColor.GREEN + "on");
			}
			return;
		}
		else if(args[0].equalsIgnoreCase("generator")) {
			if(args.length != 2) {
				Message.sendInvalidArguments(args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendMineNotSelected();
				return;
			}
			
			if(args[1].equalsIgnoreCase("empty")) curMine.setGenerator(Generator.EMPTY);
			else curMine.setGenerator(Generator.RANDOM);
			Message.sendNote(curMine.getName(), "Mine generator has been set to " + ChatColor.GREEN + args[1].toUpperCase());
			return;
		}
		else if(args[0].equalsIgnoreCase("link") || args[0].equalsIgnoreCase("setparent")) {
			if(args.length != 2) {
				Message.sendInvalidArguments(args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendMineNotSelected();
				return;
			}
			
			if(!args[1].equalsIgnoreCase("none") && MineUtils.getMine(args[1]) == null) {
				Message.sendInvalidMineName(args[1]);
				return;
			}
			
			if(args[1].equalsIgnoreCase("none")) {
				curMine.setParent(null);
				Message.sendNote(curMine.getName(), "Mine is no longer linked to " + ChatColor.RED + args[1]);
				return;
			}
			
			curMine.setParent(MineUtils.getMine(args[1]));
			Message.sendNote(curMine.getName(), "Mine will use the timers of " + ChatColor.GREEN + args[1]);
			return;
		}
		else {
			Message.sendInvalid(args);
			return;
		}
	}
}
