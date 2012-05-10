package com.wolvencraft.MineReset.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Configuration;

import couk.Adamki11s.AutoUpdater.Updater;

public class PlayerLoginListener implements Listener
{
	
	public PlayerLoginListener(MineReset plugin)
	{
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerJoinEvent event)
	{
		if(!Configuration.getBoolean("versions.remind-on-login"))
			return;
		
		String perm = Configuration.getString("versions.permission-node");
		
		if(event.getPlayer().hasPermission(perm));
		{
			if(!Updater.checkVersion())
			{
				Player player = event.getPlayer();
				player.sendMessage(ChatColor.BLUE + "Update for MineReset (" + Updater.getVersion() + "." + Updater.getSubVersion() + ") is available! Urgency: " + Updater.getUrgency());
			}
		}
	}
}