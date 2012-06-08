package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Protection;
import com.wolvencraft.MineReset.util.MineUtils;
import org.bukkit.ChatColor;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class InfoCommand
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("info"))
		{
			Message.sendDenied(args);
			return;
		}
		
		Mine curMine = null;
		if(args.length == 1)
		{
			if(CommandManager.getMine() != null)
			{
				curMine = CommandManager.getMine();
			}
			else
			{
				HelpCommand.getInfo();
				return;
			}
		}
		if(args.length > 2)
		{
			Message.sendInvalid(args);
			return;
		}
		
		if(args.length != 1 && MineUtils.getMine(args[1]) == null)
		{
			Message.sendError("Mine '" + args[1] + "' does not exist. Use " + ChatColor.GREEN + "/mine help" + ChatColor.WHITE + " for help");
			return;
		}
		
		
		if(args[1].equalsIgnoreCase("info") || args[1].equalsIgnoreCase("?"))
		{
			// Title
			String displayName = curMine.getDisplayName();
			if(displayName.equals("")) displayName = curMine.getName();
			Message.sendMessage(ChatColor.DARK_RED + "                             -=[ " + ChatColor.GREEN + ChatColor.BOLD + displayName + ChatColor.RED + " ]=-");
			
			// Reset
			Mine parentMine = curMine.getParent();
			if(parentMine == null)
				parentMine = curMine;
			
			
			String autoResetFormatted = curMine.getResetPeriod() / 60 + ":";
			if(curMine.getResetPeriod() % 60 < 10)
				autoResetFormatted = autoResetFormatted + "0" + curMine.getResetPeriod() % 60;
			else
				autoResetFormatted = autoResetFormatted + curMine.getResetPeriod() % 60;
			
			int nextResetTime = (int) ((parentMine.getNextAutomaticResetTick() - parentMine.getWorld().getTime()) / 20);
			String nextResetFormatted = nextResetTime / 60 + ":";
			if(nextResetTime % 60 < 10)
				nextResetFormatted = nextResetFormatted + "0" + nextResetTime % 60;
			else
				nextResetFormatted = nextResetFormatted + nextResetTime % 60;
			
			
			if(curMine.getAutomatic())
			{
				Message.sendMessage(" Resets every " + ChatColor.GOLD +  autoResetFormatted + ChatColor.WHITE + " minutes. Next reset in " + ChatColor.GOLD + nextResetFormatted + ChatColor.WHITE + " minutes");
			}
			else
			{
				Message.sendMessage("The mine has to be reset manually");
			}
			
			// generator
			//String generatorString = curMine.getGenerator();
			//if(parentMine != mineName)
			//	generatorString = generatorString + ChatColor.WHITE + " | Linked to " + ChatColor.GOLD + parentMine;
			//Message.sendMessage(generatorString);
			
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
			
			Message.sendMessage(ChatColor.BLUE + " Protection:");
			Message.sendMessage("Breaking: " + blockBreak + ChatColor.WHITE + " | Placement: " + blockPlace + ChatColor.WHITE + " | PVP " + pvpProtection);
			
			List<String> finalList = MineUtils.getSortedList(curMine);
			
			Message.sendMessage(ChatColor.BLUE + " Composition:");
			for(String line : finalList)
			{
				Message.sendMessage(line);
			}
			
			// Blacklist
			//List<String> blacklistBlocks =  Regions.getList("mines." + mineName + ".blacklist.blocks");
			//if(Regions.getBoolean("mines." + mineName + ".blacklist.enabled") && blacklistBlocks.size() != 0)
			//{
			//	if(Regions.getBoolean("mines." + mineName + ".blacklist.whitelist"))
			//	{
			//		Message.sendMessage(ChatColor.BLUE + " Whitelist:");
			//	}
			//	else Message.sendMessage(ChatColor.BLUE + " Blacklist:");
			//	
			//	for(String block : blacklistBlocks)
			//	{
			//		Message.sendMessage(" - " + Material.getMaterial(Integer.parseInt(block)).toString().toLowerCase().replace("_", " "));
			//	}
			//}
			return;
		}
		if(args[1].equalsIgnoreCase("time"))
		{
			String displayName = curMine.getDisplayName();
			if(displayName.equals("")) displayName = curMine.getName();
			
			// Reset
			Mine parent = curMine.getParent();
			if(parent == null)
				parent = curMine;
			
			int autoResetTime = parent.getResetPeriod();
			String autoResetFormatted = autoResetTime / 60 + ":";
			if(autoResetTime % 60 < 10)
				autoResetFormatted = autoResetFormatted + "0" + autoResetTime % 60;
			else
				autoResetFormatted = autoResetFormatted + autoResetTime % 60;
			
			//TODO Fix this!
            int nextResetTime = (int) ((parent.getNextAutomaticResetTick() - parent.getWorld().getTime()) / 20);
            String nextResetFormatted = nextResetTime / 60 + ":";
			if(nextResetTime % 60 < 10)
				nextResetFormatted = nextResetFormatted + "0" + nextResetTime % 60;
			else
				nextResetFormatted = nextResetFormatted + nextResetTime % 60;
			
			if(curMine.getAutomatic())
			{
				Message.sendSuccess(displayName + " resets every " + ChatColor.GOLD +  autoResetFormatted + ChatColor.WHITE + " minutes. Next reset in " + ChatColor.GOLD + nextResetFormatted + ChatColor.WHITE + " minutes.");
			}
			else
			{
				Message.sendSuccess(displayName + " has to be reset manually");
			}
		}
	}
}
