package com.wolvencraft.MineReset.events;

import java.util.List;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Protection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.Util;

public class BucketEmptyListener implements Listener
{
	public BucketEmptyListener(MineReset plugin) {
        ChatUtil.debug("Initiating BucketEmptyListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent event) {
		if(event.isCancelled()) return;
        ChatUtil.debug("BucketEmptyEvent caught");
        
        Player player = event.getPlayer();
        
        if(Util.playerHasPermission(event.getPlayer(), "protection.bypass.place")) {
            ChatUtil.debug("The player has a permission to bypass the protection. Aborting . . .");
			return;
		}

        ChatUtil.debug("Retrieving the region list...");
        List<Mine> mines = MineReset.getMines();

        for (Mine mine : mines) {
			ChatUtil.debug("Checking mine " + mine.getName());
            
        	if(!mine.isLocationInProtection(event.getBlockClicked().getRelative(event.getBlockFace()).getLocation())) continue;
        	
	        if(!Util.playerHasPermission(player, "protection.place." + mine.getName()) && !Util.playerHasPermission(player, "protection.place")) {
	        	ChatUtil.debug("Player " + event.getPlayer().getName() + " does not have permission to empty buckets in the mine");
	        	ChatUtil.sendPlayerError(player, "You are not allowed to empty buckets in this area");
	        	event.setCancelled(true);
	          	return;
	        }
            
            if (!mine.getProtection().contains(Protection.BLOCK_PLACE)) {
                ChatUtil.debug("The mine doesn't have placement protection enabled, skipping rest of check...");
                continue;
            }

            ChatUtil.debug("Mine has a block placement protection enabled");
			if(mine.getPlaceBlacklist().getEnabled()) {
				ChatUtil.debug("Block placement blacklist detected");
				boolean found = false;
				for(MaterialData block : mine.getPlaceBlacklist().getBlocks()) {
					if(block.getItemType().equals(event.getBucket())) {
						found = true;
						break;
					}
				}
				
				if((mine.getPlaceBlacklist().getWhitelist() && !found) || (!mine.getPlaceBlacklist().getWhitelist() && found)) {
					ChatUtil.debug("Player " + player.getName() + " broke a black/whitelisted block in the mine!");
					ChatUtil.sendPlayerError(player, "You are not allowed to empty buckets in the mine");
					event.setCancelled(true);
					return;
				}
			}
			else {
				ChatUtil.debug("No block placement blacklist detected");
				ChatUtil.sendPlayerError(player, "You are not allowed to empty buckets in the mine");
				event.setCancelled(true);
			}
        }
    }
}