package com.wolvencraft.MineReset.cmd;

import java.util.List;

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
		if(args.length > 5)
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
			String baseNode = "mines." + curMine + ".protection.breaking";
			if(args[2].equalsIgnoreCase("toggle"))
			{
				if(Util.getRegionBoolean(baseNode + ".enabled"))
				{
					Util.setRegionBoolean(baseNode + ".enabled", false);
					Util.sendSuccess("Mine protection has been turned off for mine '" + curMine + "'");
				}
				else
				{
					Util.setRegionBoolean(baseNode + ".enabled", true);
					Util.sendSuccess("Mine breaking protection has been turned on for mine '" + curMine + "'");
				}
			}
			else if(args[2].equalsIgnoreCase("blacklist"))
			{
				if(args[3].equalsIgnoreCase("toggle"))
				{
					if(Util.getRegionBoolean(baseNode + ".blacklist.enabled"))
					{
						Util.setRegionBoolean(baseNode + ".blacklist.enabled", false);
						Util.sendSuccess("Block breaking protection blacklist has been turned off for mine '" + curMine + "'");
					}
					else
					{
						Util.setRegionBoolean(baseNode + ".blacklist.enabled", true);
						Util.sendSuccess("Block breaking protection blacklist has been turned on for mine '" + curMine + "'");
					}
					
				}
				else if(args[3].equalsIgnoreCase("whitelist"))
				{
					if(Util.getRegionBoolean(baseNode + ".blacklist.whitelist"))
					{
						Util.setRegionBoolean(baseNode + ".blacklist.whitelist", false);
						Util.sendSuccess("Block breaking blacklist is no longer treated as a whitelist for mine '" + curMine + "'");
					}
					else
					{
						Util.setRegionBoolean(baseNode + ".blacklist.whitelist", true);
						Util.sendSuccess("Block breaking blacklist is now treated as a whitelist for mine '" + curMine + "'");
					}
				}
				else if(args[3].equalsIgnoreCase("add"))
				{
					String blockName = args[2];
					int blockID = Util.getBlockId(args[4]);
					
					if(blockID == -1)
					{
						Util.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<String> blockList = Util.getRegionList(baseNode + ".blacklist.blocks");
					
					
					// Writing everything down
					blockList.add(""+blockID);
					Util.setRegionList(baseNode + ".blacklist.blocks", blockList);
					
					Util.saveRegionData();
					
					Util.sendSuccess(blockName + " was added to the block breaking protection blacklist of '" + curMine + "'");
					return;
				}
				else if(args[3].equalsIgnoreCase("remove"))
				{
					int blockID = Util.getBlockId(args[4]);
					
					if(blockID == -1)
					{
						Util.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<String> blockList = Util.getRegionList(baseNode + ".blacklist.blocks");
					
					
					int index = blockList.indexOf("" + blockID);
					if(index == -1)
					{
						Util.sendError("There is no '" + args[4] + "' in protection blacklist of mine '" + curMine + "'");
						return;
					}
					blockList.remove(index);
					

					Util.setRegionList(baseNode + ".blacklist.blocks", blockList);
					
					Util.saveRegionData();
					Util.sendSuccess(args[4] + " was successfully removed from block breaking protection mine '" + curMine + "'");
					return;
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
			String baseNode = "mines." + curMine + ".protection.placement";
			if(args[2].equalsIgnoreCase("toggle"))
			{
				if(Util.getRegionBoolean(baseNode + ".enabled"))
				{
					Util.setRegionBoolean(baseNode + ".enabled", false);
					Util.sendSuccess("Block placement protection has been turned off for mine '" + curMine + "'");
				}
				else
				{
					Util.setRegionBoolean(baseNode + ".enabled", true);
					Util.sendSuccess("Block placement protection has been turned on for mine '" + curMine + "'");
				}
			}
			else if(args[2].equalsIgnoreCase("blacklist"))
			{
				if(args[3].equalsIgnoreCase("toggle"))
				{
					if(Util.getRegionBoolean(baseNode + ".blacklist.enabled"))
					{
						Util.setRegionBoolean(baseNode + ".blacklist.enabled", false);
						Util.sendSuccess("Block placement protection blacklist has been turned off for mine '" + curMine + "'");
					}
					else
					{
						Util.setRegionBoolean(baseNode + ".blacklist.enabled", true);
						Util.sendSuccess("Block placement protection blacklist has been turned on for mine '" + curMine + "'");
					}
					
				}
				else if(args[3].equalsIgnoreCase("whitelist"))
				{
					if(Util.getRegionBoolean(baseNode + ".blacklist.whitelist"))
					{
						Util.setRegionBoolean(baseNode + ".blacklist.whitelist", false);
						Util.sendSuccess("Block breaking blacklist is no longer treated as a whitelist for mine '" + curMine + "'");
					}
					else
					{
						Util.setRegionBoolean(baseNode + ".blacklist.whitelist", true);
						Util.sendSuccess("Block breaking blacklist is now treated as a whitelist for mine '" + curMine + "'");
					}
				}
				else if(args[3].equalsIgnoreCase("add"))
				{
					String blockName = args[2];
					int blockID = Util.getBlockId(args[4]);
					
					if(blockID == -1)
					{
						Util.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<String> blockList = Util.getRegionList(baseNode + ".blacklist.blocks");
					
					
					// Writing everything down
					blockList.add(""+blockID);
					Util.setRegionList(baseNode + ".blacklist.blocks", blockList);
					
					Util.saveRegionData();
					
					Util.sendSuccess(blockName + " was added to the block placement protection blacklist of '" + curMine + "'");
					return;
				}
				else if(args[3].equalsIgnoreCase("remove"))
				{
					int blockID = Util.getBlockId(args[4]);
					
					if(blockID == -1)
					{
						Util.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<String> blockList = Util.getRegionList(baseNode + ".blacklist.blocks");
					
					
					int index = blockList.indexOf("" + blockID);
					if(index == -1)
					{
						Util.sendError("There is no '" + args[4] + "' in protection blacklist of mine '" + curMine + "'");
						return;
					}
					blockList.remove(index);
					

					Util.setRegionList(baseNode + ".blacklist.blocks", blockList);
					
					Util.saveRegionData();
					Util.sendSuccess(args[4] + " was successfully removed from block placement protection mine '" + curMine + "'");
					return;
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
