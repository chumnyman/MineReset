package com.wolvencraft.MineReset.cmd;

import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineError;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;

public class WarpCommand {
	
	public static boolean run(String[] args) {
		if(args.length == 1) {
			getHelp();
			return true;
		}
		
		Player player;
		if(Util.isPlayer()) player = (Player) CommandManager.getSender();
		else {
			ChatUtil.sendError("This command cannot be executed via console");
			return false;
		}
		
		if(args.length != 2) {
			ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
			return false;
		}
		
		if(args[0].equalsIgnoreCase("set")) {
			if(!Util.hasPermission("warp.set")) {
				ChatUtil.sendInvalid(MineError.ACCESS, args);
				return false;
			}
			Mine curMine = CommandManager.getMine();
			if(curMine == null) {
				ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
				return false;
			}
			
			curMine.setWarp(player.getLocation());
			
			ChatUtil.sendSuccess("Mine spawn point set at the current location!");
			return true;
		}
		
		Mine curMine = MineUtil.getMine(args[1]);
		if(curMine != null) {
			if(!Util.hasPermission("warp.use." + curMine.getName()) && !Util.hasPermission("warp.use")) {
				ChatUtil.sendInvalid(MineError.ACCESS, args);
				return false;
			}
			player.teleport(curMine.getWarp());
			String message = Util.parseVars(Language.getString("misc.mine-teleport"), curMine);
			ChatUtil.sendSuccess(message);
			return true;
		}
		else {
			ChatUtil.sendInvalid(MineError.MINE_NAME, args, args[1]);
			return false;
		}
	}

	public static void getHelp() {
		ChatUtil.formatHeader(20, "Teleportation");
		ChatUtil.formatHelp("warp", "<name>", "Teleports you to the mine warp location");
		ChatUtil.formatHelp("warp", "set", "Sets a warp for the current mine");
	}
}
