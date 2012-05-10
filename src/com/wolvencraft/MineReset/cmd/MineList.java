package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;

import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class MineList
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("list"))
		{
			Message.sendDenied(args);
			return;
		}
		
		List<String> mineList = Regions.getList("data.list-of-mines");
		
		Message.sendMessage("                    -=[ Public Mines ]=-");
		
		for(String mineName : mineList)
		{
			String displayName = Regions.getString("mines." + mineName + ".display-name");
			if(displayName.equals(""))
				Message.sendMessage(" - " + ChatColor.GREEN + mineName + "");
			else
				Message.sendMessage(" - " + ChatColor.GREEN + displayName + ChatColor.WHITE + " (" + mineName + ")");
		}
	}
}