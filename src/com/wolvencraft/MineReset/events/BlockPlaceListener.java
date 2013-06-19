/*
 * BlockPlaceListener.java
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
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.Util;

public class BlockPlaceListener implements Listener
{
    public BlockPlaceListener(MineReset plugin) {
        ChatUtil.debug("Initiating BlockPlaceListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onBlockplace(BlockPlaceEvent event) {
        if(event.isCancelled()) return;
        ChatUtil.debug("BlockPlaceEvent caught");
        
        Player player = event.getPlayer();
        
        if(Util.playerHasPermission(player, "protection.bypass.place")) {
            ChatUtil.debug("The player has a permission to bypass the protection. Aborting . . .");
            return;
        }

        ChatUtil.debug("Retrieving the region list...");
        List<Mine> mines = MineReset.getMines();
        
        if(mines.size() == 0) {
            ChatUtil.debug("No mines defined! Aborting . . .");
            return;
        }
        Block b = event.getBlock();
        String blockName = ChatColor.RED + b.getType().name().toLowerCase().replace("_", " ") + ChatColor.WHITE;
        
        for(Mine mine : mines) {
            ChatUtil.debug("Checking mine " + mine.getName());
            
            if(!mine.isLocationInProtection(b.getLocation())) continue;
            
            ChatUtil.debug("Location is in the mine protection region");
            
            if(!Util.playerHasPermission(player, "protection.place." + mine.getName()) && !Util.playerHasPermission(player, "protection.place")) {
                ChatUtil.debug("Player " + event.getPlayer().getName() + " does not have permission to place blocks in the mine");
                ChatUtil.sendError(player, "You are not allowed to place " + blockName + " in this area");
                event.setCancelled(true);
                continue;
            }
                
            if(!mine.getProtection().contains(Protection.BLOCK_PLACE)) {
                ChatUtil.debug("Mine has no block placement protection enabled");
                continue;
            }
                
            ChatUtil.debug("Mine has a block placement protection enabled");
            if(mine.getPlaceBlacklist().getEnabled()) {
                ChatUtil.debug("Block placement blacklist detected");
                boolean found = false;
                for(MaterialData block : mine.getPlaceBlacklist().getBlocks()) {
                    if(block.getItemType().equals(b.getType())) {
                        found = true;
                        break;
                    }
                }
                
                if((mine.getPlaceBlacklist().getWhitelist() && !found) || (!mine.getPlaceBlacklist().getWhitelist() && found)) {
                    ChatUtil.debug("Player " + player.getName() + " broke a black/whitelisted block in the mine!");
                    ChatUtil.sendError(player, "You are not allowed to place " + blockName + " in this area");
                    event.setCancelled(true);
                    return;
                }
            }
            else {
                ChatUtil.debug("No block placement blacklist detected");
                ChatUtil.sendError(player, "You are not allowed to place " + blockName + " in this area");
                event.setCancelled(true);
            }
        }
        ChatUtil.debug("Placed block was not in the mine region");
        return;
    }
}
