package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;

public class ResetCommand
{	
	public static void run(String[] args, boolean automatic, String forcedGenerator)
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
		
		if(!automatic) {
			if(!Util.hasPermission("reset.manual." + curMine.getName()) && !Util.hasPermission("reset.manual")) {
				ChatUtil.sendDenied(args);
				return;
			}
			
			if(!Util.hasPermission("reset.bypass") && curMine.getNextCooldown() > 0) {
				ChatUtil.sendError("You can reset the mine in " + Util.parseSeconds(curMine.getNextCooldown()));
				return;
			}
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
		
		String broadcastMessage;
		if(automatic) {
			List<Mine> mines = MineReset.getMines();
			for(Mine childMine : mines) {
				ChatUtil.debug(childMine.getParent());
				if(childMine.getParent() != null) {
					String[] childArgs = {null, childMine.getName()};
					run(childArgs, true, null);
				}
			}
			
			broadcastMessage = Language.getString("reset.automatic-reset-successful");
		}
		else {
			broadcastMessage = Language.getString("reset.manual-reset-successful");
		}
		
		broadcastMessage = Util.parseVars(broadcastMessage, curMine);
		
		if(!curMine.getSilent()) {
			ChatUtil.broadcast(broadcastMessage);
		}
		else if(!automatic) {
			ChatUtil.sendSuccess(broadcastMessage);
		}
		return;
	}
}
