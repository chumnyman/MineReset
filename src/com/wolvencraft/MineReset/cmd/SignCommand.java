package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.block.Sign;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.SignClass;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineError;
import com.wolvencraft.MineReset.util.SignUtil;
import com.wolvencraft.MineReset.util.Util;

public class SignCommand  implements BaseCommand {
	public boolean run(String args[]) {
		Player player;
		if(CommandManager.getSender() instanceof Player)
			player = (Player) CommandManager.getSender();
		else {
			ChatUtil.sendError("This command cannot be executed via console");
			return false;
		}
		if(!Util.hasPermission("edit.sign")) {
			ChatUtil.sendInvalid(MineError.ACCESS, args);
			return false;
		}
		
		if(args.length == 1) {
			getHelp();
			return false;
		}
		
		if(args.length != 2) {
			ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
			return false;
		}
		
		Block b = player.getTargetBlock(null, 100);
		if(b.getType() != Material.WALL_SIGN && b.getType() != Material.SIGN_POST) {
			ChatUtil.sendError("You are targeting " + b.getType().toString() + "; you can only use this command on signs");
			return false;
		}
		
		Mine curMine = CommandManager.getMine();
		if(curMine == null) {
			ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
			return false;
		}
		
		
		if(args[1].equalsIgnoreCase("create")) {	
			if(SignUtil.exists(b.getLocation())) {
				ChatUtil.sendError("This sign has been defined already.");
				return false;
			}
			
			SignClass sign = new SignClass(curMine.getName(), b.getLocation(), (Sign) b.getState());
			
			List<SignClass> signs = MineReset.getSigns();
			signs.add(sign);
			MineReset.setSigns(signs);
			
			ChatUtil.sendNote(curMine.getName(), "A new sign was defined successfully");
			SignUtil.saveAll();
			return false;
		}
		else if(args[1].equalsIgnoreCase("reset")) {
			if(!SignUtil.exists(b.getLocation())) {
				ChatUtil.sendError("This sign has not been defined for this mine yet");
				return false;
			}
			SignClass sign = SignUtil.getSignAt(b.getLocation());
			
			if(sign.getReset()) {
				sign.setReset(false);
				ChatUtil.sendNote(curMine.getName(), "Right-clicking on this sign will no longer reset the mine");
			}
			else {
				sign.setReset(true);
				ChatUtil.sendNote(curMine.getName(), "Right-clicking on this sign will now reset the mine");
			}

			SignUtil.save(sign);
			return true;
		}
		else if(args[1].equalsIgnoreCase("remove")) {
			if(!SignUtil.exists(b.getLocation())) {
				ChatUtil.sendError("This sign has not been defined yet");
				return false;
			}
			
			SignClass sign = SignUtil.getSignAt(b.getLocation());
			if(b.getState() instanceof Sign) {
				Sign signBlock = (Sign) b.getState();
				List<String> lines = sign.getLines();

				for(int i = 0; i < lines.size(); i++)
					signBlock.setLine(i, lines.get(i));
				signBlock.update(true);
			}
			else {
				ChatUtil.sendError("You are targeting " + b.getType().toString() + "; you can only use this command on signs");
				return false;
			}
			
			List<SignClass> signs = MineReset.getSigns();
			signs.remove(sign);
			MineReset.setSigns(signs);
			SignUtil.delete(sign);
			SignUtil.saveAll();
			ChatUtil.sendSuccess("This sign is no longer defined. You can destroy it now.");
			return true;
		}
		else {
			ChatUtil.sendInvalid(MineError.INVALID, args);
			return false;
		}
	}
	
	public void getHelp() {
		ChatUtil.formatHeader(20, "Signs");
		ChatUtil.formatHelp("sign", "create", "Saves the targeted sign into a file");
		ChatUtil.formatHelp("sign", "setparent <id>", "Sets the sign's parent to the one specified");
		ChatUtil.formatHelp("sign", "reset", "Marks the targeted sign as mine-resetting");
		ChatUtil.formatHelp("sign", "remove", "Removes the targeted sign from data");
		return;
	}
}
