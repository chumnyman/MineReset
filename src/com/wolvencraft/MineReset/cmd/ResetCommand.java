package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.mine.Generator;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.Broadcast;
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
			if(!Util.senderHasPermission("reset.manual." + curMine.getName()) && !Util.senderHasPermission("reset.manual"))
			{
				Message.sendDenied(args);
				return;
			}
		}
		
		Generator generator;
		if(forcedGenerator == null)
			generator = curMine.getGenerator();
		else
			generator = Generator.valueOf(forcedGenerator);
		
		curMine.reset(generator);
		curMine.resetTimer();
		
		String broadcastMessage;
		if(automatic) {
			List<Mine> mines = MineReset.getMines();
			for(Mine childMine : mines) {
				if(childMine.getParent() != null)
				{
					String[] childArgs = {"", childMine.getName()};
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
			Broadcast.sendSuccess(broadcastMessage);
		}
		else if(!automatic) {
			Message.sendSuccess(broadcastMessage);
		}
		return;
	}
}
