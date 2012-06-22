package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.MineUtils;
import com.wolvencraft.MineReset.util.Util;

public class SaveCommand
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
		
		if(!Util.hasPermission("edit.save")) {
			Message.sendDenied(args);
			return;
		}
		
		if(args.length != 2) {
			Message.sendInvalidArguments(args);
			return;
		}
		
		if(!Util.locationsSet()) {
			Message.sendError("Make a selection first");
			return;
		}
		
		Location[] loc = CommandManager.getLocation();
		
		if(!loc[0].getWorld().equals(loc[1].getWorld())) {
			Message.sendError("Your selection points are in different worlds");
			return;
		}

		if(MineUtils.getMine(args[1]) != null) {
			Message.sendError("Mine '" + args[1] + "' already exists!");
			return;
		}

        Message.debug("Mine existance check passed");
        
        // Parsing the coordinates
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
		
		Mine newMine = new Mine(loc[0], loc[1], player.getLocation(), loc[0].getWorld(), args[1], 900);
		
		// = = Adding to the mine list
		List<Mine> mines = MineReset.getMines();
		mines.add(newMine);
		MineReset.setMines(mines);
		
		MineUtils.save(newMine);

        Message.debug("Mine creation completed");
		
		CommandManager.setLocation(null, 0);
		CommandManager.setLocation(null, 1);

        Message.debug("Data saved successfully");
		CommandManager.setMine(newMine);
		Message.sendSuccess("Mine '" + newMine.getName() + "' created successfully!");
		return;
	}
}
