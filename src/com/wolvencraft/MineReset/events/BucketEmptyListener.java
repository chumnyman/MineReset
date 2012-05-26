package com.wolvencraft.MineReset.events;

import java.util.List;

import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.util.Mine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Regions;
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
        Message.debug("BucketEmptyEvent caught");

        if (Util.playerHasPermission(event.getPlayer(), "protection.place") || !Configuration.getBoolean("lag.protection-checks-enabled")) {
            Message.debug("Player has perms or protection is disabled");
            return;
        }

        List<String> mines = Regions.getList("data.list-of-mines");

        for (String mine : mines) {
            if (!Regions.getBoolean("mines." + mine + ".protection.placement.enabled")) {
                Message.debug(mine + " doesn't have placement protection enabled, skipping rest of check...");
                continue;
            }

            if (event.getBlockClicked().getWorld().equals(Bukkit.getWorld(Regions.getString("mines." + mine + ".coordinates.world")))) {
                Message.debug(mine + " mine's world, " + Regions.getString("mines." + mine + ".coordinates.world") + ", didn't equal block's world, " + event.getBlockClicked().getWorld().getName());
                continue;
            }

            if (Util.playerHasPermission(event.getPlayer(), "protection.place." + mine)) {
                Message.debug("Player had permission to place in that mine, leave 'em alone!");
                continue;
            }

            if (Mine.isBlockInMine(event.getBlockClicked().getRelative(event.getBlockFace()), mine)) {
                Message.debug("Yup, they can't place that bucket in the mine.");
                event.setCancelled(true);
                Message.sendPlayerError(event.getPlayer(), "You are not allowed to empty buckets in the mine! No soup for you!");
                break;
            }
        }
    }
}