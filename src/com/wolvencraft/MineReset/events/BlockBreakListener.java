package com.wolvencraft.MineReset.events;

import java.util.List;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Protection;
import com.wolvencraft.MineReset.mine.SignClass;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.SignUtil;
import com.wolvencraft.MineReset.util.Util;

public class BlockBreakListener implements Listener
{
	public BlockBreakListener(MineReset plugin) {
		ChatUtil.debug("Initiating BlockBreakListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onBlockbreak(BlockBreakEvent event) {
		if(event.isCancelled()) return;
		ChatUtil.debug("BlockBreakEvent caught");
		
		Player player = event.getPlayer();
		
		if(Util.playerHasPermission(player, "protection.bypass.break")) {
			ChatUtil.debug("The player has a permission to bypass the protection. Aborting . . .");
			signCheck(event);
			return;
		}

		ChatUtil.debug("Retrieving the region list...");
		List<Mine> mines = MineReset.getMines();
		
		if(mines.size() == 0) {
			ChatUtil.debug("No mines defined! Aborting . . .");
			signCheck(event);
			return;
		}
		Block b = event.getBlock();
		String blockName = ChatColor.RED + b.getType().name().toLowerCase().replace("_", " ") + ChatColor.WHITE;
		
		for(Mine mine : mines) {
			ChatUtil.debug("Checking mine " + mine.getName());
			
			if(!mine.isLocationInProtection(b.getLocation())) continue;
			
			ChatUtil.debug("Location is in the mine protection region");
			
			if(!Util.playerHasPermission(player, "protection.break." + mine.getName()) && !Util.playerHasPermission(player, "protection.break")) {
				ChatUtil.debug("Player " + event.getPlayer().getName() + " does not have permission to break blocks in the mine");
				ChatUtil.sendError(player, "You are not allowed to break " + blockName + " in this area");
				event.setCancelled(true);
				return;
			}
				
			if(!mine.getProtection().contains(Protection.BLOCK_BREAK)) {
				ChatUtil.debug("Mine has no block breaking protection enabled");
				continue;
			}
				
			ChatUtil.debug("Mine has a block breaking protection enabled");
			if(mine.getBreakBlacklist().getEnabled()) {
				ChatUtil.debug("Block breaking blacklist detected");
				boolean found = false;
				for(MaterialData block : mine.getBreakBlacklist().getBlocks()) {
					if(block.getItemType().equals(b.getType())) {
						found = true;
						break;
					}
				}
				
				if((mine.getBreakBlacklist().getWhitelist() && !found) || (!mine.getBreakBlacklist().getWhitelist() && found)) {
					ChatUtil.debug("Player " + player.getName() + " broke a black/whitelisted block in the mine!");
					ChatUtil.sendError(player, "You are not allowed to break " + blockName + " in this area");
					event.setCancelled(true);
					return;
				}
			}
			else {
				ChatUtil.debug("No block breaking blacklist detected");
				ChatUtil.sendError(player, "You are not allowed to break " + blockName + " in this area");
				event.setCancelled(true);
			}
		}
		ChatUtil.debug("Broken block was not in the mine region");
		signCheck(event);
		return;
	}
	
	public void signCheck(BlockBreakEvent event) {
		if(event.isCancelled()) return;
        BlockState b = event.getBlock().getState();
        if(b instanceof Sign) {
        	ChatUtil.debug("Checking for defined signs before quitting . . .");
        	SignClass sign = SignUtil.getSign((Sign) b);
        	if(sign == null) return;
        	
        	SignUtil.delete(sign);
        	ChatUtil.sendSuccess(event.getPlayer(), "Sign successfully removed");
        	return;
        }
        else return;
	}
}
