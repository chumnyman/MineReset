package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class BlacklistCommand
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("edit"))
		{
			Message.sendDenied(args);
			return;
		}
		
		if(args.length == 1)
		{
			HelpCommand.getBlacklist();
			return;
		}
		else if(args.length > 3)
		{
			Message.sendInvalid(args);
			return;
		}
		
		String mineName = CommandManager.getMine();
		if(mineName == null)
		{
			String error = Language.getString("general.mine-not-selected");
			Message.sendError(error);
			return;
		}
		
		if(args[1].equalsIgnoreCase("toggle") || args[1].equalsIgnoreCase("tg"))
		{
			if(Regions.getBoolean("mines." + mineName + ".blacklist.enabled"))
			{
				Regions.setBoolean("mines." + mineName + ".blacklist.enabled", false);
				Message.sendSuccess("Blacklist turned OFF for mine '" + mineName + "'");
			}
			else
			{
				Regions.setBoolean("mines." + mineName + ".blacklist.enabled", true);
				Message.sendSuccess("Blacklist turned ON for mine '" + mineName + "'");
			}
			Regions.saveData();
			return;
		}
		else if(args[1].equalsIgnoreCase("whitelist") || args[1].equalsIgnoreCase("wl"))
		{
			if(Regions.getBoolean("mines." + mineName + ".blacklist.whitelist"))
			{
				Regions.setBoolean("mines." + mineName + ".blacklist.whitelist", false);
				Message.sendSuccess("Blacklist is no longer treated as a whitelist for mine '" + mineName);
			}
			else
			{
				Regions.setBoolean("mines." + mineName + ".blacklist.whitelist", true);
				Message.sendSuccess("Blacklist is now treated as a whitelist for mine '" + mineName + "'");
			}
			Regions.saveData();
			return;
		}
		else if(args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("+"))
		{
			MaterialData block = Util.getBlock(args[2]);
			if(block == null)
			{
				Message.sendError("Block '"+ args[2] + "' does not exist");
				return;
			}
			
			List<String> blacklist = Regions.getList("mines." + mineName + ".blacklist.blocks");
			blacklist.add(block.getItemTypeId() + ":" + block.getData());
			Regions.setList("mines." + mineName + ".blacklist.blocks", blacklist);

			Regions.saveData();
			Message.sendSuccess("Block " + ChatColor.GREEN + args[2] + ChatColor.WHITE + " is now in the blacklist");
			return;
		}
		else if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("-"))
		{
			MaterialData block = Util.getBlock(args[2]);
			if(block == null)
			{
				Message.sendError("Block '" + ChatColor.GREEN + args[2] + ChatColor.WHITE + "' does not exist");
				return;
			}
			
			List<String> blacklist = Regions.getList("mines." + mineName + ".blacklist.blocks");
			
			int index = blacklist.indexOf(block.getItemTypeId() + ":" + block.getData());
			if(index == -1)
			{
				Message.sendError("Block " + args[2] + " is not in the blacklist");
				return;
			}
			blacklist.remove(index);
			Regions.setList("mines." + mineName + ".blacklist.blocks", blacklist);
			Regions.saveData();
			Message.sendSuccess("Block " + args[2] + " has been removed from the blacklist");
			return;
		}
		else
		{
			Message.sendInvalid(args);
			return;
		}
	}
}
