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
import com.wolvencraft.MineReset.config.Regions;

public class BlockPlaceListener implements Listener
{
	public BlockPlaceListener(MineReset plugin)
	{
		if(Util.debugEnabled()) Util.log("Initiating BlockPlaceListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(Util.debugEnabled()) Util.log("BlockPlaceEvent called");
		
		Player player = event.getPlayer();
		
		if(Util.playerHasPermission(player, "protection.place"))
		{
			return;
		}
		
		if(Util.debugEnabled()) Util.log("Permissions check passed");
		
		int padding;
		int paddingTop;
		
		List<String> regionList = Regions.getList("data.list-of-mines");
		
		if(Util.debugEnabled()) Util.log("Retrieved the region list");
		
		if(regionList.size() == 0) return;
		
		for(String mineName : regionList )
		{
			if(Util.debugEnabled()) Util.log("For mine " + mineName);
			if(Regions.getBoolean("mines." + mineName + ".protection.placement.enabled"))
			{
				if(!Util.playerHasPermission(player, "protection.place." + mineName) && !Util.playerHasPermission(player, "protection.place"))
				{
					if(Util.debugEnabled()) Util.log("The player does not have protection.place." + mineName);
					Block b = event.getBlockPlaced();
					Location blockLocation = b.getLocation();
					padding = Regions.getInt("mines." + mineName + ".protection.padding");
					paddingTop = Regions.getInt("mines." + mineName + ".protection.padding-top");
					String mineWorld = Regions.getString("mines." + mineName + ".coordinates.world");
					int[] x = {Regions.getInt("mines." + mineName + ".coordinates.pos0.x"), Regions.getInt("mines." + mineName + ".coordinates.pos1.x")};
					int[] y = {Regions.getInt("mines." + mineName + ".coordinates.pos0.y"), Regions.getInt("mines." + mineName + ".coordinates.pos1.y")};
					int[] z = {Regions.getInt("mines." + mineName + ".coordinates.pos0.z"), Regions.getInt("mines." + mineName + ".coordinates.pos1.z")};
					
					if(mineWorld.equals(blockLocation.getWorld().getName())
							&& (blockLocation.getBlockX() >= (x[0] - padding) && blockLocation.getBlockX() <= (x[1] + padding))
							&& (blockLocation.getBlockY() >= (y[0] - padding) && blockLocation.getBlockY() <= (y[1] + paddingTop))
							&& (blockLocation.getBlockZ() >= (z[0] - padding) && blockLocation.getBlockZ() <= (z[1] + padding)))
					{
						if(Util.debugEnabled()) Util.log("Player placed a block in the mine region");
						if(Regions.getBoolean("mines." + mineName + ".protection.placement.blacklist.enabled"))
						{
							List<String> blacklist = Regions.getList("mines." + mineName + ".protection.placement.blacklist.blocks");
							boolean whitelist = Regions.getBoolean("mines." + mineName + ".protection.placement.blacklist.whitelist");
							
							for(String block : blacklist)
							{
								if(block.equals("326")) block = 8 + "";
								if(block.equals("329")) block = 10 + "";
								
								String blockTypeId = b.getTypeId() + "";
								if(Util.debugEnabled()) Util.log(blockTypeId + " ? " + block);
								if((whitelist && !blockTypeId.equals(block)) || (!whitelist && blockTypeId.equals(block)))
								{
									Util.sendPlayerError(player, "You are not allowed to place " + ChatColor.RED + b.getType().name().toLowerCase().replace("_", " ") + ChatColor.WHITE + " in this mine");
									event.setCancelled(true);
									return;
								}
							}
						}
						else
						{
							Util.sendPlayerError(player, "You are not allowed to place blocks in this mine");
							event.setCancelled(true);
						}
					}
				}
			}
		}
		
		return;
	}
}
