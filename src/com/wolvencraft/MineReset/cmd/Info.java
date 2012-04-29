package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Regions;

public class Info
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("info"))
		{
			Util.sendDenied(args);
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
			Util.sendInvalid(args);
			return;
		}
		
		if(args.length != 1 && !Util.mineExists(args[1]))
		{
			Util.sendError("Mine '" + args[1] + "' does not exist. Use " + ChatColor.GREEN + "/mine help" + ChatColor.WHITE + " for help");
			return;
		}
		
		if(mineName == null) mineName = args[1];
		
		
		
		// Title
		String displayName = Regions.getString("mines." + mineName + ".display-name");
		if(displayName.equals("")) displayName = mineName;
		Util.sendMessage(ChatColor.DARK_RED + "                    -=[ " + displayName + " ]=-");
		
		// Reset
		boolean autoReset = Regions.getBoolean("mines." + mineName + ".reset.auto.reset");
		int autoResetTime = Regions.getInt("mines." + mineName + ".reset.auto.reset-time");
		int nextResetMin = Regions.getInt("mines." + mineName + ".reset.auto.data.min");
		int nextResetSec = Regions.getInt("mines." + mineName + ".reset.auto.data.sec");
		
		if(autoReset)
		{
			Util.sendMessage(" Resets every " + ChatColor.GOLD +  autoResetTime + ChatColor.WHITE + " minutes. Next reset in " + ChatColor.GOLD + nextResetMin + ChatColor.WHITE + " minutes " + ChatColor.GOLD + nextResetSec + ChatColor.WHITE + " seconds");
		}
		else
		{
			Util.sendMessage("The mine has to be reset manually");
		}
		
		// Protection
		String pvp;
		if(Regions.getBoolean("mines" + mineName + ".protection.pvp.enabled"))
			pvp = ChatColor.GREEN + "ON";
		else
			pvp = ChatColor.RED + "OFF";
		
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
		
		Util.sendMessage(ChatColor.BLUE + " Protection:");
		Util.sendMessage(" PVP: " + pvp + ChatColor.WHITE + " | Breaking: " + breakingProt + ChatColor.WHITE + " | Placement: " + placementProt + ChatColor.WHITE + " ");
		
		// Composition
		List<String> itemList = Regions.getList("mines." + mineName + ".materials.blocks");
		List<String> weightList = Regions.getList("mines." + mineName + ".materials.weights");
		
		String blockName;
		String blockWeight;
		int blockID;
		
		Util.sendMessage(ChatColor.BLUE + " Composition:");
		for(int i = 0; i < itemList.size(); i ++)
		{
			blockID = Integer.parseInt(itemList.get(i));
			blockName = Material.getMaterial(blockID).toString();
			if(Double.parseDouble(weightList.get(i)) < 10)
				blockWeight = " " + weightList.get(i);
			else
				blockWeight = weightList.get(i);
			Util.sendMessage(" - " + blockWeight + "% " + ChatColor.GREEN + blockName.toLowerCase().replace("_", " "));
		}
		
		// Blacklist
		List<String> blacklistBlocks =  Regions.getList("mines." + mineName + ".blacklist.blocks");
		if(Regions.getBoolean("mines." + mineName + ".blacklist.enabled") && blacklistBlocks.size() != 0)
		{
			if(Regions.getBoolean("mines." + mineName + ".blacklist.whitelist"))
			{
				Util.sendMessage(ChatColor.BLUE + " Whitelist:");
			}
			else Util.sendMessage(ChatColor.BLUE + " Blacklist:");
			
			for(String block : blacklistBlocks)
			{
				Util.sendMessage(" - " + Material.getMaterial(Integer.parseInt(block)).toString().toLowerCase().replace("_", " "));
			}
		}
		return;
	}
}
