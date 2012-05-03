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
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.cmd.Reset;
import com.wolvencraft.MineReset.cmd.Util;
import com.wolvencraft.MineReset.config.Signs;

public class PlayerInteractListener implements Listener
{
	public PlayerInteractListener(MineReset plugin)
	{
		if(Util.debugEnabled()) Util.log("Initiating PlayerInteractListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(Util.debugEnabled()) Util.log("PlayerInteractEvent passed");
		
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (event.getAction().equals(Action.LEFT_CLICK_BLOCK))
		{
			if(!Util.playerHasPermission(player, "edit")) return;
			if(player.getItemInHand().equals(new ItemStack(Material.WOOD_AXE)))
			{
				Location loc = block.getLocation();
				CommandManager.setLocation(loc, 0);
			}
			return;
		}
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(player.getItemInHand().equals(new ItemStack(Material.WOOD_AXE)))
			{
				if(!Util.playerHasPermission(player, "edit")) return;
				Location loc = event.getClickedBlock().getLocation();
				CommandManager.setLocation(loc, 1);
				return;
			}
			
			if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
			{
				if(Util.debugEnabled()) Util.log("Resetting a mine with a sign");
				if(!Util.playerHasPermission(player, "reset.sign"))
				{
					return;
				}
				if(Util.debugEnabled()) Util.log("Permissions check passed!");

		     	String id = Signs.getId(block);
				if(id == null)
				{
					return;
				}
				if(Util.debugEnabled()) Util.log("All checks passed!");
		     	if(Util.debugEnabled()) Util.log("Sign found! (id " + id + ")");
		     	String mineName = Signs.getString("signs." + id + ".mine");
		     	if(Signs.getBoolean("signs." + id + ".reset"))
		     	{
		     		if(Util.debugEnabled()) Util.log("Resetting!");
		     		String[] args = {"reset", mineName};
		     		Reset.run(args, true);
		     	}
			}
			
			return;
		}
		else return;

	}
}
