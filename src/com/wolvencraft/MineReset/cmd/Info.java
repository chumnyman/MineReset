package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Mine;
import com.wolvencraft.MineReset.util.Util;

public class Info
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("info"))
		{
			Message.sendDenied(args);
			return;
		}
		
		String mineName = null;
		if(args.length == 1)
		{
			if(CommandManager.getMine() != null)
			{
				mineName = CommandManager.getMine();
			}
			else
			{
				Help.getInfo();
				return;
			}
		}
		if(args.length > 2)
		{
			Message.sendInvalid(args);
			return;
		}
		
		if(args.length != 1 && !Mine.exists(args[1]))
		{
			Message.sendError("Mine '" + args[1] + "' does not exist. Use " + ChatColor.GREEN + "/mine help" + ChatColor.WHITE + " for help");
			return;
		}
		
		if(mineName == null) mineName = args[1];
		
		
		
		// Title
		String displayName = Regions.getString("mines." + mineName + ".display-name");
		if(displayName.equals("")) displayName = mineName;
		Message.sendMessage(ChatColor.DARK_RED + "                             -=[ " + ChatColor.GREEN + ChatColor.BOLD + displayName + ChatColor.RED + " ]=-");
		
		// Reset
		String parentMine = Regions.getString("mines." + mineName + ".parent");
		if(parentMine == null)
			parentMine = mineName;
		
		boolean autoReset = Regions.getBoolean("mines." + parentMine + ".reset.auto.reset");
		int autoResetTime = Regions.getInt("mines." + parentMine + ".reset.auto.reset-every");
		String autoResetFormatted = autoResetTime / 60 + ":";
		if(autoResetTime % 60 < 10)
			autoResetFormatted = autoResetFormatted + "0" + autoResetTime % 60;
		else
			autoResetFormatted = autoResetFormatted + autoResetTime % 60;
		
		int nextResetTime = Regions.getInt("mines." + parentMine + ".reset.auto.data.next");
		String nextResetFormatted = nextResetTime / 60 + ":";
		if(nextResetTime % 60 < 10)
			nextResetFormatted = nextResetFormatted + "0" + nextResetTime % 60;
		else
			nextResetFormatted = nextResetFormatted + nextResetTime % 60;
		
		
		if(autoReset)
		{
			Message.sendMessage(" Resets every " + ChatColor.GOLD +  autoResetFormatted + ChatColor.WHITE + " minutes. Next reset in " + ChatColor.GOLD + nextResetFormatted + ChatColor.WHITE + " minutes");
		}
		else
		{
			Message.sendMessage("The mine has to be reset manually");
		}
		
		// generator
		String generatorString = " Generator: " + ChatColor.GOLD + Regions.getString("mines." + mineName + ".reset.generator").toUpperCase();
		if(parentMine != mineName)
			generatorString = generatorString + ChatColor.WHITE + " | Linked to " + ChatColor.GOLD + parentMine;
		Message.sendMessage(generatorString);
		
		// Protection
		String breakingProt;
		if(Regions.getBoolean("mines." + mineName + ".protection.breaking.enabled"))
		{
			
			if(Regions.getBoolean("mines." + mineName + ".protection.breaking.blacklist.enabled"))
			{
				if(Regions.getBoolean("mines." + mineName + ".protection.breaking.blacklist.whitelist"))
					breakingProt = ChatColor.GREEN + "ON" + ChatColor.WHITE + " (W)";
				else
					breakingProt = ChatColor.GREEN + "ON" + ChatColor.WHITE + " (B)";
			}
			else
				breakingProt = ChatColor.GREEN + "ON";
		}
		else
		{
			breakingProt = ChatColor.RED + "OFF";
		}

		String placementProt;
		if(Regions.getBoolean("mines." + mineName + ".protection.placement.enabled"))
		{
			if(Regions.getBoolean("mines." + mineName + ".protection.placement.blacklist.enabled"))
			{
				if(Regions.getBoolean("mines." + mineName + ".protection.placement.blacklist.whitelist"))
					placementProt = ChatColor.GREEN + "ON" + ChatColor.WHITE + " (W)";
				else
					placementProt = ChatColor.GREEN + "ON" + ChatColor.WHITE + " (B)";
			}
			else
				placementProt = ChatColor.GREEN + "ON";
		}
		else
		{
			placementProt = ChatColor.RED + "OFF";
		}
		
		String pvpProt;
		if(Regions.getBoolean("mines." + mineName + ".protection.pvp.enabled"))
			pvpProt = ChatColor.GREEN + "ON";
		else pvpProt = ChatColor.RED + "OFF";
		
		Message.sendMessage(ChatColor.BLUE + " Protection:");
		Message.sendMessage("Breaking: " + breakingProt + ChatColor.WHITE + " | Placement: " + placementProt + ChatColor.WHITE + " | PVP " + pvpProt);
		
		List<String> finalList = Mine.getSortedList(mineName);
		
		Message.sendMessage(ChatColor.BLUE + " Composition:");
		for(String message : finalList)
		{			Message.sendMessage(message);
		}
		
		// Blacklist
		List<String> blacklistBlocks =  Regions.getList("mines." + mineName + ".blacklist.blocks");
		if(Regions.getBoolean("mines." + mineName + ".blacklist.enabled") && blacklistBlocks.size() != 0)
		{
			if(Regions.getBoolean("mines." + mineName + ".blacklist.whitelist"))
			{
				Message.sendMessage(ChatColor.BLUE + " Whitelist:");
			}
			else Message.sendMessage(ChatColor.BLUE + " Blacklist:");
			
			for(String block : blacklistBlocks)
			{
				Message.sendMessage(" - " + Material.getMaterial(Integer.parseInt(block)).toString().toLowerCase().replace("_", " "));
			}
		}
		return;
	}
}
