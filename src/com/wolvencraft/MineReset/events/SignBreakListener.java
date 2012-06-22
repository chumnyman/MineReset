package com.wolvencraft.MineReset.events;

import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.SignClass;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.SignUtils;

public class SignBreakListener implements Listener
{
	public SignBreakListener(MineReset plugin) {
		Message.debug("Initiating SignBreakListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onSignBreak(BlockBreakEvent event) {
		if(event.isCancelled()) return;
        Message.debug("SignBreakEvent called");
        
        BlockState b = event.getBlock().getState();
        if(b instanceof Sign) {
        	SignClass sign = SignUtils.getSign((Sign) b);
        	if(sign == null) return;
        	
        	SignUtils.delete(sign);
        	Message.sendPlayerSuccess(event.getPlayer(), "Sign successfully removed");
        	return;
        }
        else return;
	}
}
