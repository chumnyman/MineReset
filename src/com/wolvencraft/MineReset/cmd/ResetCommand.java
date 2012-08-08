package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineCommand;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Reset;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.GeneratorUtil;
import com.wolvencraft.MineReset.util.MineError;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;

public class ResetCommand implements BaseCommand
{	
	public boolean run(String[] args) {
		Mine curMine;
		if(args.length == 1) curMine = CommandManager.getMine();
		else curMine = MineUtil.getMine(args[1]);
		
		if(curMine == null) {
			if(args.length == 1) getHelp();
			ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
			return false;
		}
		
		ChatUtil.debug("Resettign mine: " + curMine.getName());
		Reset source;
		if(CommandManager.getSender() == null) {
			source = Reset.AUTOMATIC;
			ChatUtil.debug("Automatic reset!");
		}
		else {
			source = Reset.MANUAL;
			ChatUtil.debug("Manual reset!");
		}
		
		if(source.equals(Reset.MANUAL)) {
			if(!Util.hasPermission("reset.manual." + curMine.getName()) && !Util.hasPermission("reset.manual")) {
				ChatUtil.sendInvalid(MineError.ACCESS, args);
				return false;
			}
			
			if(curMine.getCooldown() && curMine.getNextCooldown() > 0 && !Util.hasPermission("reset.bypass")) {
				ChatUtil.sendError(Util.parseVars(Language.getString("reset.mine-cooldown"), curMine));
				return false;
			}
			
			MineReset.getStats().updateViaCommand();
		}
		
		String forcedGenerator = "";
		if(args.length == 3) forcedGenerator = args[2];
		
		String generator = curMine.getGenerator();
		if(forcedGenerator.equals("")) generator = curMine.getGenerator();
		
		if(curMine.getAutomatic()) curMine.resetTimer();
		if(curMine.getCooldown()) curMine.resetCooldown();
		
		if(!(curMine.reset(generator))) return false;
		
		String broadcastMessage;
		if(source.equals(Reset.AUTOMATIC)) {
			List<Mine> mines = MineReset.getMines();
			for(Mine childMine : mines) {
				Mine parent = MineUtil.getMine(childMine.getParent());
				if(parent != null && parent.equals(curMine)) {
					MineCommand.RESET.run(childMine.getName());
				}
			}
			
			broadcastMessage = Language.getString("reset.automatic-reset-successful");
		}
		else broadcastMessage = Language.getString("reset.manual-reset-successful");
		
		if(curMine.getParent() == null) {
			broadcastMessage = Util.parseVars(broadcastMessage, curMine);
			
			if(!curMine.getSilent()) ChatUtil.broadcast(broadcastMessage);
			else if(!source.equals(Reset.AUTOMATIC)) ChatUtil.sendSuccess(broadcastMessage);
		}
		return true;
	}
	
	public void getHelp() {
		ChatUtil.formatHeader(20, "Reset");
		ChatUtil.formatHelp("reset", "<name> [generator]", "Resets the mine manually");
		ChatUtil.formatMessage("Resets the mine according to the generator defined by the configuration.");
		ChatUtil.formatMessage("The following generators are supported: ");
		ChatUtil.formatMessage(GeneratorUtil.list());
		return;
	}
}
