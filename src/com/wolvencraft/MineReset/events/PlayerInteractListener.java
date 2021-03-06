/*
 * PlayerInteraceListener.java
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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineCommand;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.SignClass;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.SignUtil;
import com.wolvencraft.MineReset.util.Util;

public class PlayerInteractListener implements Listener
{
    public PlayerInteractListener(MineReset plugin)
    {
        ChatUtil.debug("Initiating PlayerInteractListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.isCancelled()) return;
        ChatUtil.debug("PlayerInteractEvent passed");
        
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if(!Util.playerHasPermission(player, "edit.select")) return;
            ItemStack wand = new ItemStack(Configuration.getInt("misc.wand-item"));
            if(player.getItemInHand().equals(wand)) {
                if(MineReset.getWorldEditPlugin() != null && (new ItemStack(MineReset.getWorldEditPlugin().getLocalConfiguration().navigationWand).equals(wand))) return;
                
                Location loc = event.getClickedBlock().getLocation();
                CommandManager.setLocation(loc, 0);
                event.setCancelled(true);

                String message = "First point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
                if(Util.locationsSet()) message = message + ": " + Util.getBlockCount() + " blocks";
                ChatUtil.sendSuccess (message);
            }
            return;
        }
        
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(!Util.playerHasPermission(player, "edit.select")) return;
            ItemStack wand = new ItemStack(Configuration.getInt("misc.wand-item"));
            if(player.getItemInHand().equals(wand)) {
                if(MineReset.getWorldEditPlugin() != null && (new ItemStack(MineReset.getWorldEditPlugin().getLocalConfiguration().navigationWand).equals(wand))) return;

                Location loc = event.getClickedBlock().getLocation();
                CommandManager.setLocation(loc, 1);
                event.setCancelled(true);
                
                String message =  "Second point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
                if(Util.locationsSet()) message = message + ": " + Util.getBlockCount() + " blocks";
                ChatUtil.sendSuccess (message);

                return;
            }
            
            Block block = event.getClickedBlock();
            
            if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
            {
                ChatUtil.debug("A sign has been clicked");
                
                 SignClass sign = SignUtil.getSignAt(block.getLocation());
                if(sign == null) return;
                if(!sign.getReset()) return;
             
                 Mine curMine = MineUtil.getMine(sign.getParent());
                if(!Util.playerHasPermission(player, "reset.sign." + curMine.getName()) && !Util.playerHasPermission(player, "reset.sign")) {
                    ChatUtil.sendError(player, "You do not have permission to do this!");
                    return;
                }
                
                if(curMine.getCooldown() && curMine.getNextCooldown() > 0 && !Util.hasPermission("reset.bypass")) {
                    ChatUtil.sendError(Util.parseVars(Language.getString("reset.mine-cooldown"), curMine));
                    return;
                }
                 
                 MineCommand.RESET.run(curMine.getName());
            }
            return;
        }
        else return;

    }
}
