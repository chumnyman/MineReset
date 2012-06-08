package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Protection;
import com.wolvencraft.MineReset.util.Message;
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
			Message.sendInvalid(args);
		}
		
		Mine curMine = CommandManager.getMine();
		if(curMine == null) {
			Message.sendError(Language.getString("general.mine-not-selected"));
			return;
		}
		
		if(args[1].equalsIgnoreCase("pvp")) {
			if(args.length != 2) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			if(curMine.getProtection().contains(Protection.PVP)) {
				curMine.getProtection().remove(Protection.PVP);
				Message.sendSuccess("PVP has been turned OFF for mine '" + curMine.getName() + "'");
			}
			else {
				curMine.getProtection().add(Protection.PVP);
				Message.sendSuccess("PVP has been turned ON for mine '" + curMine.getName() + "'");
			}
			return;
		}
		else if(args[1].equalsIgnoreCase("breaking")) {
			if(args.length < 3) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			if(args[2].equalsIgnoreCase("toggle")) {

				if(args.length != 3) {
					Message.sendError("Invalid parameters. Check your argument count!");
					return;
				}
				
				if(curMine.getProtection().contains(Protection.BLOCK_BREAK))
				{
					curMine.getProtection().remove(Protection.BLOCK_BREAK);
					Message.sendSuccess("Mine protection has been turned off for mine '" + curMine.getName() + "'");
				}
				else
				{
					curMine.getProtection().add(Protection.BLOCK_BREAK);
					Message.sendSuccess("Mine breaking protection has been turned on for mine '" + curMine.getName() + "'");
				}
				return;
			}
			else if(args[2].equalsIgnoreCase("blacklist")) {
				if(args.length < 3) {
					Message.sendError("Invalid parameters. Check your argument count!");
					return;
				}
				if(args[3].equalsIgnoreCase("toggle"))
				{
					if(args.length != 3) {
						Message.sendError("Invalid parameters. Check your argument count!");
						return;
					}
					
					if(curMine.getBreakBlacklist().getEnabled()) {
						curMine.getBreakBlacklist().setEnabled(false);
						Message.sendSuccess("Block breaking protection blacklist has been turned off for mine '" + curMine.getName() + "'");
					}
					else {
						curMine.getBreakBlacklist().setEnabled(true);
						Message.sendSuccess("Block breaking protection blacklist has been turned on for mine '" + curMine.getName() + "'");
					}
					
				}
				else if(args[3].equalsIgnoreCase("whitelist")) {
					if(args.length != 3) {
						Message.sendError("Invalid parameters. Check your argument count!");
						return;
					}
					if(curMine.getBreakBlacklist().getWhitelist()) {
						curMine.getBreakBlacklist().setWhitelist(false);
						Message.sendSuccess("Block breaking blacklist is no longer treated as a whitelist for mine '" + curMine.getName() + "'");
					}
					else {
						curMine.getBreakBlacklist().setWhitelist(true);
						Message.sendSuccess("Block breaking blacklist is now treated as a whitelist for mine '" + curMine.getName() + "'");
					}
				}
				else if(args[3].equalsIgnoreCase("add") || args[3].equalsIgnoreCase("+")) {
					if(args.length != 4) {
						Message.sendError("Invalid parameters. Check your argument count!");
						return;
					}
					MaterialData block = Util.getBlock(args[4]);
					
					if(block == null) {
						Message.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<MaterialData> blockList = curMine.getBreakBlacklist().getBlocks();
					blockList.add(block);
					curMine.getBreakBlacklist().setBlocks(blockList);
					
					Message.sendSuccess(block.getItemType().toString().toLowerCase().replace("_", " ") + " was added to the block breaking protection blacklist of '" + curMine.getName() + "'");
					return;
				}
				else if(args[3].equalsIgnoreCase("remove") || args[3].equalsIgnoreCase("-")) {
					if(args.length != 4) {
						Message.sendError("Invalid parameters. Check your argument count!");
						return;
					}
					MaterialData block = Util.getBlock(args[4]);
					
					if(block == null) {
						Message.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<MaterialData> blockList = curMine.getBreakBlacklist().getBlocks();
					
					if(blockList.indexOf(block) == -1) {
						Message.sendError("There is no '" + args[4] + "' in protection blacklist of mine '" + curMine.getName() + "'");
						return;
					}
					blockList.remove(block);
					curMine.getBreakBlacklist().setBlocks(blockList);

					Message.sendSuccess(args[4] + " was successfully removed from block breaking protection of mine '" + curMine.getName() + "'");
					return;
				}
				else {
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
		else if(args[1].equalsIgnoreCase("placement")) {
			if(args.length < 3) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			if(args[2].equalsIgnoreCase("toggle")) {

				if(args.length != 3) {
					Message.sendError("Invalid parameters. Check your argument count!");
					return;
				}
				
				if(curMine.getProtection().contains(Protection.BLOCK_PLACE))
				{
					curMine.getProtection().remove(Protection.BLOCK_PLACE);
					Message.sendSuccess("Mine protection has been turned off for mine '" + curMine.getName() + "'");
				}
				else
				{
					curMine.getProtection().add(Protection.BLOCK_PLACE);
					Message.sendSuccess("Mine placement protection has been turned on for mine '" + curMine.getName() + "'");
				}
				return;
			}
			else if(args[2].equalsIgnoreCase("blacklist")) {
				if(args.length < 3) {
					Message.sendError("Invalid parameters. Check your argument count!");
					return;
				}
				if(args[3].equalsIgnoreCase("toggle"))
				{
					if(args.length != 3) {
						Message.sendError("Invalid parameters. Check your argument count!");
						return;
					}
					
					if(curMine.getPlaceBlacklist().getEnabled()) {
						curMine.getPlaceBlacklist().setEnabled(false);
						Message.sendSuccess("Block placement protection blacklist has been turned off for mine '" + curMine.getName() + "'");
					}
					else {
						curMine.getPlaceBlacklist().setEnabled(true);
						Message.sendSuccess("Block placement protection blacklist has been turned on for mine '" + curMine.getName() + "'");
					}
					
				}
				else if(args[3].equalsIgnoreCase("whitelist")) {
					if(args.length != 3) {
						Message.sendError("Invalid parameters. Check your argument count!");
						return;
					}
					if(curMine.getPlaceBlacklist().getWhitelist()) {
						curMine.getPlaceBlacklist().setWhitelist(false);
						Message.sendSuccess("Block placement blacklist is no longer treated as a whitelist for mine '" + curMine.getName() + "'");
					}
					else {
						curMine.getPlaceBlacklist().setWhitelist(true);
						Message.sendSuccess("Block placement blacklist is now treated as a whitelist for mine '" + curMine.getName() + "'");
					}
				}
				else if(args[3].equalsIgnoreCase("add") || args[3].equalsIgnoreCase("+")) {
					if(args.length != 4) {
						Message.sendError("Invalid parameters. Check your argument count!");
						return;
					}
					MaterialData block = Util.getBlock(args[4]);
					
					if(block == null) {
						Message.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<MaterialData> blockList = curMine.getPlaceBlacklist().getBlocks();
					blockList.add(block);
					curMine.getPlaceBlacklist().setBlocks(blockList);
					
					Message.sendSuccess(block.getItemType().toString().toLowerCase().replace("_", " ") + " was added to the block placement protection blacklist of '" + curMine.getName() + "'");
					return;
				}
				else if(args[3].equalsIgnoreCase("remove") || args[3].equalsIgnoreCase("-")) {
					if(args.length != 4) {
						Message.sendError("Invalid parameters. Check your argument count!");
						return;
					}
					MaterialData block = Util.getBlock(args[4]);
					
					if(block == null) {
						Message.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<MaterialData> blockList = curMine.getPlaceBlacklist().getBlocks();
					
					if(blockList.indexOf(block) == -1) {
						Message.sendError("There is no '" + args[4] + "' in protection blacklist of mine '" + curMine.getName() + "'");
						return;
					}
					blockList.remove(block);
					curMine.getPlaceBlacklist().setBlocks(blockList);

					Message.sendSuccess(args[4] + " was successfully removed from block placement protection of mine '" + curMine.getName() + "'");
					return;
				}
				else {
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
		else
		{
			Message.sendInvalid(args);
			return;
		}
	}
}
