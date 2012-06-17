package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Protection;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.MineUtils;
import com.wolvencraft.MineReset.util.Util;

public class ProtectionCommand {
	public static void run(String[] args) {
		if(!Util.senderHasPermission("edit")) {
			Message.sendDenied(args);
			return;
		}
		
		if(args.length == 1) {
			HelpCommand.getProtection();
			return;
		}
		if(args.length > 5) {
			Message.sendInvalidArguments(args);
		}
		
		Mine curMine = CommandManager.getMine();
		if(curMine == null) {
			Message.sendMineNotSelected();
			return;
		}
		
		if(args[1].equalsIgnoreCase("pvp")) {
			if(args.length != 2) {
				Message.sendInvalidArguments(args);
				return;
			}
			if(curMine.getProtection().contains(Protection.PVP)) {
				curMine.getProtection().remove(Protection.PVP);
				Message.sendNote(curMine.getName(), "PVP protection has been turned " + ChatColor.RED + "off");
			}
			else {
				curMine.getProtection().add(Protection.PVP);
				Message.sendNote(curMine.getName(), "PVP protection has been turned " + ChatColor.GREEN + "on");
			}
		}
		else if(args[1].equalsIgnoreCase("breaking") || args[1].equalsIgnoreCase("break")) {
			if(args.length < 3) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			if(args[2].equalsIgnoreCase("toggle")) {

				if(args.length != 3) {
					Message.sendInvalidArguments(args);
					return;
				}
				
				if(curMine.getProtection().contains(Protection.BLOCK_BREAK)) {
					curMine.getProtection().remove(Protection.BLOCK_BREAK);
					Message.sendNote(curMine.getName(), "Block breaking protection has been turned " + ChatColor.RED + "off");
				}
				else {
					curMine.getProtection().add(Protection.BLOCK_BREAK);
					Message.sendNote(curMine.getName(), "Block breaking protection has been turned " + ChatColor.GREEN + "on");
				}
			}
			else if(args[2].equalsIgnoreCase("whitelist")) {
				if(args.length != 3) {
					Message.sendInvalidArguments(args);
					return;
				}
				if(curMine.getBreakBlacklist().getWhitelist()) {
					curMine.getBreakBlacklist().setWhitelist(false);
					Message.sendNote(curMine.getName(), "Block breaking whitelist is now " + ChatColor.RED + "off");
				}
				else {
					curMine.getBreakBlacklist().setWhitelist(true);
					Message.sendNote(curMine.getName(), "Block breaking whitelist is now " + ChatColor.GREEN + "on");
				}
			}
			else if(args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("+")) {
				if(args.length != 4) {
					Message.sendInvalidArguments(args);
					return;
				}
				MaterialData block = Util.getBlock(args[3]);
				
				if(block == null) {
					Message.sendBlockDoesNotExist(args[3]);
					return;
				}
				
				List<MaterialData> blockList = curMine.getBreakBlacklist().getBlocks();
				blockList.add(block);
				curMine.getBreakBlacklist().setBlocks(blockList);
				
				Message.sendNote(curMine.getName(), ChatColor.GREEN + block.getItemType().toString().toLowerCase().replace("_", " ") + ChatColor.WHITE + " was added to the block breaking protection blacklist");
			}
			else if(args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("-")) {
				if(args.length != 4) {
					Message.sendInvalidArguments(args);
					return;
				}
				MaterialData block = Util.getBlock(args[3]);
				
				if(block == null) {
					Message.sendBlockDoesNotExist(args[3]);
					return;
				}
				
				List<MaterialData> blockList = curMine.getBreakBlacklist().getBlocks();
				
				if(blockList.indexOf(block) == -1) {
					Message.sendError("There is no '" + args[3] + "' in break protection blacklist of mine '" + curMine.getName() + "'");
					return;
				}
				blockList.remove(block);
				curMine.getBreakBlacklist().setBlocks(blockList);

				Message.sendNote(curMine.getName(), ChatColor.RED + block.getItemType().toString().toLowerCase().replace("_", " ") + ChatColor.WHITE + " was removed from the block breaking protection blacklist");
			}
			else
			{
				Message.sendInvalid(args);
				return;
			}
		}
		else if(args[1].equalsIgnoreCase("placement") || args[1].equalsIgnoreCase("place")) {
			if(args.length < 3) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			if(args[2].equalsIgnoreCase("toggle")) {

				if(args.length != 3) {
					Message.sendInvalidArguments(args);
					return;
				}
				
				if(curMine.getProtection().contains(Protection.BLOCK_PLACE)) {
					curMine.getProtection().remove(Protection.BLOCK_PLACE);
					Message.sendNote(curMine.getName(), "Block placement protection has been turned " + ChatColor.RED + "off");
				}
				else {
					curMine.getProtection().add(Protection.BLOCK_PLACE);
					Message.sendNote(curMine.getName(), "Block placement protection has been turned " + ChatColor.GREEN + "on");
				}
			}
			else if(args[2].equalsIgnoreCase("whitelist")) {
				if(args.length != 3) {
					Message.sendInvalidArguments(args);
					return;
				}
				if(curMine.getPlaceBlacklist().getWhitelist()) {
					curMine.getPlaceBlacklist().setWhitelist(false);
					Message.sendNote(curMine.getName(), "Block placement whitelist is now " + ChatColor.RED + "off");
				}
				else {
					curMine.getPlaceBlacklist().setWhitelist(true);
					Message.sendNote(curMine.getName(), "Block placement whitelist is now " + ChatColor.GREEN + "on");
				}
			}
			else if(args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("+")) {
				if(args.length != 4) {
					Message.sendInvalidArguments(args);
					return;
				}
				MaterialData block = Util.getBlock(args[3]);
				
				if(block == null) {
					Message.sendBlockDoesNotExist(args[3]);
					return;
				}
				
				List<MaterialData> blockList = curMine.getPlaceBlacklist().getBlocks();
				blockList.add(block);
				curMine.getPlaceBlacklist().setBlocks(blockList);
				
				Message.sendNote(curMine.getName(), ChatColor.GREEN + block.getItemType().toString().toLowerCase().replace("_", " ") + ChatColor.WHITE + " was added to the block placement protection blacklist");
			}
			else if(args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("-")) {
				if(args.length != 4) {
					Message.sendInvalidArguments(args);
					return;
				}
				MaterialData block = Util.getBlock(args[3]);
				
				if(block == null) {
					Message.sendBlockDoesNotExist(args[3]);
					return;
				}
				
				List<MaterialData> blockList = curMine.getPlaceBlacklist().getBlocks();
				
				if(blockList.indexOf(block) == -1) {
					Message.sendError("There is no '" + args[3] + "' in place protection blacklist of mine '" + curMine.getName() + "'");
					return;
				}
				blockList.remove(block);
				curMine.getPlaceBlacklist().setBlocks(blockList);

				Message.sendNote(curMine.getName(), ChatColor.RED + block.getItemType().toString().toLowerCase().replace("_", " ") + ChatColor.WHITE + " was removed from the block placement protection blacklist");
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
		
		MineUtils.save(curMine);
		return;
	}
}
