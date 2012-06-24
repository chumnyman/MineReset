package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;

public class BlacklistCommand {
	public static void run(String[] args) {
		if(!Util.hasPermission("edit.blacklist")) {
			ChatUtil.sendDenied(args);
			return;
		}
		
		if(args.length == 1) {
			HelpCommand.getBlacklist();
			return;
		}
		else if(args.length > 3) {
			ChatUtil.sendInvalid(args);
			return;
		}
		
		Mine curMine = CommandManager.getMine();
		if(curMine == null) {
			ChatUtil.sendMessage(Language.getString("error.mine-name-invalid").replaceAll("%MINE%", args[1]));
			return;
		}
		
		if(args[1].equalsIgnoreCase("toggle")) {
			if(args.length != 1) {
				ChatUtil.sendMessage(Language.getString("error.invalid-arguments"));
				return;
			}
			if(curMine.getBlacklist().getEnabled()) {
				curMine.getBlacklist().setEnabled(false);
				ChatUtil.sendNote(curMine.getName(), "Blacklist turned " + ChatColor.RED + "off");
			}
			else
			{
				curMine.getBlacklist().setEnabled(true);
				ChatUtil.sendNote(curMine.getName(), "Blacklist turned " + ChatColor.GREEN + "on");
			}
		}
		else if(args[1].equalsIgnoreCase("whitelist")) {
			if(curMine.getBlacklist().getWhitelist()) {
				curMine.getBlacklist().setWhitelist(false);
				ChatUtil.sendNote(curMine.getName() + " Blacklist", "Whitelist mode " + ChatColor.RED + "off");
			}
			else {
				curMine.getBlacklist().setWhitelist(true);
				ChatUtil.sendNote(curMine.getName() + " Blacklist", "Whitelist mode " + ChatColor.GREEN + "on");
			}
		}
		else if(args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("+")) {
			if(args.length != 3) {
				ChatUtil.sendMessage(Language.getString("error.invalid-arguments"));
				return;
			}
			
			MaterialData block = Util.getBlock(args[2]);
			if(block == null) {
				ChatUtil.sendError("Block '" + ChatColor.RED + args[2] + ChatColor.WHITE + "' does not exist");
				return;
			}
			
			List<MaterialData> blocks = curMine.getBlacklist().getBlocks();
			blocks.add(block);
			curMine.getBlacklist().setBlocks(blocks);
			
			ChatUtil.sendNote(curMine.getName(), ChatColor.GREEN + Util.parseMaterialData(block) + ChatColor.WHITE + " has been added to the blacklistlist");
		}
		else if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("-")) {
			if(args.length != 3) {
				ChatUtil.sendMessage(Language.getString("error.invalid-arguments"));
				return;
			}
			
			MaterialData block = Util.getBlock(args[2]);
			if(block == null) {
				ChatUtil.sendError("Block '" + ChatColor.RED + args[2] + ChatColor.WHITE + "' does not exist");
				return;
			}
			
			List<MaterialData> blocks = curMine.getBlacklist().getBlocks();
			blocks.remove(block);
			curMine.getBlacklist().setBlocks(blocks);
			ChatUtil.sendNote(curMine.getName(), ChatColor.GREEN + Util.parseMaterialData(block) + ChatColor.WHITE + " has been removed from the list");
		}
		else {
			ChatUtil.sendInvalid(args);
			return;
		}
		
		MineUtil.save(curMine);
		return;
	}
}
