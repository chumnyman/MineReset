package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockState;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Snapshot;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.SnapshotUtils;
import com.wolvencraft.MineReset.util.Util;

public class SnapshotCommand {
	public static void run(String[] args) {
		if(!Util.senderHasPermission("edit")) {
			Message.sendDenied(args);
			return;
		}
		
		if(args.length > 3) {
			Message.sendInvalidArguments(args);
			return;
		}
		
		Mine curMine = CommandManager.getMine();
		if(curMine == null) {
			Message.sendMineNotSelected();
			return;
		}
		
		if(args[1].equalsIgnoreCase("save")) {
			if(!Util.locationsSet()) {
				Message.sendError("Make a selection first");
				return;
			}
			
			Location[] loc = CommandManager.getLocation();
			
			if(!loc[0].getWorld().equals(loc[1].getWorld())) {
				Message.sendError("Your selection points are in different worlds");
				return;
			}
			
			if(!loc[0].getWorld().equals(curMine.getWorld())) {
				Message.sendError("Mine and protection regions are in different worlds");
				return;
			}
			
			Snapshot snap = new Snapshot(curMine);
			snap.save(loc[0].getWorld(), loc[0], loc[1]);
			Message.sendNote(curMine.getName(), "Snapshot successfully saved!");
		}
		else if(args[1].equalsIgnoreCase("restore")) {
			Snapshot snap = SnapshotUtils.getSnapshot(curMine);
			List<BlockState> blocks = snap.getBlocks();
			for(BlockState block : blocks)
				block.update();
			Message.sendNote(curMine.getName(), "Snapshot successfully restored!");
		}
		else if(args[1].equalsIgnoreCase("delete")) {
			if(SnapshotUtils.delete(SnapshotUtils.getSnapshot(curMine)))
				Message.sendNote(curMine.getName(), "Snapshot successfully deleted!");
			else
				Message.sendError("This mine does not have a snapshot saved");
		}
		else {
			Message.sendInvalid(args);
			return;
		}
		return;
	}
}
