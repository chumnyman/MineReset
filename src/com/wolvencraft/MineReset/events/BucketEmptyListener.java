package com.wolvencraft.MineReset.events;

import java.util.List;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Protection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class BucketEmptyListener implements Listener
{
	public BucketEmptyListener(MineReset plugin) {
        Message.debug("Initiating BucketEmptyListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent event) {
		if(event.isCancelled()) return;
        Message.debug("BucketEmptyEvent caught");

        if (Util.playerHasPermission(event.getPlayer(), "protection.place")) {
            Message.debug("Player has perms or protection is disabled");
            return;
        }

        List<Mine> mines = MineReset.getMines();

        for (Mine mine : mines) {
            if (!mine.getProtection().contains(Protection.BLOCK_PLACE)) {
                Message.debug(mine.getName() + " doesn't have placement protection enabled, skipping rest of check...");
                continue;
            }


            if (Util.playerHasPermission(event.getPlayer(), "protection.place." + mine.getName())) {
                Message.debug("Player had permission to place in that mine, leave 'em alone!");
                continue;
            }

            if (mine.isLocationInProtection(event.getBlockClicked().getRelative(event.getBlockFace()).getLocation())) {
                Message.debug("Yup, they can't place that bucket in the mine.");
                event.setCancelled(true);
                Message.sendPlayerError(event.getPlayer(), "You are not allowed to empty buckets in the mine! No soup for you!");
                break;
            }
        }
    }
}