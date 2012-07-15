package com.wolvencraft.MineReset.cmd;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.GeneratorUtil;
import com.wolvencraft.MineReset.util.MineError;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;

public class SaveCommand
{
	public static void run(String[] args)
	{
		Player player;
		if(CommandManager.getSender() instanceof Player)
			player = (Player) CommandManager.getSender();
		else {
			ChatUtil.sendError("This command cannot be executed via console");
			return;
		}
		
		if(!Util.hasPermission("edit.save")) {
			ChatUtil.sendInvalid(MineError.ACCESS, args);
			return;
		}
		
		if(args.length == 1) {
			getHelp();
			return;
		}
		
		if(args.length != 2 && args.length != 3) {
			ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
			return;
		}
		
		String generator;
		if(args.length == 3) {
			if(GeneratorUtil.get(args[2]) != null) {
				generator = args[2].toUpperCase();
			}
			else {
				ChatUtil.sendError("Invalid generator provided!");
				return;
			}
		}
		else generator = "RANDOM";
		
		if(!Util.locationsSet()) {
			WorldEditPlugin we = MineReset.getWorldEditPlugin();
			if(we == null) {
				ChatUtil.sendError("Make a selection first");
				return;
			}
			else {
				Selection sel = we.getSelection(player);
				if(sel == null) {
					ChatUtil.sendError("Make a selection first");
					return;
				}
				CommandManager.setLocation(we.getSelection(player).getMinimumPoint(), 0);
				CommandManager.setLocation(we.getSelection(player).getMaximumPoint(), 1);
			}
		}
		
		Location[] loc = CommandManager.getLocation();
		
		if(!loc[0].getWorld().equals(loc[1].getWorld())) {
			ChatUtil.sendError("Your selection points are in different worlds");
			return;
		}

		if(MineUtil.getMine(args[1]) != null) {
			ChatUtil.sendError("Mine '" + args[1] + "' already exists!");
			return;
		}

        ChatUtil.debug("Mine existance check passed");
        
        double temp = 0;
		
		if((int)loc[0].getX() > (int)loc[1].getX()) {
			temp = loc[0].getBlockX();
			loc[0].setX(loc[1].getBlockX());
			loc[1].setX(temp);
		}
		
		if((int)loc[0].getY() > (int)loc[1].getY()) {
			temp = loc[0].getBlockY();
			loc[0].setY(loc[1].getBlockY());
			loc[1].setY(temp);
		}
		
		if((int)loc[0].getZ() > (int)loc[1].getZ()) {
			temp = loc[0].getBlockZ();
			loc[0].setZ(loc[1].getBlockZ());
			loc[1].setZ(temp);
		}
		
		Mine newMine = new Mine(loc[0], loc[1], player.getLocation(), loc[0].getWorld(), args[1], generator, 900);
		
		GeneratorUtil.get(generator).init(newMine);
		
		MineReset.getMines().add(newMine);
		MineUtil.save(newMine);

        ChatUtil.debug("Mine creation completed");
		
		CommandManager.resetLocation();
		CommandManager.setMine(newMine);
		ChatUtil.sendNote(newMine.getName(), "Mine created successfully!");
		return;
	}
	
	public static void getHelp() {
		ChatUtil.formatHeader(20, "Save");
		ChatUtil.formatHelp("save", "<name> [generator]", "Saves the region for future use, with an optional generator");
		ChatUtil.formatMessage("If no generator is specified, RandomGenerator will be used. Available generators:");
		ChatUtil.formatMessage(GeneratorUtil.list());
		return;
	}
}
