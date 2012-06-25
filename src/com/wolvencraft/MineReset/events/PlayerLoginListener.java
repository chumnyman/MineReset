package com.wolvencraft.MineReset.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.wolvencraft.AutoUpdater.Updater;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.util.Util;


public class PlayerLoginListener implements Listener
{
	
	public PlayerLoginListener(MineReset plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerJoinEvent event) {
		if(!Configuration.getBoolean("updater.remind-on-login")) return;
		
		Player player = event.getPlayer();
		if(Util.playerHasPermission(player, Configuration.getString("updater.permission-node"))) {
			if(!Updater.checkVersion()) {
				player.sendMessage("Update for " + ChatColor.BLUE + "MineReset" + ChatColor.WHITE + " (" + ChatColor.GOLD + Updater.getVersion() + "." + Updater.getSubVersion() + ChatColor.WHITE + ") is available! Urgency: " + ChatColor.GOLD + Updater.getUrgency());
			}
		}
		return;
	}
}