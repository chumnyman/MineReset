package com.wolvencraft.MineReset.events;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.cmd.Util;

public class BlockPlaceListener implements Listener
{
	public BlockPlaceListener(MineReset plugin)
	{
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(Util.debugEnabled()) Util.log("BlockPlaceEvent called");
		
		Player player = event.getPlayer();
		
		if(player.isOp() || Util.playerHasPermission(player, "protection") || Util.playerHasPermission(player, "protection.place"))
			return;
		
		if(Util.debugEnabled()) Util.log("Permissions check passed");
		
		int padding;
		int paddingTop;
		
		List<String> regionList = Util.getRegionList("data.list-of-mines");
		
		if(Util.debugEnabled()) Util.log("Retrieved the region list");
		
		for(String mineName : regionList )
		{
			if(Util.debugEnabled()) Util.log("For mine " + mineName);
				if(!Util.playerHasPermission(player, "protection.place." + mineName))
				{
					if(Util.debugEnabled()) Util.log("The player does not have protection.place." + mineName);
					Block b = event.getBlock();
					Location blockLocation = b.getLocation();
					padding = Util.getRegionInt("mines." + mineName + ".protection.padding");
					paddingTop = Util.getRegionInt("mines." + mineName + ".protection.padding-top");
					int[] x = {Util.getRegionInt("mines." + mineName + ".coordinates.pos0.x"), Util.getRegionInt("mines." + mineName + ".coordinates.pos1.x")};
					int[] y = {Util.getRegionInt("mines." + mineName + ".coordinates.pos0.y"), Util.getRegionInt("mines." + mineName + ".coordinates.pos1.y")};
					int[] z = {Util.getRegionInt("mines." + mineName + ".coordinates.pos0.z"), Util.getRegionInt("mines." + mineName + ".coordinates.pos1.z")};
					
					if((blockLocation.getX() > (x[0] - padding) && blockLocation.getX() < (x[1] + padding))
							&& (blockLocation.getY() > (y[0] - padding) && blockLocation.getY() < (y[1] + paddingTop))
							&& (blockLocation.getZ() > (z[0] - padding) && blockLocation.getZ() < (z[1] + padding)))
					{
						if(Util.debugEnabled()) Util.log("Player is in the mine region");
						if(Util.getConfigBoolean("mines." + mineName + ".protection.placement.blacklist.enabled"))
						{
							List<String> blacklist = Util.getConfigList("mines." + mineName + ".protection.placement.blacklist.blocks");
							if(Util.getConfigBoolean("mines." + mineName + ".protection.placement.blacklist.whitelist"))
							{
								for(String block : blacklist)
								{
									if(Integer.parseInt(block) == b.getTypeId())
									{
										Util.sendPlayerError(player, "You are not allowed to place " + ChatColor.RED + b.getType().name().toLowerCase().replace("_", " ") + ChatColor.WHITE + " in this mine");
										event.setCancelled(true);
										return;
									}
									else return;
								}
							}
							else
							{
								for(String block : blacklist)
								{
									if(Integer.parseInt(block) != b.getTypeId())
									{
										Util.sendPlayerError(player, "You are not allowed to place " + ChatColor.RED + b.getType().name().toLowerCase().replace("_", " ") + ChatColor.WHITE + " in this mine");
										event.setCancelled(true);
										return;
									}
									else return;
								}
							}
						}
					
						Util.sendPlayerError(player, "You are not allowed to place blocks in this mine");
						event.setCancelled(true);
					}
					else return;
				}
		}
		
		return;
	}
}
