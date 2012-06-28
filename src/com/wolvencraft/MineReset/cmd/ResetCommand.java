package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.Reset;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;

public class ResetCommand
{	
	public static void run(String[] args, Reset source, String forcedGenerator)
	{	
		Mine curMine;
		if(args.length == 1)
			curMine = CommandManager.getMine();
		else
			curMine = MineUtil.getMine(args[1]);
		
		if(curMine == null) {
			ChatUtil.sendMineNotSelected();
			return;
		}
		
		if(source.equals(Reset.AUTOMATIC)) {
			if(!Util.hasPermission("reset.manual." + curMine.getName()) && !Util.hasPermission("reset.manual")) {
				ChatUtil.sendDenied(args);
				return;
			}
			
			if(curMine.getCooldown() && curMine.getNextCooldown() > 0 && !Util.hasPermission("reset.bypass")) {
				ChatUtil.sendError(Util.parseVars(Language.getString("reset.mine-cooldown"), curMine));
				return;
			}
			
			MineReset.getStats().updateViaCommand();
		}
		
		if(args.length == 3)
			forcedGenerator = args[2];
		
		String generator;
		if(forcedGenerator == null)
			generator = curMine.getGenerator();
		else {
			generator = forcedGenerator;
		}
		
		if(!curMine.reset(generator)) return;
		
		if(curMine.getAutomatic()) curMine.resetTimer();
		if(curMine.getCooldown()) curMine.resetCooldown();
		
		String broadcastMessage = Language.getString("reset.manual-reset-successful");
		if(source.equals(Reset.AUTOMATIC)) {
			List<Mine> mines = MineReset.getMines();
			for(Mine childMine : mines) {
				ChatUtil.debug(childMine.getParent());
				if(childMine.getParent() != null) {
					String[] childArgs = {null, childMine.getName()};
					run(childArgs, Reset.AUTOMATIC, null);
				}
			}
			
			broadcastMessage = Language.getString("reset.automatic-reset-successful");
		}
		
		broadcastMessage = Util.parseVars(broadcastMessage, curMine);
		
		if(!curMine.getSilent()) {
			ChatUtil.broadcastWorld(broadcastMessage, curMine.getWorld());
		}
		else if(!source.equals(Reset.AUTOMATIC)) {
			ChatUtil.sendSuccess(broadcastMessage);
		}
		return;
	}
}
