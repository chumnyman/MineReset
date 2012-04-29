package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;

import com.wolvencraft.MineReset.config.Regions;

public class MineList
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("list"))
		{
			Util.sendDenied(args);
			return;
		}
		
		List<String> mineList = Regions.getList("data.list-of-mines");
		
		Util.sendMessage("                    -=[ Public Mines ]=-");
		
		for(String s : mineList)
		{
			Util.sendMessage(" - " + ChatColor.GREEN + s + "");
		}
	}
}