package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Protection;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;

public class ProtectionCommand {
	public static void run(String[] args) {
		if(!Util.hasPermission("edit.protection")) {
			ChatUtil.sendDenied(args);
			return;
		}
		
		if(args.length == 1) {
			HelpCommand.getProtection();
			return;
		}
		if(args.length > 5) {
			ChatUtil.sendInvalidArguments(args);
		}
		
		Mine curMine = CommandManager.getMine();
		if(curMine == null) {
			ChatUtil.sendMineNotSelected();
			return;
		}
		
		if(args[1].equalsIgnoreCase("save")) {
			if(args.length != 2) {
				ChatUtil.sendInvalidArguments(args);
				return;
			}
			
			if(!Util.locationsSet()) {
				ChatUtil.sendError("Make a selection first");
				return;
			}
			
			Location[] loc = CommandManager.getLocation();
			
			if(!loc[0].getWorld().equals(loc[1].getWorld())) {
				ChatUtil.sendError("Your selection points are in different worlds");
				return;
			}
			
			if(!loc[0].getWorld().equals(curMine.getWorld())) {
				ChatUtil.sendError("Mine and protection regions are in different worlds");
				return;
			}
	        
	        // Parsing the coordinates
	        double temp = 0;
			
			if((int)loc[0].getX() > (int)loc[1].getX()) {
				temp = loc[0].getBlockX();
				loc[0].setX(loc[1].getBlockX());
				loc[1].setX(temp);
			}
			
			if((int)loc[0].getY() > (int)loc[1].getY()) {
				temp = loc[0].getBlockY();
				loc[0].setY(loc[1].getBlockY());
				loc[1].setY(temp);
			}
			
			if((int)loc[0].getZ() > (int)loc[1].getZ()) {
				temp = loc[0].getBlockZ();
				loc[0].setZ(loc[1].getBlockZ());
				loc[1].setZ(temp);
			}
			
			curMine.setProtectionRegion(loc);
			ChatUtil.sendNote(curMine.getName(), "Protection region has been set!");
		}
		else if(args[1].equalsIgnoreCase("pvp")) {
			if(args.length != 2) {
				ChatUtil.sendInvalidArguments(args);
				return;
			}
			if(curMine.getProtection().contains(Protection.PVP)) {
				curMine.getProtection().remove(Protection.PVP);
				ChatUtil.sendNote(curMine.getName(), "PVP protection has been turned " + ChatColor.RED + "off");
			}
			else {
				curMine.getProtection().add(Protection.PVP);
				ChatUtil.sendNote(curMine.getName(), "PVP protection has been turned " + ChatColor.GREEN + "on");
			}
		}
		else if(args[1].equalsIgnoreCase("breaking") || args[1].equalsIgnoreCase("break")) {
			if(args.length < 3) {
				ChatUtil.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			if(args[2].equalsIgnoreCase("toggle")) {

				if(args.length != 3) {
					ChatUtil.sendInvalidArguments(args);
					return;
				}
				
				if(curMine.getProtection().contains(Protection.BLOCK_BREAK)) {
					curMine.getProtection().remove(Protection.BLOCK_BREAK);
					curMine.getBreakBlacklist().setEnabled(false);
					ChatUtil.sendNote(curMine.getName(), "Block breaking protection has been turned " + ChatColor.RED + "off");
				}
				else {
					curMine.getProtection().add(Protection.BLOCK_BREAK);
					curMine.getBreakBlacklist().setEnabled(true);
					ChatUtil.sendNote(curMine.getName(), "Block breaking protection has been turned " + ChatColor.GREEN + "on");
				}
			}
			else if(args[2].equalsIgnoreCase("whitelist")) {
				if(args.length != 3) {
					ChatUtil.sendInvalidArguments(args);
					return;
				}
				if(curMine.getBreakBlacklist().getWhitelist()) {
					curMine.getBreakBlacklist().setWhitelist(false);
					ChatUtil.sendNote(curMine.getName(), "Block breaking whitelist is now " + ChatColor.RED + "off");
				}
				else {
					curMine.getBreakBlacklist().setWhitelist(true);
					ChatUtil.sendNote(curMine.getName(), "Block breaking whitelist is now " + ChatColor.GREEN + "on");
				}
			}
			else if(args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("+")) {
				if(args.length != 4) {
					ChatUtil.sendInvalidArguments(args);
					return;
				}
				MaterialData block = Util.getBlock(args[3]);
				
				if(block == null) {
					ChatUtil.sendBlockDoesNotExist(args[3]);
					return;
				}
				
				List<MaterialData> blockList = curMine.getBreakBlacklist().getBlocks();
				blockList.add(block);
				curMine.getBreakBlacklist().setBlocks(blockList);
				
				ChatUtil.sendNote(curMine.getName(), ChatColor.GREEN + block.getItemType().toString().toLowerCase().replace("_", " ") + ChatColor.WHITE + " was added to the block breaking protection blacklist");
			}
			else if(args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("-")) {
				if(args.length != 4) {
					ChatUtil.sendInvalidArguments(args);
					return;
				}
				MaterialData block = Util.getBlock(args[3]);
				
				if(block == null) {
					ChatUtil.sendBlockDoesNotExist(args[3]);
					return;
				}
				
				List<MaterialData> blockList = curMine.getBreakBlacklist().getBlocks();
				
				if(blockList.indexOf(block) == -1) {
					ChatUtil.sendError("There is no '" + args[3] + "' in break protection blacklist of mine '" + curMine.getName() + "'");
					return;
				}
				blockList.remove(block);
				curMine.getBreakBlacklist().setBlocks(blockList);

				ChatUtil.sendNote(curMine.getName(), ChatColor.RED + block.getItemType().toString().toLowerCase().replace("_", " ") + ChatColor.WHITE + " was removed from the block breaking protection blacklist");
			}
			else
			{
				ChatUtil.sendInvalid(args);
				return;
			}
		}
		else if(args[1].equalsIgnoreCase("placement") || args[1].equalsIgnoreCase("place")) {
			if(args.length < 3) {
				ChatUtil.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			if(args[2].equalsIgnoreCase("toggle")) {

				if(args.length != 3) {
					ChatUtil.sendInvalidArguments(args);
					return;
				}
				
				if(curMine.getProtection().contains(Protection.BLOCK_PLACE)) {
					curMine.getProtection().remove(Protection.BLOCK_PLACE);
					curMine.getPlaceBlacklist().setEnabled(false);
					ChatUtil.sendNote(curMine.getName(), "Block placement protection has been turned " + ChatColor.RED + "off");
				}
				else {
					curMine.getProtection().add(Protection.BLOCK_PLACE);
					curMine.getPlaceBlacklist().setEnabled(true);
					ChatUtil.sendNote(curMine.getName(), "Block placement protection has been turned " + ChatColor.GREEN + "on");
				}
			}
			else if(args[2].equalsIgnoreCase("whitelist")) {
				if(args.length != 3) {
					ChatUtil.sendInvalidArguments(args);
					return;
				}
				if(curMine.getPlaceBlacklist().getWhitelist()) {
					curMine.getPlaceBlacklist().setWhitelist(false);
					ChatUtil.sendNote(curMine.getName(), "Block placement whitelist is now " + ChatColor.RED + "off");
				}
				else {
					curMine.getPlaceBlacklist().setWhitelist(true);
					ChatUtil.sendNote(curMine.getName(), "Block placement whitelist is now " + ChatColor.GREEN + "on");
				}
			}
			else if(args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("+")) {
				if(args.length != 4) {
					ChatUtil.sendInvalidArguments(args);
					return;
				}
				MaterialData block = Util.getBlock(args[3]);
				
				if(block == null) {
					ChatUtil.sendBlockDoesNotExist(args[3]);
					return;
				}
				
				List<MaterialData> blockList = curMine.getPlaceBlacklist().getBlocks();
				blockList.add(block);
				curMine.getPlaceBlacklist().setBlocks(blockList);
				
				ChatUtil.sendNote(curMine.getName(), ChatColor.GREEN + block.getItemType().toString().toLowerCase().replace("_", " ") + ChatColor.WHITE + " was added to the block placement protection blacklist");
			}
			else if(args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("-")) {
				if(args.length != 4) {
					ChatUtil.sendInvalidArguments(args);
					return;
				}
				MaterialData block = Util.getBlock(args[3]);
				
				if(block == null) {
					ChatUtil.sendBlockDoesNotExist(args[3]);
					return;
				}
				
				List<MaterialData> blockList = curMine.getPlaceBlacklist().getBlocks();
				
				if(blockList.indexOf(block) == -1) {
					ChatUtil.sendError("There is no '" + args[3] + "' in place protection blacklist of mine '" + curMine.getName() + "'");
					return;
				}
				blockList.remove(block);
				curMine.getPlaceBlacklist().setBlocks(blockList);

				ChatUtil.sendNote(curMine.getName(), ChatColor.RED + block.getItemType().toString().toLowerCase().replace("_", " ") + ChatColor.WHITE + " was removed from the block placement protection blacklist");
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
