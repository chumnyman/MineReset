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
//		Message.debug("Entity was damaged by another Entity!");
		if (!(event.getDamager() instanceof Player || event.getDamager() instanceof Arrow) || !(event.getEntity() instanceof Player)) {
//			Message.debug("Entities weren't players");
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
			
			ChatUtil.sendPlayerError(attacker, "PvP is not allowed in the mine!");
			event.setCancelled(true);
			return;
		}
	}
}
