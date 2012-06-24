package com.wolvencraft.MineReset.generation;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Snapshot;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.SnapshotUtil;

public class SnapshotGenerator implements BaseGenerator {
	public final String NAME;
	public final String DESCRIPTION;
	
	public SnapshotGenerator() {
		NAME = "SNAPSHOT";
		DESCRIPTION = "Resets the contents of the mine to the snapshot taken before";
	}
	
	public boolean run(Mine curMine) {
		Snapshot snap = SnapshotUtil.getSnapshot(curMine);
		if(snap == null) {
			ChatUtil.sendError("Snapshot was never saved! Use " + ChatColor.GOLD + "/mine snapshot save" + ChatColor.WHITE + " to save it");
			return false;
		}
		List<BlockState> blocks = snap.getBlocks();
		if(!blocks.isEmpty())
		{
			ChatUtil.sendError("Snapshot was never saved! Use " + ChatColor.GOLD + "/mine snapshot save" + ChatColor.WHITE + " to save it");
			return false;
		}
		
		if(curMine.getBlacklist().getEnabled())
    	{        		
    		if(curMine.getBlacklist().getWhitelist()) {
    			for(BlockState block : blocks) {
    				MaterialData original = new MaterialData(block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).getType());
    				if(curMine.getBlacklist().getBlocks().contains(original))
    					block.update(true);
    			}
    		}
    		else {
    			for(BlockState block : blocks) {
    				MaterialData original = new MaterialData(block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).getType());
    				if(!curMine.getBlacklist().getBlocks().contains(original))
                    	block.update(true);
    			}
    		}
    	}
    	else
    	{
	        for(BlockState block : blocks) {
	        	block.update();
	        }
    	}
		return true;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
}
