package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.CommandManager;

public class Protection
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("edit", true))
		{
			Util.sendDenied(args);
			return;
		}
		
		if(args.length == 1)
		{
			Help.getProtection();
			return;
		}
		if(args.length > 4)
		{
			Util.sendInvalid(args);
		}
		
		String curMine = CommandManager.getMine();
		
		if(curMine == null)
		{
			Util.sendError("Select a mine first with /mine edit <name>");
			return;
		}
		
		if(args[1].equalsIgnoreCase("breaking"))
		{
			if(args[2].equalsIgnoreCase("toggle"))
			{
				
			}
			else if(args[2].equalsIgnoreCase("blacklist"))
			{
				if(args[3].equalsIgnoreCase("toggle"))
				{
					
				}
				else if(args[3].equalsIgnoreCase("whitelist"))
				{
					
				}
				else if(args[3].equalsIgnoreCase("add"))
				{
					
				}
				else if(args[3].equalsIgnoreCase("remove"))
				{
					
				}
				else
				{
					Util.sendInvalid(args);
				}
			}
			else
			{
				Util.sendInvalid(args);
			}
		}
		else if(args[1].equalsIgnoreCase("placement"))
		{
			if(args[2].equalsIgnoreCase("toggle"))
			{
				
			}
			else if(args[2].equalsIgnoreCase("blacklist"))
			{
				if(args[3].equalsIgnoreCase("toggle"))
				{
					
				}
				else if(args[3].equalsIgnoreCase("whitelist"))
				{
					
				}
				else if(args[3].equalsIgnoreCase("add"))
				{
					
				}
				else if(args[3].equalsIgnoreCase("remove"))
				{
					
				}
				else
				{
					Util.sendInvalid(args);
				}
			}
			else
			{
				Util.sendInvalid(args);
			}
		}
		else
		{
			Util.sendInvalid(args);
		}
	}
}
