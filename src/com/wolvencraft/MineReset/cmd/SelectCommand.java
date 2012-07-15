package com.wolvencraft.MineReset.cmd;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineError;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;

public class SelectCommand {
	public static void run(String[] args) {
		
		Player player;
		if(CommandManager.getSender() instanceof Player)
			player = (Player) CommandManager.getSender();
		else {
			ChatUtil.sendError("This command cannot be executed via console");
			return;
		}
		
		if(!Util.isPlayer() || !Util.hasPermission("edit.select")) {
			ChatUtil.sendInvalid(MineError.ACCESS, args);
			return;
		}

		if(args.length == 1) {
			getHelp();
			return;
		}
		
		if(args.length != 2) {
			ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
			return;
		}
		
		Location loc = null;
		String message = "";
		
		if(args[0].equalsIgnoreCase("select")) {
			
			Mine curMine = MineUtil.getMine(args[1]);
			if(curMine == null) {
				ChatUtil.sendInvalid(MineError.MINE_NAME, args);
				return;
			}
			
			CommandManager.setLocation(curMine.getFirstPoint(), 0);
			CommandManager.setLocation(curMine.getSecondPoint(), 1);
			ChatUtil.sendNote(curMine.getName(), "Referents points of the mine are selected!");
			return;
		}
		else if(args[0].equalsIgnoreCase("hpos1")) {
			loc = player.getTargetBlock(null, 100).getLocation();
			CommandManager.setLocation(loc, 0);
			message = "First position set to (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
		}
		else if(args[0].equalsIgnoreCase("pos1")) {
			loc = player.getLocation();
			CommandManager.setLocation(loc, 0);
			message = "First position set to (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
		}
		else if(args[0].equalsIgnoreCase("hpos2")) {
			loc = player.getTargetBlock(null, 100).getLocation();
			CommandManager.setLocation(loc, 1);
			message = "Second position set to (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
		}
		else if(args[0].equalsIgnoreCase("pos2")) {
			loc = player.getLocation();
			CommandManager.setLocation(loc, 1);
			message = "Second position set to (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
		}
		else {
			ChatUtil.sendInvalid(MineError.INVALID, args);
			return;
		}
		
		if(Util.locationsSet()) message = message + " [" + Util.getBlockCount() + "]";
		ChatUtil.sendSuccess (message);
		
		return;
	}
	
	public static void getHelp() {
		ChatUtil.formatHeader(20, "Selecting");
		ChatUtil.formatHelp("hpos1", "", "Creates a reference point 1 at the block you are looking at");
		ChatUtil.formatHelp("hpos2", "", "Creates a reference point 2 at the block you are looking at");
		ChatUtil.formatMessage("Your field of view is limited to 100 blocks");
		ChatUtil.formatHelp("pos1", "", "Creates a reference point 1 at your immediate location");
		ChatUtil.formatHelp("pos2", "", "Creates a reference point 2 at your immediate location");
		ChatUtil.formatMessage("You can also select a region with your normal World Edit tool");
		return;
	}
}
