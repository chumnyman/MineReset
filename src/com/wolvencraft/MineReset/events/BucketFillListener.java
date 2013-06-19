/*
 * BucketFillListener.java
 * 
 * MineReset
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.MineReset.events;

import java.util.List;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Protection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.Util;

public class BucketFillListener implements Listener
{
    public BucketFillListener(MineReset plugin) {
        ChatUtil.debug("Initiating BucketFillListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if(event.isCancelled()) return;
        ChatUtil.debug("BucketFillEvent caught");
        
        Player player = event.getPlayer();
        
        if(Util.playerHasPermission(event.getPlayer(), "protection.bypass.break")) {
            ChatUtil.debug("The player has a permission to bypass the protection. Aborting . . .");
            return;
        }

        ChatUtil.debug("Retrieving the region list...");
        List<Mine> mines = MineReset.getMines();

        for (Mine mine : mines) {
            ChatUtil.debug("Checking mine " + mine.getName());
            
            if(!mine.isLocationInProtection(event.getBlockClicked().getRelative(event.getBlockFace()).getLocation())) continue;
            
            if(!Util.playerHasPermission(player, "protection.break." + mine.getName()) && !Util.playerHasPermission(player, "protection.break")) {
                ChatUtil.debug("Player " + event.getPlayer().getName() + " does not have permission to fill buckets in the mine");
                ChatUtil.sendError(player, "You are not allowed to fill buckets in this area");
                event.setCancelled(true);
                  return;
            }
            
            if (!mine.getProtection().contains(Protection.BLOCK_BREAK)) {
                ChatUtil.debug("The mine doesn't have breaking protection enabled, skipping rest of check...");
                continue;
            }

            ChatUtil.debug("Mine has a block breaking protection enabled");
            if(mine.getBreakBlacklist().getEnabled()) {
                ChatUtil.debug("Block breaking blacklist detected");
                boolean found = false;
                for(MaterialData block : mine.getBreakBlacklist().getBlocks()) {
                    if(block.getItemType().equals(event.getBucket())) {
                        found = true;
                        break;
                    }
                }
                
                if((mine.getBreakBlacklist().getWhitelist() && !found) || (!mine.getBreakBlacklist().getWhitelist() && found)) {
                    ChatUtil.debug("Player " + player.getName() + " broke a black/whitelisted block in the mine!");
                    ChatUtil.sendError(player, "You are not allowed to fill buckets in the mine");
                    event.setCancelled(true);
                    return;
                }
            }
            else {
                ChatUtil.debug("No block breaking blacklist detected");
                ChatUtil.sendError(player, "You are not allowed to fill buckets in the mine");
                event.setCancelled(true);
            }
        }
    }
}