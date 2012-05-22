package com.wolvencraft.MineReset.events;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class BucketEmptyListener implements Listener
{
	public BucketEmptyListener(MineReset plugin)
	{
        Message.debug("Initiating BucketEmptyListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent  event)
	{
        Message.debug("PlayerBucketEmptyEvent called");
		
		Player player = event.getPlayer();
		
		if(Util.playerHasPermission(player, "protection.place"))
		{
			return;
		}

        Message.debug("Permissions check passed");
		
		int padding;
		int paddingTop;
		
		List<String> regionList = Regions.getList("data.list-of-mines");

        Message.debug("Retrieved the region list");
		
		for(String mineName : regionList )
		{
            Message.debug("For mine " + mineName);
			if(Regions.getBoolean("mines." + mineName + ".protection.placement.enabled"))
			{
				if(!Util.playerHasPermission(player, "protection.place." + mineName) && !Util.playerHasPermission(player, "protection.place"))
				{
                    Message.debug("The player does not have protection.place." + mineName);
					Block b = event.getBlockClicked();
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
                        Message.debug("Player emptied a bucket in the mine region");
						if(Regions.getBoolean("mines." + mineName + ".protection.placement.blacklist.enabled"))
						{
							List<String> blacklist = Regions.getList("mines." + mineName + ".protection.placement.blacklist.blocks");
							boolean whitelist = Regions.getBoolean("mines." + mineName + ".protection.placement.blacklist.whitelist");
							
							for(String block : blacklist)
							{
								String blockTypeId = b.getTypeId() + "";
								if(block.equals("326") || block.equals("327"))
								{
                                    Message.debug(blockTypeId + " ? " + block);
									if((whitelist && !blockTypeId.equals(block)) || (!whitelist && blockTypeId.equals(block)))
									{
										Message.sendPlayerError(player, "You are not allowed to place " + ChatColor.RED + b.getType().name().toLowerCase().replace("_", " ") + ChatColor.WHITE + " in this mine");
										event.setCancelled(true);
										return;
									}
								}
							}
						}
						else
						{
							event.setCancelled(true);
						}
					}
				}
			}
		}
		
		return;
	}
}