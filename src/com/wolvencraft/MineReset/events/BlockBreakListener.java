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
import com.wolvencraft.MineReset.cmd.Util;
import com.wolvencraft.MineReset.config.Regions;

public class BlockBreakListener implements Listener
{
	public BlockBreakListener(MineReset plugin)
	{
		if(Util.debugEnabled()) Util.log("Initiating BlockBreakListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(Util.debugEnabled()) Util.log("BlockBreakEvent called");
		
		Player player = event.getPlayer();
		
		if(Util.debugEnabled() && player.isOp()) Util.log("Player is Op");
		if(Util.debugEnabled() && Util.playerHasPermission(player, "protection")) Util.log("Player has protection permission");
		if(Util.debugEnabled() && Util.playerHasPermission(player, "protection.break")) Util.log("Player has protection.break permission");
		
		if(Util.playerHasPermission(player, "protection.break"))
		{
			return;
		}
		
		if(Util.debugEnabled()) Util.log("Permissions check passed");
		
		int padding;
		int paddingTop;
		
		List<String> regionList = Regions.getList("data.list-of-mines");
		
		if(Util.debugEnabled()) Util.log("Retrieved the region list");
		
		for(String mineName : regionList )
		{
			if(Util.debugEnabled()) Util.log("For mine " + mineName);
			if(Regions.getBoolean("mines." + mineName + ".protection.breaking.enabled"))
			{
				if(!Util.playerHasPermission(player, "protection.break." + mineName) && !Util.playerHasPermission(player, "protection.break"))
				{
					if(Util.debugEnabled()) Util.log("The player does not have protection.break." + mineName);
					Block b = event.getBlock();
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
						if(Util.debugEnabled()) Util.log("Player broke a block in the mine region");
						if(Regions.getBoolean("mines." + mineName + ".protection.breaking.blacklist.enabled"))
						{
							List<String> blacklist = Regions.getList("mines." + mineName + ".protection.breaking.blacklist.blocks");
							boolean whitelist = Regions.getBoolean("mines." + mineName + ".protection.breaking.blacklist.whitelist");
							
							for(String block : blacklist)
							{
								String blockTypeId = b.getTypeId() + "";
								if(Util.debugEnabled()) Util.log(blockTypeId + " ? " + block);
								if((whitelist && !blockTypeId.equals(block)) || (!whitelist && blockTypeId.equals(block)))
								{
									Util.sendPlayerError(player, "You are not allowed to break " + ChatColor.RED + b.getType().name().toLowerCase().replace("_", " ") + ChatColor.WHITE + " in this mine");
									event.setCancelled(true);
									return;
								}
							}
						}
						else
						{
							Util.sendPlayerError(player, "You are not allowed to break blocks in this mine");
							event.setCancelled(true);
						}
					}
				}
			}
		}
		
		return;
	}
}
