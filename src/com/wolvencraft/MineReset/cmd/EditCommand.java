package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.MineBlock;
import com.wolvencraft.MineReset.util.MineUtils;

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
			Message.sendInvalid(args);
		}
		
		if(args[0].equalsIgnoreCase("edit")) {
			if(args.length != 2) {
				Message.sendInvalid(args);
				return;
			}
			
			Mine curMine = MineUtils.getMine(args[1]);
			if(curMine == null) {
				String error = Language.getString("general.mine-name-invalid").replaceAll("%MINE%", args[1]);
				Message.sendError(error);
				return;
			}
			CommandManager.setMine(curMine);
			String message = Util.parseVars(Language.getString("general.mine-selected-successfully"), curMine);
			Message.sendSuccess(message);
		}
		else if(args[0].equalsIgnoreCase("none")) {
			if(args.length != 1) {
				Message.sendInvalid(args);
				return;
			}
			
			Mine curMine = CommandManager.getMine();
			String message = Util.parseVars(Language.getString("general.mine-deselected-successfully"), curMine);
			CommandManager.setMine(null);
			Message.sendSuccess(message);
		}
		else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("+")) {
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendError(Language.getString("general.mine-not-selected"));
				return;
			}
			
			if(args.length != 3) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			List<MineBlock> blocks = curMine.getBlocks();
			
			if(blocks.size() == 0)
				blocks.add(new MineBlock(new MaterialData(Material.AIR), 1));
			
			MaterialData block = Util.getBlock(args[1]);
			MineBlock air = MineUtils.getBlock(curMine, new MaterialData(Material.AIR));
			
			if(block == null) {
				Message.sendError("Block '"+ args[1] + "' does not exist");
				return;
			}
			if(block.equals(air.getBlock())) {
				Message.sendError("You do not need to do this. The weight of the default block is calculated automatically.");
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
				String[] awkArray = args[2].split("%");
				try
				{
					percent = Double.parseDouble(awkArray[0]);
				}
				catch(NumberFormatException nfe)
				{
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
			
			
			Message.sendSuccess(percent + "% of " + block.getItemType().toString().toLowerCase().replace("_", " ") + " added to " + curMine.getName());
			Message.sendSuccess("Reset the mine for the changes to take effect");
			return;
		}
		else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("-"))
		{
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendError(Language.getString("general.mine-not-selected"));
				return;
			}
			
			if(args.length != 3) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			MineBlock blockData = MineUtils.getBlock(curMine, Util.getBlock(args[1]));
			MineBlock air = MineUtils.getBlock(curMine, new MaterialData(Material.AIR));
			if(blockData == null) {
				Message.sendError("There is no '" + args[2] + "' in mine '" + curMine + "'");
				return;
			}
			if(blockData.equals(air)) {
				Message.sendError("You cannot remove the default block from the mine");
				return;
			}
			
			List<MineBlock> blocks = curMine.getBlocks();

			air.setChance(air.getChance() + blockData.getChance());
			blocks.remove(blockData);
			
			curMine.setBlocks(blocks);
			
			Message.sendSuccess(args[1] + " was successfully removed from mine '" + curMine.getName() + "'");
			return;
		}
		else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del"))
		{
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendError(Language.getString("general.mine-not-selected"));
				return;
			}
			
			if(args.length != 2) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			List<Mine> mines = MineReset.getMines();
			mines.remove(curMine);
			CommandManager.setMine(null);
			Message.sendSuccess("Mine '" + args[1] + "' was successfully deleted.");
			return;
		}
		else if(args[0].equalsIgnoreCase("name"))
		{
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendError(Language.getString("general.mine-not-selected"));
				return;
			}
			
			if(args.length < 2) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			String name = args[1];
			for(int i = 2; i < args.length; i++)
			{
				name = name + " " + args[i];
			}
			
			curMine.setDisplayName(name);
			return;
		}
		else if(args[0].equalsIgnoreCase("silent"))
		{
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendError(Language.getString("general.mine-not-selected"));
				return;
			}
			
			if(args.length != 1) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			if(curMine.getSilent())
			{
				curMine.setSilent(false);
				Message.sendSuccess("Silent mode has been turned off for mine '" + curMine.getName() + "'");
			}
			else
			{
				curMine.setSilent(true);
				Message.sendSuccess("'" + curMine.getName() + "' will no longer broadcast any notifications");
			}
		}
		//TODO Add generator support!
		//else if(args[0].equalsIgnoreCase("generator"))
		//{
		//	Mine curMine = CommandManager.getMine();
		//	if(curMine == null) {
		//		Message.sendError(Language.getString("general.mine-not-selected"));
		//		return;
		//	}
		//	
		//	if(args.length != 2) {
		//		Message.sendError("Invalid parameters. Check your argument count!");
		//		return;
		//	}
		//	
		//	String generator = args[1];
		//	curMine.setGenerator(args[1].toUpperCase());
		//}
		else if(args[0].equalsIgnoreCase("link"))
		{
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendError(Language.getString("general.mine-not-selected"));
				return;
			}
			
			if(args.length != 2) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			if(!args[1].equalsIgnoreCase("none") && MineUtils.getMine(args[1]) == null)
			{
				String error = Language.getString("general.mine-name-invalid");
				error = error.replaceAll("%MINE%", args[1]);
				error = error.replaceAll("%MINENAME%", args[1]);
				Message.sendError(error);
				return;
			}
			
			if(args[1].equalsIgnoreCase("none"))
			{
				curMine.setParent(null);
				Message.sendSuccess("Mine '" + curMine.getName() + "' is no longer linked to any mine");
				return;
			}
			
			curMine.setParent(MineUtils.getMine(args[1]));
			Message.sendSuccess("Mines '" + curMine.getName() + "' will now reset with '" + args[1] + "'");
			return;
		}
		else
		{
			Message.sendInvalid(args);
			return;
		}
	}
}
