package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.mine.Mine;
import org.bukkit.ChatColor;

import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class ListCommand
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("list"))
		{
			Message.sendDenied(args);
			return;
		}
		
		Message.sendMessage("                    -=[ Public Mines ]=-");
		
		for(Mine mine : CommandManager.getPlugin().getMines())
		{
			String displayName = mine.getDisplayName();
			if(displayName.equals(""))
				Message.sendMessage(" - " + ChatColor.GREEN + mine.getName() + "");
			else
				Message.sendMessage(" - " + ChatColor.GREEN + displayName + ChatColor.WHITE + " (" + mine.getName() + ")");
		}
	}
}