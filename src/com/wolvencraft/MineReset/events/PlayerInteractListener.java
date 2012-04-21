package com.wolvencraft.MineReset.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
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
				Location loc = event.getClickedBlock().getLocation();
				CommandManager.setLocation(loc, 1);
				return;
			}
			
			if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
			{
				BlockState state = block.getState();
				if(state instanceof Sign)
				{
					Sign sign = (Sign) state;
		     		String signTitle = Util.getConfigString("message.title-on-signs");
		     		if(!sign.getLine(0).equalsIgnoreCase("[" + signTitle + "]")) return;
		     		
		     		String mineName[] = {sign.getLine(1)};
		     		Reset.run(mineName, false);
		     		
		        	Util.sendSuccess("Mine '" + mineName + "' successfully reset");
		     		return;
				}
			}
			
			return;
		}
		else return;

	}
}
