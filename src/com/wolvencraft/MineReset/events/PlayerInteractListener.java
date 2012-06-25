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
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.SignClass;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.SignUtil;
import com.wolvencraft.MineReset.util.Util;

public class PlayerInteractListener implements Listener
{
	public PlayerInteractListener(MineReset plugin)
	{
        ChatUtil.debug("Initiating PlayerInteractListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.isCancelled()) return;
        ChatUtil.debug("PlayerInteractEvent passed");
		
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(!Util.playerHasPermission(player, "edit.select")) return;
			if(player.getItemInHand().equals(new ItemStack(Material.WOOD_AXE))) {
				Location loc = block.getLocation();
				CommandManager.setLocation(loc, 0);
				event.setCancelled(true);
			}
			return;
		}
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(player.getItemInHand().equals(new ItemStack(Material.WOOD_AXE))) {
				if(!Util.playerHasPermission(player, "edit.select")) return;
				Location loc = event.getClickedBlock().getLocation();
				CommandManager.setLocation(loc, 1);
				event.setCancelled(true);
				return;
			}
			
			if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
			{
                ChatUtil.debug("A sign has been clicked");
				if(!Util.playerHasPermission(player, "reset.sign")) {
					return;
				}
				
		     	SignClass sign = SignUtil.getSignAt(block.getLocation());
				if(sign == null) return;
				
		     	if(sign.getReset()) {
		     		Mine curMine = MineUtil.getMine(sign.getParent());
					if(curMine.getCooldown() && curMine.getNextCooldown() > 0 && !Util.hasPermission("reset.bypass")) {
						ChatUtil.sendPlayerNote(player, curMine.getName(), "You can reset the mine in " + Util.parseSeconds(curMine.getNextCooldown()));
						return;
					}
		     		
		     		String[] args = {"reset", curMine.getName()};
		     		ResetCommand.run(args, false, null);
		     	}
			}
			return;
		}
		else return;

	}
}
