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
		if(Util.debugEnabled()) Util.log("Initiating BlockplaceListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onBlockplace(BlockPlaceEvent event)
	{
		if(Util.debugEnabled()) Util.log("BlockplaceEvent called");
		
		Player player = event.getPlayer();
		
		if(player.isOp() || Util.playerHasPermission(player, "protection") || Util.playerHasPermission(player, "protection.place"))
			return;
		
		if(Util.debugEnabled()) Util.log("Permissions check passed");
		
		int padding;
		int paddingTop;
		
		List<String> regionList = Regions.getList("data.list-of-mines");
		
		if(Util.debugEnabled()) Util.log("Retrieved the region list");
		
		for(String mineName : regionList )
		{
			if(Util.debugEnabled()) Util.log("For mine " + mineName);
				if(!Util.playerHasPermission(player, "protection.place." + mineName))
				{
					if(Util.debugEnabled()) Util.log("The player does not have protection.place." + mineName);
					Block b = event.getBlock();
					Location blockLocation = b.getLocation();
					padding = Regions.getInt("mines." + mineName + ".protection.padding");
					paddingTop = Regions.getInt("mines." + mineName + ".protection.padding-top");
					String mineWorld = Regions.getString("mines." + mineName + ".coordinates.world");
					int[] x = {Regions.getInt("mines." + mineName + ".coordinates.pos0.x"), Regions.getInt("mines." + mineName + ".coordinates.pos1.x")};
					int[] y = {Regions.getInt("mines." + mineName + ".coordinates.pos0.y"), Regions.getInt("mines." + mineName + ".coordinates.pos1.y")};
					int[] z = {Regions.getInt("mines." + mineName + ".coordinates.pos0.z"), Regions.getInt("mines." + mineName + ".coordinates.pos1.z")};
					
					if(mineWorld.equals(blockLocation.getWorld().getName())
							&& (blockLocation.getX() >= (x[0] - padding) && blockLocation.getX() <= (x[1] + padding))
							&& (blockLocation.getY() >= (y[0] - padding) && blockLocation.getY() <= (y[1] + paddingTop))
							&& (blockLocation.getZ() >= (z[0] - padding) && blockLocation.getZ() <= (z[1] + padding)))
					{
						if(Util.debugEnabled()) Util.log("Player broke a block in the mine region");
						if(Regions.getBoolean("mines." + mineName + ".protection.placement.blacklist.enabled"))
						{
							List<String> blacklist = Regions.getList("mines." + mineName + ".protection.placement.blacklist.blocks");
							if(Regions.getBoolean("mines." + mineName + ".protection.placement.blacklist.whitelist"))
							{
								for(String block : blacklist)
								{
									if(Util.debugEnabled()) Util.log(Integer.parseInt(block) + " ? " + b.getTypeId());
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
									if(Util.debugEnabled()) Util.log(Integer.parseInt(block) + " ? " + b.getTypeId());
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
