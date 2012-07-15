package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Reset;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.GeneratorUtil;
import com.wolvencraft.MineReset.util.MineError;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;

public class ResetCommand
{	
	public static void run(String mineName, Reset source, String forcedGenerator) {
		String[] args = {null, mineName};
		run(args, source, forcedGenerator);
	}
	
	public static void run(String[] args, Reset source, String forcedGenerator)
	{	
		Mine curMine;
		if(args.length == 1) curMine = CommandManager.getMine();
		else curMine = MineUtil.getMine(args[1]);
		
		if(curMine == null) {
			if(args.length == 1) getHelp();
			ChatUtil.sendInvalid(MineError.MINE_NOT_SELECTED, args);
			return;
		}

		ChatUtil.debug("Resettign mine: " + curMine.getName());
		
		if(source.equals(Reset.MANUAL)) {
			if(!Util.hasPermission("reset.manual." + curMine.getName()) && !Util.hasPermission("reset.manual")) {
				ChatUtil.sendInvalid(MineError.ACCESS, args);
				return;
			}
			
			if(curMine.getCooldown() && curMine.getNextCooldown() > 0 && !Util.hasPermission("reset.bypass")) {
				ChatUtil.sendError(Util.parseVars(Language.getString("reset.mine-cooldown"), curMine));
				return;
			}
			
			MineReset.getStats().updateViaCommand();
		}
		
		if(args.length == 3) forcedGenerator = args[2];
		
		String generator = curMine.getGenerator();
		if(forcedGenerator.equals("")) generator = curMine.getGenerator();
		
		if(curMine.getAutomatic()) curMine.resetTimer();
		if(curMine.getCooldown()) curMine.resetCooldown();
		
		if(!(curMine.reset(generator))) return;
		
		String broadcastMessage;
		if(source.equals(Reset.AUTOMATIC)) {
			List<Mine> mines = MineReset.getMines();
			for(Mine childMine : mines) {
				Mine parent = MineUtil.getMine(childMine.getParent());
				if(parent != null & parent.equals(curMine)) {
					run(childMine.getName(), Reset.AUTOMATIC, null);
				}
			}
			
			broadcastMessage = Language.getString("reset.automatic-reset-successful");
		}
		else broadcastMessage = Language.getString("reset.manual-reset-successful");
		
		broadcastMessage = Util.parseVars(broadcastMessage, curMine);
		
		if(!curMine.getSilent()) ChatUtil.broadcast(broadcastMessage, curMine.getWorld());
		else if(!source.equals(Reset.AUTOMATIC)) ChatUtil.sendSuccess(broadcastMessage);
		return;
	}
	
	public static void getHelp() {
		ChatUtil.formatHeader(20, "Reset");
		ChatUtil.formatHelp("reset", "<name> [generator]", "Resets the mine manually");
		ChatUtil.formatMessage("Resets the mine according to the generator defined by the configuration.");
		ChatUtil.formatMessage("The following generators are supported: ");
		ChatUtil.formatMessage(GeneratorUtil.list());
		return;
	}
}
