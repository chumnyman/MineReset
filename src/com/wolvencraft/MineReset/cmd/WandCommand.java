package com.wolvencraft.MineReset.cmd;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.Util;

public class WandCommand {
	public static void run(String[] args) {
		if(!Util.hasPermission("edit.wand")) {
			ChatUtil.sendDenied(args);
			return;
		}
		
		if(args.length > 1) {
			ChatUtil.sendInvalidArguments(args);
			return;
		}
		
		if(!Util.isPlayer()) {
			ChatUtil.sendError("This command can only be executed by a living player");
			return;
		}
		
		Player player = (Player) CommandManager.getSender();
		ItemStack wand = new ItemStack(Configuration.getInt("misc.wand-item"));
		
		if(player.getInventory().contains(wand)) {
			ChatUtil.sendError("You already have a " + wand.getType().toString().toLowerCase().replace("_", " "));
			return;
		}
		
		player.getInventory().addItem(wand);
		ChatUtil.sendSuccess("Here is your " + wand.getType().toString().toLowerCase().replace("_", " "));
		return;
	}
}
