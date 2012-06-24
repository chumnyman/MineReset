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
			
			// Title
			String displayName;
			if(curMine.getDisplayName().isEmpty())
				displayName = curMine.getName();
			else
				displayName = curMine.getDisplayName();
			ChatUtil.sendMessage(ChatColor.DARK_RED + "                             -=[ " + ChatColor.GREEN + ChatColor.BOLD + displayName + ChatColor.DARK_RED + " ]=-");
			
			
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
				String autoResetFormatted = Util.parseSeconds(curMine.getResetPeriod());
				
				String nextResetFormatted = Util.parseSeconds((int)curMine.getNextAutomaticResetTick() / 20);
				
				
				if(curMine.getAutomatic())
					ChatUtil.sendMessage(" Resets every " + ChatColor.GOLD +  autoResetFormatted + ChatColor.WHITE + " minutes. Next reset in " + ChatColor.GOLD + nextResetFormatted + ChatColor.WHITE + " minutes");
				else
					ChatUtil.sendMessage("The mine has to be reset manually");
				
				String generatorString =" Generator: " + ChatColor.GOLD +  curMine.getGenerator().toString();
				if(!parentMine.equals(curMine) && !parentMine.equals(null))
					generatorString = generatorString + ChatColor.WHITE + " | Linked to " + ChatColor.GOLD + parentMine.getName();
				ChatUtil.sendMessage(generatorString);
				
				String blockBreak, blockPlace;
				// Protection
				if(curMine.getProtection().contains(Protection.BLOCK_BREAK))
					blockBreak = ChatColor.GREEN + "ON";
				else
					blockBreak = ChatColor.RED + "OFF";
				
				if(curMine.getProtection().contains(Protection.BLOCK_PLACE))
					blockPlace = ChatColor.GREEN + "ON";
				else
					blockPlace = ChatColor.RED + "OFF";
				
				String pvpProtection;
				if(curMine.getProtection().contains(Protection.PVP))
					pvpProtection = ChatColor.GREEN + "ON";
				else pvpProtection = ChatColor.RED + "OFF";
				
				ChatUtil.sendMessage(ChatColor.BLUE + " Protection:");
				ChatUtil.sendMessage("Breaking: " + blockBreak + ChatColor.WHITE + " | Placement: " + blockPlace + ChatColor.WHITE + " | PVP " + pvpProtection);
				
				List<String> finalList = MineUtil.getSortedList(curMine);
				
				ChatUtil.sendMessage(ChatColor.BLUE + " Composition:");
				for(String line : finalList)
					ChatUtil.sendMessage(line);
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
