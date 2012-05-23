package com.wolvencraft.MineReset.events;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Mine;
import com.wolvencraft.MineReset.util.Util;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class PVPListener implements Listener {
    public PVPListener(MineReset plugin) {
        Message.debug("Initiating PVPListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Message.debug("Entity was damaged by another Entity!");
        if (!(event.getDamager() instanceof Player || event.getDamager() instanceof Arrow) || !(event.getEntity() instanceof Player)) {
            Message.debug("Entities weren't players");
            return;
        }
        Player attacker;
        if (event.getDamager() instanceof Arrow) {
            if ((((Arrow) event.getDamager()).getShooter() instanceof Player)) {
                attacker = (Player) ((Arrow) event.getDamager()).getShooter();
            } else {
                Message.debug("Attacking entity (an arrow) wasn't shot by a player.");
                return;
            }
        } else {
            attacker = (Player) event.getDamager();
        }
        Player victim = (Player) event.getEntity();
        if (Util.playerHasPermission(attacker, "protection.bypass") || !Configuration.getBoolean("lag.protection-checks-enabled")) {
            Message.debug("Attacker had perms or protection is disabled");
            return;
        }

        List<String> regionList = Regions.getList("data.list-of-mines");

        for (String mine : regionList) {
            Message.debug("Checking mine "+mine);
            if (!Regions.getBoolean("mines." + mine + ".protection.pvp.enabled")) {
                Message.debug(mine+" doesn't have PvP protection on");
                return;
            }
            if (Mine.playerInTheMine(attacker, mine) || Mine.playerInTheMine(victim, mine)) {
                Message.sendPlayerError(attacker, "PvP is not allowed in the mine!");
                event.setCancelled(true);
                return;
            }
        }
    }
}
