/*
 * PVPListener.java
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

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Protection;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.Util;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class PVPListener implements Listener {
    public PVPListener(MineReset plugin) {
        ChatUtil.debug("Initiating PVPListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) return;
//        Message.debug("Entity was damaged by another Entity!");
        if (!(event.getDamager() instanceof Player || event.getDamager() instanceof Arrow) || !(event.getEntity() instanceof Player)) {
//            Message.debug("Entities weren't players");
            return;
        }
        Player attacker;
        if (event.getDamager() instanceof Arrow) {
            if ((((Arrow) event.getDamager()).getShooter() instanceof Player)) {
                attacker = (Player) ((Arrow) event.getDamager()).getShooter();
            } else {
                ChatUtil.debug("Attacking entity (an arrow) wasn't shot by a player.");
                return;
            }
        } else {
            attacker = (Player) event.getDamager();
        }
        Player victim = (Player) event.getEntity();
        if (Util.playerHasPermission(attacker, "protection.bypass")) {
            ChatUtil.debug("Attacker had perms or protection is disabled");
            return;
        }

        List<Mine> mines = MineReset.getMines();

        for (Mine mine : mines) {
            ChatUtil.debug("Checking mine " + mine.getName());
            
            if(!mine.isLocationInProtection(victim.getLocation())) continue;
            
            if (!mine.getProtection().contains(Protection.PVP)) {
                ChatUtil.debug(mine + " doesn't have PvP protection on");
                continue;
            }
            
            ChatUtil.sendError(attacker, "PvP is not allowed in the mine!");
            event.setCancelled(true);
            return;
        }
    }
}
