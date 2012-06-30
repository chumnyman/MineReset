package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Protection;
import com.wolvencraft.MineReset.mine.SignClass;
import com.wolvencraft.MineReset.util.MineUtil;
import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.Util;

public class InfoCommand
{
	public static void run(String[] args)
	{
		Mine curMine = null;
		if(args.length == 1) {
			if(CommandManager.getMine() != null) {
				curMine = CommandManager.getMine();
			}
			else {
				HelpCommand.getInfo();
				return;
			}
		}
		else {
			curMine = MineUtil.getMine(args[1]);
		}
		
		if(curMine == null) {
			ChatUtil.sendInvalidMineName(args[1]);
			return;
		}
		
		if(args.length > 3) {
			ChatUtil.sendInvalidArguments(args);
			return;
		}
		
		
		if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("?")) {

			if(!Util.hasPermission("info.all")) {
				ChatUtil.sendDenied(args);
				return;
			}
			

			// SPACING INFO - IMPORTANT
			// 51 symbols
			// 87 spaces
			// 0.586206897 | 1.70588235
			//ChatUtil.sendMessage("|                                                                              |");
			//ChatUtil.sendMessage("/---------------------------------------------------\\");			
			
			// Reset
			Mine parentMine = MineUtil.getMine(curMine.getParent());
			if(parentMine == null)
				parentMine = curMine;
			
			if(args.length == 3)
			{
				if(args[2].equalsIgnoreCase("blacklist") || args[2].equalsIgnoreCase("bl")) {
					boolean enabled = curMine.getBlacklist().getEnabled();
					boolean whitelist = curMine.getBlacklist().getWhitelist();
					List<MaterialData> blocks = curMine.getBlacklist().getBlocks();
					
					String str = "Blacklist: ";
					if(enabled) str = str + ChatColor.GREEN + "on";
					else str = str + ChatColor.RED + "off";
					str = str + ChatColor.WHITE + " | Whitelist: ";
					if(whitelist) str = str + ChatColor.GREEN + "on";
					else str = str + ChatColor.RED + "off";
					ChatUtil.sendMessage(str);
					ChatUtil.sendMessage(ChatColor.BLUE + " Composition");
					for(MaterialData block : blocks) {
						String[] parts = {block.getItemTypeId() + "", block.getData() + ""};
						ChatUtil.sendMessage(" - " + Util.parseMetadata(parts, true) + " " + Util.parseMaterial(block.getItemType()));
					}
				}
				else if(args[2].equalsIgnoreCase("protection") || args[2].equalsIgnoreCase("pt")) {
					boolean pvp = curMine.getProtection().contains(Protection.PVP);
					boolean blbreak = curMine.getProtection().contains(Protection.BLOCK_BREAK);
					boolean blplace = curMine.getProtection().contains(Protection.BLOCK_PLACE);
					String str = "Block breaking protection: ";
					if(blbreak) str = str + ChatColor.GREEN + "Enabled";
					else str = str + ChatColor.RED + "Disabled";
					
					str = str + ChatColor.WHITE + " | Block placement protection: ";
					if(blplace) str = str + ChatColor.GREEN + "Enabled";
					else str = str + ChatColor.RED + "Disabled";
					ChatUtil.sendMessage(str);
					
					str = "PVP protection: ";
					if(pvp) str = str + ChatColor.GREEN + "Enabled";
					else str = str + ChatColor.RED + "Disabled";
					ChatUtil.sendMessage(str);
					return;
				}
				else if(args[2].equalsIgnoreCase("sign")) {
					ChatUtil.sendMessage("Signs associated with this mine: ");
					List<SignClass> signs = MineReset.getSigns();
					for(SignClass sign : signs) {
						if(sign.getParent().equals(curMine))
							ChatUtil.sendMessage(" - " + sign.getLocation().getBlockX() + ", " + sign.getLocation().getBlockY() + ", " + sign.getLocation().getBlockZ());
					}
					return;
				}
				else if(args[2].equalsIgnoreCase("reset")) {
					String autoResetFormatted = Util.parseSeconds(curMine.getResetPeriod());
					String nextResetFormatted = Util.parseSeconds((int)curMine.getNextAutomaticResetTick() / 20);
					
					if(curMine.getAutomatic()) {
						ChatUtil.sendMessage("Mine resets every " + ChatColor.GOLD +  autoResetFormatted + ChatColor.WHITE + " minutes. Next reset in " + ChatColor.GOLD + nextResetFormatted + ChatColor.WHITE + " minutes.");
						ChatUtil.sendMessage("Warnings are send as follows: ");
						List<Integer> warnings = curMine.getWarningTimes();
						for(int time : warnings) {
							ChatUtil.sendMessage(" - " + Util.parseSeconds(time));
						}
					}
					else {
						ChatUtil.sendMessage("Mine has to be reset manually");
					}
					return;
				}
				else
				{
					ChatUtil.sendInvalid(args);
					return;
				}
			}
			else {
				
				// Title
				String displayName = curMine.getDisplayName();
				

				List<String> finalList = MineUtil.getSortedList(curMine);
				
				if(displayName.isEmpty()) displayName = curMine.getName();
				ChatUtil.sendMessage("");
				String displayString = "---==[ " + ChatColor.GREEN + ChatColor.BOLD + displayName + ChatColor.WHITE + " ]==---";
				for(int i = 0; i < 25 - (displayName.length() / 2); i++)
					displayString = " " + displayString;
				ChatUtil.sendMessage(displayString);
				ChatUtil.sendMessage("");
				
				String str = "    [ ";
				if(curMine.getProtection().contains(Protection.BLOCK_BREAK)) {
					if(curMine.getBreakBlacklist().getWhitelist()) str += ChatColor.YELLOW;
					else str += ChatColor.GREEN;
				}
				else str += ChatColor.RED;
				str += "Block Breaking" + ChatColor.WHITE + " ]     [ ";
				if(curMine.getProtection().contains(Protection.PVP)) str += ChatColor.GREEN;
				else str += ChatColor.RED;
				str += "PVP" + ChatColor.WHITE + " ]    [ ";
				if(curMine.getProtection().contains(Protection.BLOCK_PLACE)) {
					if(curMine.getPlaceBlacklist().getWhitelist()) str += ChatColor.YELLOW;
					else str += ChatColor.GREEN;
				}
				else str += ChatColor.RED;
				str += "Block Placement" + ChatColor.WHITE + " ]";
				
				ChatUtil.sendMessage(str);
				if(curMine.getAutomatic())
					ChatUtil.sendMessage("    Resets every ->  " + ChatColor.GREEN + Util.parseSeconds(curMine.getResetPeriod()) + "    " + ChatColor.GOLD + Util.parseSeconds((int)curMine.getNextAutomaticResetTick() / 20) + ChatColor.WHITE + "  <- Next Reset");
				str = "    Generator: " + ChatColor.GOLD + curMine.getGenerator();
				String parentName;
				if(curMine.getParent() == null || curMine.getParent().equals(curMine.getName()))
					parentName = "none";
				else parentName = curMine.getParent();
				for(int i = 0; i < (25 - parentName.length()); i++)
					str += " ";
				str += "Linked to: " + parentName;
				ChatUtil.sendMessage(str);
				
				for(int i = 0; i < (finalList.size() - 1); i += 2) {
					int spaces = 10;
					String line = finalList.get(i);
					if(line.length() > 25) spaces -= (line.length() - 25);
					else if(line.length() < 25) spaces += (25 - line.length());
					
					str = "        " + line;
					for(int j = 0; j < spaces; j++) str += " ";
					str += finalList.get(i + 1);
					ChatUtil.sendMessage(str);
				}
				if(finalList.size() % 2 != 0) ChatUtil.sendMessage("        " + finalList.get(finalList.size() - 1));
				ChatUtil.sendMessage("");
				return;
			}
		}
		if(args[0].equalsIgnoreCase("time")) {
			if(!Util.hasPermission("info.time")) {
				ChatUtil.sendDenied(args);
				return;
			}
			
			
			if(args.length != 2) {
				ChatUtil.sendInvalidArguments(args);
				return;
			}
			String displayName = curMine.getDisplayName();
			if(displayName.equals("")) displayName = curMine.getName();
			
			// Reset
			Mine parentMine = MineUtil.getMine(curMine.getParent());
			if(parentMine == null)
				parentMine = curMine;
			
			String autoResetFormatted = Util.parseSeconds(curMine.getResetPeriod());
			String nextResetFormatted = Util.parseSeconds((int)curMine.getNextAutomaticResetTick() / 20);
			
			if(curMine.getAutomatic()) {
				ChatUtil.sendSuccess(displayName + " resets every " + ChatColor.GOLD +  autoResetFormatted + ChatColor.WHITE + " minutes. Next reset in " + ChatColor.GOLD + nextResetFormatted + ChatColor.WHITE + " minutes.");
			}
			else {
				ChatUtil.sendSuccess(displayName + " has to be reset manually");
			}
		}
	}
}