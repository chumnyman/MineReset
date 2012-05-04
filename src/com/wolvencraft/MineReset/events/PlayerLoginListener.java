package com.wolvencraft.MineReset.events;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.cmd.Util;

import couk.Adamki11s.AutoUpdater.AUCore;

public class PlayerLoginListener implements Listener
{
	private Logger log;
	private AUCore core;
	
	public PlayerLoginListener(MineReset plugin)
	{
		log = Bukkit.getServer().getLogger();
		core = new AUCore("http://wolvencraft.com/plugins/MineReset/index.html", log);
		
		if(Util.debugEnabled()) Util.log("Initiating PlayerLoginListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerJoinEvent event)
	{
		if(Util.playerHasPermission(event.getPlayer(), "edit"))
		{
			if(!core.checkVersion())
			{
				Player player = event.getPlayer();
				player.sendMessage(ChatColor.BLUE + "Update for MineReset (" + core.getVersion() + "." + core.getSubVersion() + ") is available! Urgency: " + core.getUrgency());
			}
		}
	}
}