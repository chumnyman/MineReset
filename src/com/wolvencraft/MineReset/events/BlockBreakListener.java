package com.wolvencraft.MineReset.events;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class BlockBreakListener implements Listener
{
	public BlockBreakListener(MineReset plugin)
	{
        Message.debug("Initiating BlockBreakListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
        Message.debug("BlockBreakEvent called");
		
		Player player = event.getPlayer();
		
		if(Util.playerHasPermission(player, "protection.bypass") || !Configuration.getBoolean("lag.protection-checks-enabled"))
		{
            Message.debug("Bypass permission check passed");
			return;
		}

        Message.debug("Bypass permission check failed");
		
		int padding;
		int paddingTop;
		
		List<String> regionList = Regions.getList("data.list-of-mines");

        Message.debug("Retrieved the region list");
		
		if(regionList.size() == 0) return;
		
		Block b = event.getBlock();
		
		for(String mineName : regionList )
		{
            Message.debug("For mine " + mineName);
			
			if(Regions.getBoolean("mines." + mineName + ".protection.breaking.enabled"))
			{
                Message.debug(mineName + " has protection enabled");
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
                    Message.debug("Player breakd a block in the mine region");

					if(!Util.playerHasPermission(player, "protection.break." + mineName) && !Util.playerHasPermission(player, "protection.break"))
					{
                        Message.debug("Second permissions check passed");
						Message.sendPlayerError(player, "You are not allowed to break " + ChatColor.RED + b.getType().name().toLowerCase().replace("_", " ") + ChatColor.WHITE + " in this mine");
						event.setCancelled(true);
						return;
					}

                    Message.debug("Second permissions check failed");
					
					if(Regions.getBoolean("mines." + mineName + ".protection.breaking.blacklist.enabled"))
					{
						List<String> blacklist = Regions.getList("mines." + mineName + ".protection.breaking.blacklist.blocks");
						boolean whitelist = Regions.getBoolean("mines." + mineName + ".protection.breaking.blacklist.whitelist");
						boolean found = false;
						
						for(String block : blacklist)
						{	
							String blockTypeId = b.getTypeId() + "";
                            Message.debug(blockTypeId + " ? " + block);
							if(blockTypeId.equals(block))
							{
								found = true;
							}
						}
						
						if((whitelist && !found) || (!whitelist && found))
						{
							Message.sendPlayerError(player, "You are not allowed to break " + ChatColor.RED + b.getType().name().toLowerCase().replace("_", " ") + ChatColor.WHITE + " in this mine");
							event.setCancelled(true);
							return;
						}
					}
					else
					{
						Message.sendPlayerError(player, "You are not allowed to break blocks in this mine");
						event.setCancelled(true);
					}
				}
			}
		}
		
		return;
	}
}