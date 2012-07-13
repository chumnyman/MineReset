package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineError;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;

public class BlacklistCommand {
	public static void run(String[] args) {
		if(!Util.hasPermission("edit.blacklist")) {
			ChatUtil.sendInvalid(MineError.ACCESS, args);
			return;
		}

		if(args.length == 1) {
			getHelp();
			return;
		}
		
		Mine curMine = CommandManager.getMine();
		if(curMine == null) {
			ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
			return;
		}
		
		if(args[1].equalsIgnoreCase("toggle")) {
			if(args.length != 2) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			if(curMine.getBlacklist().getEnabled()) {
				curMine.getBlacklist().setEnabled(false);
				ChatUtil.sendNote(curMine.getName(), "Blacklist turned " + ChatColor.RED + "off");
			}
			else {
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
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			
			MaterialData block = Util.getBlock(args[2]);
			if(block == null) {
				ChatUtil.sendInvalid(MineError.INVALID_BLOCK, args, args[2]);
				return;
			}
			
			List<MaterialData> blocks = curMine.getBlacklist().getBlocks();
			blocks.add(block);
			curMine.getBlacklist().setBlocks(blocks);
			ChatUtil.sendNote(curMine.getName(), ChatColor.GREEN + Util.parseMaterialData(block) + ChatColor.WHITE + " has been added to the blacklistlist");
		}
		else if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("-")) {
			if(args.length != 3) {
				ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
				return;
			}
			
			MaterialData block = Util.getBlock(args[2]);
			if(block == null) {
				ChatUtil.sendInvalid(MineError.INVALID_BLOCK, args, args[2]);
				return;
			}
			
			List<MaterialData> blocks = curMine.getBlacklist().getBlocks();
			blocks.remove(block);
			curMine.getBlacklist().setBlocks(blocks);
			ChatUtil.sendNote(curMine.getName(), ChatColor.GREEN + Util.parseMaterialData(block) + ChatColor.WHITE + " has been removed from the list");
		}
		else {
			ChatUtil.sendInvalid(MineError.INVALID, args);
			return;
		}
		
		MineUtil.save(curMine);
		return;
	}
	
	public static void getHelp() {
		ChatUtil.formatHeader(20, "Blacklist");
		ChatUtil.formatHelp("blacklist", "toggle", "Enables the use of blacklist for the mine");
		ChatUtil.formatHelp("blacklist", "whitelist", "Should the blacklist be treated as a whitelist?");
		ChatUtil.formatHelp("blacklist", "+ <block>", "Add <block> to the list");
		ChatUtil.formatHelp("blacklist", "- <block>", "Remove <block> from the list");
		return;
	}
}
