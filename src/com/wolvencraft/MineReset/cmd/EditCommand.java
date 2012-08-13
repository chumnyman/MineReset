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
import com.wolvencraft.MineReset.generation.BaseGenerator;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.GeneratorUtil;
import com.wolvencraft.MineReset.util.MineError;
import com.wolvencraft.MineReset.util.Util;

public class EditCommand  implements BaseCommand {
	public boolean run(String[] args) {
		if(!Util.hasPermission("edit.info") && !Util.hasPermission("edit")) {
			ChatUtil.sendInvalid(MineError.ACCESS, args);
			return false;
		}
		
		if(args.length == 1
				&& !args[0].equalsIgnoreCase("none")
				&& !args[0].equalsIgnoreCase("delete")
				&& !args[0].equalsIgnoreCase("generator")
				&& !args[0].equalsIgnoreCase("silent")) {
			getHelp();
			return true;
		}
		
		Mine curMine = CommandManager.getMine();
		if(!args[0].equalsIgnoreCase("edit") && !args[0].equalsIgnoreCase("delete") && curMine == null) {
			ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
			return false;
		}
		
		if(args[0].equalsIgnoreCase("edit")) {
			if(args.length != 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
			}
			
			curMine = MineUtil.getMine(args[1]);
			if(curMine == null) {
				ChatUtil.sendInvalid(MineError.MINE_NAME, args, args[1]);
				return false;
			}
			
			CommandManager.setMine(curMine);
			ChatUtil.sendSuccess(Util.parseVars(Language.getString("editing.mine-selected-successfully"), curMine));
			return true;
		}
		else if(args[0].equalsIgnoreCase("none")) {
			if(args.length != 1) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
			}

			ChatUtil.sendSuccess(Util.parseVars(Language.getString("editing.mine-deselected-successfully"), curMine));
			CommandManager.setMine(null);
			return true;
		}
		else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("+")) {
			if(args.length != 2 && args.length != 3) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
			}
			
			List<MineBlock> blocks = curMine.getBlocks();
			if(blocks.size() == 0) blocks.add(new MineBlock(new MaterialData(Material.AIR), 1));
			
			MaterialData block = Util.getBlock(args[1]);
			MineBlock air = MineUtil.getBlock(curMine, new MaterialData(Material.AIR));
			
			if(block == null) {
				ChatUtil.sendInvalid(MineError.INVALID_BLOCK, args, args[1]);
				return false;
			}
			if(block.equals(air.getBlock())) {
				ChatUtil.sendError("This value is calculated automatically");
				return false;
			}

			double percent, percentAvailable = air.getChance();
			
			if(args.length == 3) {
				if(Util.isNumeric(args[2])) percent = Double.parseDouble(args[2]);
				else {
					ChatUtil.debug("Argument is not numeric, attempting to parse");
					try { percent = Double.parseDouble(args[2].replace("%", "")); }
					catch(NumberFormatException nfe) {
						ChatUtil.sendInvalid(MineError.INVALID, args);
						return false;
					}
				}
				
				percent = percent / 100;
				ChatUtil.debug("Chance value is " + percent);
			}
			else percent = percentAvailable;
			
			if(percent <= 0) {
				ChatUtil.sendInvalid(MineError.INVALID, args);
				return false;
			}
			
			if((percentAvailable - percent) < 0) {
				ChatUtil.sendError("Invalid percentage. Use /mine info " + curMine.getName() + " to review the percentages");
				return false;
			}
			else percentAvailable -= percent;
			air.setChance(percentAvailable);
			
			MineBlock index = MineUtil.getBlock(curMine, block);
			
			if(index == null) blocks.add(new MineBlock(block, percent));
			else index.setChance(index.getChance() + percent);
			
			ChatUtil.sendNote(curMine.getName(), Util.format(percent) + " of " + block.getItemType().toString().toLowerCase().replace("_", " ") + " added to the mine");
			ChatUtil.sendNote(curMine.getName(), "Reset the mine for the changes to take effect");
			MineUtil.save(curMine);
			return true;
		}
		else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("-")) {
			if(args.length != 2 && args.length != 3) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
			}
			
			MineBlock blockData = MineUtil.getBlock(curMine, Util.getBlock(args[1]));
			if(blockData == null) {
				ChatUtil.sendError("There is no '" + args[1] + "' in mine '" + curMine + "'");
				return false;
			}

			MineBlock air = MineUtil.getBlock(curMine, new MaterialData(Material.AIR));
			if(blockData.equals(air)) {
				ChatUtil.sendError("This value is calculated automatically");
				return false;
			}
			
			double percent;
			
			if(args.length == 3) {
				if(Util.isNumeric(args[2])) percent = Double.parseDouble(args[2]);
				else {
					ChatUtil.debug("Argument is not numeric, attempting to parse");
					try {
						percent = Double.parseDouble(args[2].replace("%", ""));
					}
					catch(NumberFormatException nfe) {
						ChatUtil.sendInvalid(MineError.INVALID, args);
						return false;
					}
				}
				
				percent = percent / 100;
				ChatUtil.debug("Chance value is " + percent);
				
				if(percent > blockData.getChance()) percent = blockData.getChance();
				
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
			return true;
		}
		else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del")) {
			if(args.length > 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
			}
			
			if(args.length == 1) {
				curMine = CommandManager.getMine();
				if(curMine == null) {
					ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
					return false;
				}
			}
			else {
				curMine = MineUtil.getMine(args[1]);
				if(curMine == null) {
					ChatUtil.sendInvalid(MineError.MINE_NAME, args, args[1]);
					return false;
				}
			}
			
			List<Mine> mines = MineReset.getMines();
			mines.remove(curMine);
			CommandManager.setMine(null);
			ChatUtil.sendNote(curMine.getName(), "Mine successfully deleted");
			MineUtil.delete(curMine);
			MineUtil.saveAll();
			return true;
		}
		else if(args[0].equalsIgnoreCase("name")) {
			if(args.length < 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
			}
			
			String name = args[1];
			for(int i = 2; i < args.length; i++) name = name + " " + args[i];
			
			curMine.setDisplayName(name);
			ChatUtil.sendNote(curMine.getName(), "Mine now has a display name '" + ChatColor.GOLD + name + ChatColor.WHITE + "'");
			MineUtil.save(curMine);
			return true;
		}
		else if(args[0].equalsIgnoreCase("silent")) {
			if(args.length != 1) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
			}
			
			if(curMine.getSilent()) {
				curMine.setSilent(false);
				ChatUtil.sendNote(curMine.getName(), "Silent mode " + ChatColor.RED + "off");
			}
			else {
				curMine.setSilent(true);
				ChatUtil.sendNote(curMine.getName(), "Silent mode " + ChatColor.GREEN + "on");
			}
			MineUtil.save(curMine);
			return true;
		}
		else if(args[0].equalsIgnoreCase("cooldown")) {
			if(args.length != 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
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
						return false;
					}
					curMine.setCooldownTime(seconds);
					ChatUtil.sendNote(curMine.getName(), "Reset cooldown set to " + ChatColor.GREEN + Util.parseSeconds(seconds));
				}
				catch (NumberFormatException nfe) {
					ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				}
			}
			MineUtil.save(curMine);
			return true;
		}
		else if(args[0].equalsIgnoreCase("generator")) {
			if(args.length == 1) {
				getGenerators();
				return false;
			}
			
			if(args.length != 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
			}
			
			curMine.setGenerator(args[1].toUpperCase());
			GeneratorUtil.get(args[1]).init(curMine);
			
			ChatUtil.sendNote(curMine.getName(), "Mine generator has been set to " + ChatColor.GREEN + args[1].toUpperCase());

			MineUtil.save(curMine);
			return true;
		}
		else if(args[0].equalsIgnoreCase("setparent") || args[0].equalsIgnoreCase("link")) {
			if(args.length != 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return false;
			}
			
			if(args[1].equalsIgnoreCase("none")) {
				ChatUtil.sendNote(curMine.getName(), "Mine is no longer linked to " + ChatColor.RED + curMine.getParent());
				curMine.setParent(null);
				MineUtil.save(curMine);
				return true;
			}
			
			if(MineUtil.getMine(args[1]) == null) {
				ChatUtil.sendInvalid(MineError.MINE_NAME, args, args[1]);
				return false;
			}
			
			curMine.setParent(args[1]);
			ChatUtil.sendNote(curMine.getName(), "Mine will is now linked to " + ChatColor.GREEN + args[1]);
			MineUtil.save(curMine);
			return true;
		}
		else {
			ChatUtil.sendInvalid(MineError.INVALID, args);
			return false;
		}
	}
	

	public void getHelp() {
		ChatUtil.formatHeader(20, "Editing");
		ChatUtil.formatHelp("edit", "<id>", "Selects a mine to edit its properties");
		ChatUtil.formatHelp("none", "", "De-selects the mine");
		ChatUtil.formatHelp("name", "<id>", "Creates a display name for a mine");
		ChatUtil.formatHelp("+", "<block> [percentage]", "Adds a block type to the mine");
		ChatUtil.formatMessage("If no persentage is provided, the block will fill up all the space available");
		ChatUtil.formatHelp("-", "<block> [persentage]", "Removes the block from the mine");
		ChatUtil.formatMessage("If no persentage is provided, the block will be removed completely");
		ChatUtil.formatHelp("delete", "", "Deletes all the data about the selected mine");
		ChatUtil.formatHelp("silent", "", "Toggles the public notifications on and off");
		ChatUtil.formatHelp("setparent", "<id>", "Links the timers of two mines");
		ChatUtil.formatHelp("generator", "<generator>", "Changes the active generator");
		ChatUtil.formatMessage("The following generators are supported: ");
		ChatUtil.formatMessage(GeneratorUtil.list());
		ChatUtil.formatHelp("cooldown toggle", "", "Turn the reset cooldown on and off for the mine");
		ChatUtil.formatHelp("cooldown <time>", "", "Sets the cooldown time to the value specified");
		return;
	}
	
	public void getGenerators() {
		ChatUtil.formatHelp("generator", "<generator>", "Changes the active generator for the mine");
		ChatUtil.formatMessage("The following generators are available:");
		for(BaseGenerator gen : MineReset.getGenerators())
			ChatUtil.formatMessage(gen.getName() + ": " + gen.getDescription());
		return;
	}
}
