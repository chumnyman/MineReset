package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.mine.Generator;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.MineUtils;
import com.wolvencraft.MineReset.util.Util;

public class ResetCommand
{	
	public static void run(String[] args, boolean automatic, String forcedGenerator)
	{	
		Mine curMine;
		if(args.length == 1)
			curMine = CommandManager.getMine();
		else
			curMine = MineUtils.getMine(args[1]);
		
		if(curMine == null) {
			Message.sendMineNotSelected();
			return;
		}
		
		if(!automatic) {
			if(!Util.senderHasPermission("reset.manual." + curMine.getName()) && !Util.senderHasPermission("reset.manual")) {
				Message.sendDenied(args);
				return;
			}
			
			if(!Util.senderHasPermission("reset.cooldown") && curMine.getNextCooldown() > 0) {
				Message.sendError("You can reset the mine in " + Util.parseSeconds(curMine.getNextCooldown()));
				return;
			}
		}
		
		if(args.length == 3)
			forcedGenerator = args[2];
		
		Generator generator;
		if(forcedGenerator == null)
			generator = curMine.getGenerator();
		else {
			try {
			generator = Generator.valueOf(forcedGenerator);
			}
			catch(IllegalArgumentException iae) {
				Message.sendError("This generator does not exist!");
				return;
			}
		}
		
		curMine.reset(generator);
		
		String broadcastMessage;
		if(automatic) {
			List<Mine> mines = MineReset.getMines();
			for(Mine childMine : mines) {
				if(MineUtils.getMine(childMine.getParent()) != null) {
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
			Message.broadcast(broadcastMessage);
		}
		else if(!automatic) {
			Message.sendSuccess(broadcastMessage);
		}
		return;
	}
}
