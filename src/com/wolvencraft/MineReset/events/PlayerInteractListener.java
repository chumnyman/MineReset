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
import com.wolvencraft.MineReset.cmd.ResetCommand;
import com.wolvencraft.MineReset.mine.SignClass;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.SignUtils;
import com.wolvencraft.MineReset.util.Util;

public class PlayerInteractListener implements Listener
{
	public PlayerInteractListener(MineReset plugin)
	{
        Message.debug("Initiating PlayerInteractListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
        Message.debug("PlayerInteractEvent passed");
		
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (event.getAction().equals(Action.LEFT_CLICK_BLOCK))
		{
			if(!Util.playerHasPermission(player, "edit")) return;
			if(player.getItemInHand().equals(new ItemStack(Material.WOOD_AXE))) {
				Location loc = block.getLocation();
				CommandManager.setLocation(loc, 0);
				event.setCancelled(true);
			}
			return;
		}
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(player.getItemInHand().equals(new ItemStack(Material.WOOD_AXE))) {
				if(!Util.playerHasPermission(player, "edit")) return;
				Location loc = event.getClickedBlock().getLocation();
				CommandManager.setLocation(loc, 1);
				event.setCancelled(true);
				return;
			}
			
			if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
			{
                Message.debug("A sign has been clicked");
				if(!Util.playerHasPermission(player, "reset.sign")) {
					return;
				}

		     	SignClass sign = SignUtils.getSignAt(block.getLocation());
				if(sign == null) {
					return;
				}
				
		     	if(sign.getReset())
		     	{
		     		String[] args = {"reset", sign.getParent().getName()};
		     		ResetCommand.run(args, false, null);
		     	}
			}
			
			return;
		}
		else return;

	}
}
