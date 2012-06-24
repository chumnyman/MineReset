package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.Mine;
import org.bukkit.ChatColor;

import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.Util;

public class ListCommand
{
	public static void run(String[] args)
	{
		if(!Util.hasPermission("info.list")) {
			ChatUtil.sendDenied(args);
			return;
		}
		
		if(args.length > 2) {
			ChatUtil.sendInvalidArguments(args);
			return;
		}
		
		ChatUtil.sendMessage(ChatColor.DARK_RED + "                    -=[ " + ChatColor.GREEN + ChatColor.BOLD + "Public Mines" + ChatColor.DARK_RED + " ]=-");
		
		for(Mine mine : MineReset.getMines()) {
			String displayName = mine.getDisplayName();
			if(displayName.equals(""))
				ChatUtil.sendMessage(" - " + ChatColor.GREEN + mine.getName() + "");
			else
				ChatUtil.sendMessage(" - " + ChatColor.GREEN + displayName + ChatColor.WHITE + " (" + mine.getName() + ")");
		}
	}
}