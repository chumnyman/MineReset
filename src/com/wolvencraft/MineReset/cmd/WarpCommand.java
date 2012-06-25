package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.MineUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.Util;

public class WarpCommand
{
	public static void run(String[] args)
	{
		Player player;
		if(CommandManager.getSender() instanceof Player) {
			player = (Player) CommandManager.getSender();
		}
		else {
			ChatUtil.sendError("This command cannot be executed via console");
			return;
		}
		
		if(args.length == 1) {
			HelpCommand.getWarp();
			return;
		}
		else if(args.length != 2) {
			ChatUtil.sendInvalidArguments(args);
			return;
		}
		
		if(args[1].equalsIgnoreCase("set")) {
			if(!Util.hasPermission("warp.set")) {
				ChatUtil.sendDenied(args);
				return;
			}
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				ChatUtil.sendMineNotSelected();
				return;
			}
			
			Location loc = player.getLocation();
			curMine.setWarp(loc);
			
			ChatUtil.sendSuccess ("Mine spawn point set (" + (int)loc.getX() + ", " + (int)loc.getY() + ", " + (int)loc.getZ() + ")");
			return;
		}
		
		if(MineUtil.getMine(args[1]) != null) {
			Mine curMine = MineUtil.getMine(args[1]);
			player.teleport(curMine.getWarp());
			String message = Util.parseVars(Language.getString("misc.mine-teleport"), curMine);
			ChatUtil.sendSuccess(message);
			return;
		}
		else {
			if(!Util.hasPermission("warp.use")) {
				ChatUtil.sendDenied(args);
				return;
			}
			ChatUtil.sendInvalidMineName(args[1]);
			return;
		}
	}
}
