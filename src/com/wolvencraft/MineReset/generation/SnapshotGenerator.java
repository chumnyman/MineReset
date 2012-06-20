package com.wolvencraft.MineReset.generation;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Snapshot;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.SnapshotUtils;

public class SnapshotGenerator {
	public static void reset(Mine curMine) {
		Snapshot snap = SnapshotUtils.getSnapshot(curMine);
		if(snap == null) {
			Message.sendError("Snapshot was never saved! Use " + ChatColor.GOLD + "/mine snapshot save" + ChatColor.WHITE + " to save it");
			return;
		}
		List<BlockState> blocks = snap.getBlocks();
		if(!blocks.isEmpty())
		{
			Message.sendError("Snapshot was never saved! Use " + ChatColor.GOLD + "/mine snapshot save" + ChatColor.WHITE + " to save it");
			return;
		}
		
		if(curMine.getBlacklist().getEnabled())
    	{        		
    		if(curMine.getBlacklist().getWhitelist()) {
    			for(BlockState block : blocks) {
    				MaterialData original = new MaterialData(block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).getType());
    				if(curMine.getBlacklist().getBlocks().contains(original))
                    	block.update();
    			}
    		}
    		else {
    			for(BlockState block : blocks) {
    				MaterialData original = new MaterialData(block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).getType());
    				if(!curMine.getBlacklist().getBlocks().contains(original))
                    	block.update();
    			}
    		}
    	}
    	else
    	{
	        for(BlockState block : blocks) {
	        	block.update();
	        }
    	}
		return;
	}
}
