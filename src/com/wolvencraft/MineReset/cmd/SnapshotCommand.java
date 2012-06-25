package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.Location;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Snapshot;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.GeneratorUtil;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.SnapshotUtil;
import com.wolvencraft.MineReset.util.Util;

public class SnapshotCommand {
	public static void run(String[] args) {
		if(!Util.hasPermission("edit.snapshot")) {
			ChatUtil.sendDenied(args);
			return;
		}
		
		if(args.length == 1) {
			HelpCommand.getSnapshot();
			return;
		}
		
		if(args.length > 3) {
			ChatUtil.sendInvalidArguments(args);
			return;
		}
		
		Mine curMine = CommandManager.getMine();
		if(!args[1].equalsIgnoreCase("restore") && curMine == null) {
			ChatUtil.sendMineNotSelected();
			return;
		}
		
		if(args[1].equalsIgnoreCase("save")) {
			if(args.length != 2) {
				ChatUtil.sendInvalidArguments(args);
				return;
			}
			
			if(!Util.locationsSet()) {
				ChatUtil.sendError("Make a selection first");
				return;
			}
			
			Location[] loc = CommandManager.getLocation();
			
			if(!loc[0].getWorld().equals(loc[1].getWorld())) {
				ChatUtil.sendError("Your selection points are in different worlds");
				return;
			}
			
			if(!loc[0].getWorld().equals(curMine.getWorld())) {
				ChatUtil.sendError("Mine and protection regions are in different worlds");
				return;
			}
			
			Snapshot snap = new Snapshot(curMine.getName());
			snap.setBlocks(loc[0].getWorld(), loc[0], loc[1]);
			List<Snapshot> snaps = MineReset.getSnapshots();
			snaps.add(snap);
			MineReset.setSnapshots(snaps);
			SnapshotUtil.save(snap);
			ChatUtil.sendNote(curMine.getName(), "Snapshot successfully saved!");
		}
		else if(args[1].equalsIgnoreCase("restore")) {
			if(args.length == 2 && curMine == null) {
				ChatUtil.sendMineNotSelected();
				return;
			}
			else if(args.length == 3) {
				curMine = MineUtil.getMine(args[2]);
				if(curMine == null) {
					ChatUtil.sendInvalidMineName(args[2]);
					return;
				}
			}
			
			if(GeneratorUtil.get("SNAPSHOT").run(curMine))
				ChatUtil.sendNote(curMine.getName(), "Snapshot successfully restored!");
		}
		else if(args[1].equalsIgnoreCase("delete")) {
			if(args.length != 2) {
				ChatUtil.sendInvalidArguments(args);
				return;
			}
			
			if(SnapshotUtil.delete(SnapshotUtil.getSnapshot(curMine)))
				ChatUtil.sendNote(curMine.getName(), "Snapshot successfully deleted!");
			else
				ChatUtil.sendError("This mine does not have a snapshot saved");
		}
		else {
			ChatUtil.sendInvalid(args);
			return;
		}
		return;
	}
}
