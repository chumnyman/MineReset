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
		Message.sendMessage(ChatColor.DARK_RED + "                    -=[ " + ChatColor.GREEN + ChatColor.BOLD + displayName + ChatColor.RED + " ]=-");
		
		// Reset
		boolean autoReset = Regions.getBoolean("mines." + mineName + ".reset.auto.reset");
		int autoResetTime = Regions.getInt("mines." + mineName + ".reset.auto.reset-time");
		int nextResetMin = Regions.getInt("mines." + mineName + ".reset.auto.data.min");
		int nextResetSec = Regions.getInt("mines." + mineName + ".reset.auto.data.sec");
		
		if(autoReset)
		{
			Message.sendMessage(" Resets every " + ChatColor.GOLD +  autoResetTime + ChatColor.WHITE + " minutes. Next reset in " + ChatColor.GOLD + nextResetMin + ChatColor.WHITE + " minutes " + ChatColor.GOLD + nextResetSec + ChatColor.WHITE + " seconds");
		}
		else
		{
			Message.sendMessage("The mine has to be reset manually");
		}
		
		// generator
		Message.sendMessage(" Generator: " + ChatColor.GOLD + "DEFAULT");
		
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
		
		Message.sendMessage(ChatColor.BLUE + " Protection:");
		Message.sendMessage("Breaking: " + breakingProt + ChatColor.WHITE + " | Placement: " + placementProt + ChatColor.WHITE + " ");
		
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
