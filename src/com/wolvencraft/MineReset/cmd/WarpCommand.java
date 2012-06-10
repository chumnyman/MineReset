package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.MineUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.Message;
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
			Message.sendError("This command cannot be executed via console");
			return;
		}
		if(!Util.senderHasPermission("warp")) {
			Message.sendDenied(args);
			return;
		}
		
		if(args.length == 1) {
			HelpCommand.getTeleport();
			return;
		}
		else if(args.length != 2) {
			Message.sendInvalidArguments(args);
			return;
		}
		
		if(args[1].equalsIgnoreCase("set")) {
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				Message.sendMineNotSelected();
				return;
			}
			
			Location loc = player.getLocation();
			curMine.setWarp(loc);
			
			Message.sendSuccess ("Mine spawn point set (" + (int)loc.getX() + ", " + (int)loc.getY() + ", " + (int)loc.getZ() + ")");
			return;
		}
		
		if(MineUtils.getMine(args[1]) != null) {
			MineUtils.warpToMine(player, MineUtils.getMine(args[1]));
			String message = Util.parseVars(Language.getString("misc.mine-teleport"), MineUtils.getMine(args[1]));
			Message.sendSuccess(message);
			return;
		}
		else {
			Message.sendInvalidMineName(args[1]);
			return;
		}
	}
}
