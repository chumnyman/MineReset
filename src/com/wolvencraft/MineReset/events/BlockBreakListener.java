package com.wolvencraft.MineReset.events;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.wolvencraft.MineReset.cmd.Util;

public class BlockBreakListener implements Listener
{
	public BlockBreakListener()
	{
		// does nothing
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(Util.debugEnabled()) Util.log("BlockBreakEvent called");
		
		Player player = event.getPlayer();
		
		if(player.isOp() || Util.playerHasPermission(player, "protection") || Util.playerHasPermission(player, "protection.break"))
			return;
		
		if(Util.debugEnabled()) Util.log("Permissions check passed");
		
		int padding;
		int paddingTop;
		
		List<String> regionList = Util.getRegionList("data.list-of-mines");
		
		for(String mineName : regionList )
		{
				if(!Util.playerHasPermission(player, "protection.break." + mineName))
				{
					Block b = event.getBlock();
					Location blockLocation = b.getLocation();
					padding = Util.getRegionInt("mines." + mineName + ".protection.padding");
					paddingTop = Util.getRegionInt("mines." + mineName + ".protection.padding-top");
					int[] x = {Util.getConfigInt("regions." + mineName + ".coords.p1.x"), Util.getConfigInt("regions." + mineName + ".coords.p2.x")};
					int[] y = {Util.getConfigInt("regions." + mineName + ".coords.p1.y"), Util.getConfigInt("regions." + mineName + ".coords.p2.y")};
					int[] z = {Util.getConfigInt("regions." + mineName + ".coords.p1.z"), Util.getConfigInt("regions." + mineName + ".coords.p2.z")};
			
					if((blockLocation.getX() > (x[0] - padding) && blockLocation.getX() < (x[1] + padding))
							&& (blockLocation.getY() > (y[0] - padding) && blockLocation.getY() < (y[1] + paddingTop))
							&& (blockLocation.getZ() > (z[0] - padding) && blockLocation.getZ() < (z[1] + padding)))
					{
						if(Util.debugEnabled()) Util.log("Player is in the mine region");
						if(Util.getConfigBoolean("mines." + mineName + ".protection.breaking.blacklist.enabled"))
						{
							List<String> blacklist = Util.getConfigList("mines." + mineName + ".protection.breaking.blacklist.blocks");
							if(Util.getConfigBoolean("mines." + mineName + ".protection.breaking.blacklist.whitelist"))
							{
								for(String block : blacklist)
								{
									if(Integer.parseInt(block) == b.getTypeId())
									{
										Util.sendPlayerError(player, "You are not allowed to break " + ChatColor.RED + b.getType().name().toLowerCase().replace("_", " ") + ChatColor.WHITE + " in this mine");
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
										Util.sendPlayerError(player, "You are not allowed to break " + ChatColor.RED + b.getType().name().toLowerCase().replace("_", " ") + ChatColor.WHITE + " in this mine");
										event.setCancelled(true);
										return;
									}
									else return;
								}
							}
						}
					
						Util.sendPlayerError(player, "You are not allowed to break blocks in this mine");
						event.setCancelled(true);
					}
					else return;
				}
		}
		
		return;
	}
}
