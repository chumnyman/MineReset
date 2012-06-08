package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class BlacklistCommand {
	public static void run(String[] args) {
		if(!Util.senderHasPermission("edit")) {
			Message.sendDenied(args);
			return;
		}
		
		if(args.length == 1) {
			HelpCommand.getBlacklist();
			return;
		}
		else if(args.length > 3) {
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
			if(curMine.getBlacklist().getEnabled()) {
				curMine.getBlacklist().setEnabled(false);
				Message.sendSuccess("Blacklist turned OFF for mine '" + curMine.getName() + "'");
			}
			else
			{
				curMine.getBlacklist().setEnabled(true);
				Message.sendSuccess("Blacklist turned ON for mine '" + curMine.getName() + "'");
			}
			Regions.saveData();
			return;
		}
		else if(args[1].equalsIgnoreCase("whitelist"))
		{
			if(curMine.getBlacklist().getWhitelist())
			{
				curMine.getBlacklist().setWhitelist(false);
				Message.sendSuccess("Blacklist is no longer treated as a whitelist for mine '" + curMine.getName() + "'");
			}
			else
			{
				curMine.getBlacklist().setWhitelist(true);
				Message.sendSuccess("Blacklist is now treated as a whitelist for mine '" + curMine.getName() + "'");
			}
			Regions.saveData();
			return;
		}
		else if(args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("+"))
		{
			if(args.length != 3) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			MaterialData block = Util.getBlock(args[2]);
			if(block == null)
			{
				Message.sendError("Block '"+ args[2] + "' does not exist");
				return;
			}
			
			List<MaterialData> blocks = curMine.getBlacklist().getBlocks();
			blocks.add(block);
			curMine.getBlacklist().setBlocks(blocks);
			
			Message.sendSuccess("Block " + ChatColor.GREEN + args[2] + ChatColor.WHITE + " is now in the blacklist");
			return;
		}
		else if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("-"))
		{
			if(args.length != 3) {
				Message.sendError("Invalid parameters. Check your argument count!");
				return;
			}
			
			MaterialData block = Util.getBlock(args[2]);
			if(block == null)
			{
				Message.sendError("Block '"+ args[2] + "' does not exist");
				return;
			}
			
			List<MaterialData> blocks = curMine.getBlacklist().getBlocks();
			blocks.remove(block);
			curMine.getBlacklist().setBlocks(blocks);
			Message.sendSuccess("Block '" + args[2] + "' has been removed from the blacklist");
			return;
		}
		else
		{
			Message.sendInvalid(args);
			return;
		}
	}
}
