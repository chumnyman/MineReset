package com.wolvencraft.MineReset.events;

import java.util.List;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Protection;
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
		
		List<Mine> mines = MineReset.getMines();

        Message.debug("Retrieved the region list");
		
		if(mines.size() == 0) return;
		
		Block b = event.getBlock();
		
		for(Mine mine : mines)
		{
            Message.debug("For mine " + mine.getName());
			
			if (mine.getProtection().contains(Protection.BLOCK_BREAK))
			{
                Message.debug(mine.getName() + " has protection enabled");
				Location blockLocation = b.getLocation();

				if (mine.isLocationInMine(event.getBlock().getLocation())) {
                    Message.debug("Player broke a block in the mine region");

					if(!Util.playerHasPermission(player, "protection.break." + mine.getName()) && !Util.playerHasPermission(player, "protection.break"))
					{
                        Message.debug("Second permissions check passed");
						Message.sendPlayerError(player, "You are not allowed to break " + ChatColor.RED + b.getType().name().toLowerCase().replace("_", " ") + ChatColor.WHITE + " in this mine");
						event.setCancelled(true);
						return;
					}

                    Message.debug("Second permissions check failed");

                    //TODO: Implement blacklist again
					if(false)//Regions.getBoolean("mines." + mineName + ".protection.breaking.blacklist.enabled"))
					{
						List<String> blacklist = Regions.getList("mines." + mine.getName() + ".protection.breaking.blacklist.blocks");
						boolean whitelist = Regions.getBoolean("mines." + mine.getName() + ".protection.breaking.blacklist.whitelist");
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