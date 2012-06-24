package com.wolvencraft.MineReset.generation;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.mine.DataBlock;
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
			ChatUtil.debug("Snapshot does not exist!");
			ChatUtil.sendError("Snapshot was never saved! Use " + ChatColor.GOLD + "/mine snapshot save" + ChatColor.WHITE + " to save it");
			return false;
		}
		List<DataBlock> blocks = snap.getBlocks();
		if(blocks.isEmpty()) {
			ChatUtil.debug("Snapshot block list is empty!");
			ChatUtil.sendError("Snapshot was never saved! Use " + ChatColor.GOLD + "/mine snapshot save" + ChatColor.WHITE + " to save it");
			return false;
		}
		
		if(curMine.getBlacklist().getEnabled())
    	{        		
    		if(curMine.getBlacklist().getWhitelist()) {
    			for(DataBlock block : blocks) {
    				Block original = block.getLocation().getBlock();
    				MaterialData newBlock = block.getData();
    				if(curMine.getBlacklist().getBlocks().contains(original.getState().getData())) {
    					original.setTypeIdAndData(newBlock.getItemTypeId(), newBlock.getData(), false);
    				}
    			}
    		}
    		else {
    			for(DataBlock block : blocks) {
    				Block original = block.getLocation().getBlock();
    				MaterialData newBlock = block.getData();
    				if(!curMine.getBlacklist().getBlocks().contains(original.getData())) {
    					original.setTypeIdAndData(newBlock.getItemTypeId(), newBlock.getData(), false);
    				}
    			}
    		}
    	}
    	else
    	{
	        for(DataBlock block : blocks) {
				Block original = block.getLocation().getBlock();
				MaterialData newBlock = block.getData();
				original.setTypeIdAndData(newBlock.getItemTypeId(), newBlock.getData(), false);
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
