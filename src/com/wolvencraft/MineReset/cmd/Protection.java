package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;

public class Protection
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("edit"))
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
			String error = Language.getString("general.mine-not-selected");
			Util.sendError(error);
			return;
		}
		
		if(args[1].equalsIgnoreCase("pvp"))
		{
			String baseNode = "mines." + curMine + ".protection.pvp";
			if(Regions.getBoolean(baseNode + ".enabled"))
			{
				Regions.setBoolean(baseNode + ".enabled", false);
				Util.sendSuccess("PVP has been turned OFF for mine '" + curMine + "'");
			}
			else
			{
				Regions.setBoolean(baseNode + ".enabled", true);
				Util.sendSuccess("PVP has been turned ON for mine '" + curMine + "'");
			}
			Regions.saveData();
		}
		else if(args[1].equalsIgnoreCase("breaking"))
		{
			if(args.length < 3)
			{
				Util.sendInvalid(args);
				return;
			}
			String baseNode = "mines." + curMine + ".protection.breaking";
			if(args[2].equalsIgnoreCase("toggle"))
			{
				if(Regions.getBoolean(baseNode + ".enabled"))
				{
					Regions.setBoolean(baseNode + ".enabled", false);
					Util.sendSuccess("Mine protection has been turned OFF for mine '" + curMine + "'");
				}
				else
				{
					Regions.setBoolean(baseNode + ".enabled", true);
					Util.sendSuccess("Mine breaking protection has been turned ON for mine '" + curMine + "'");
				}
			}
			else if(args[2].equalsIgnoreCase("blacklist"))
			{
				if(args[3].equalsIgnoreCase("toggle"))
				{
					if(Regions.getBoolean(baseNode + ".blacklist.enabled"))
					{
						Regions.setBoolean(baseNode + ".blacklist.enabled", false);
						Util.sendSuccess("Block breaking protection blacklist has been turned OFF for mine '" + curMine + "'");
					}
					else
					{
						Regions.setBoolean(baseNode + ".blacklist.enabled", true);
						Util.sendSuccess("Block breaking protection blacklist has been turned ON for mine '" + curMine + "'");
					}
					
				}
				else if(args[3].equalsIgnoreCase("whitelist"))
				{
					if(Regions.getBoolean(baseNode + ".blacklist.whitelist"))
					{
						Regions.setBoolean(baseNode + ".blacklist.whitelist", false);
						Util.sendSuccess("Block breaking blacklist is no longer treated as a whitelist for mine '" + curMine + "'");
					}
					else
					{
						Regions.setBoolean(baseNode + ".blacklist.whitelist", true);
						Util.sendSuccess("Block breaking blacklist is now treated as a whitelist for mine '" + curMine + "'");
					}
				}
				else if(args[3].equalsIgnoreCase("add"))
				{
					String blockName = args[2];
					int blockId = Util.getBlockId(args[4]);
					
					if(blockId == -1)
					{
						Util.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<String> blockList = Regions.getList(baseNode + ".blacklist.blocks");
					
					
					// Writing everything down
					blockList.add(""+blockId);
					Regions.setList(baseNode + ".blacklist.blocks", blockList);
					
					Regions.saveData();
					
					Util.sendSuccess(blockName + " was added to the block breaking protection blacklist of '" + curMine + "'");
					return;
				}
				else if(args[3].equalsIgnoreCase("remove"))
				{
					int blockId = Util.getBlockId(args[4]);
					
					if(blockId == -1)
					{
						Util.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<String> blockList = Regions.getList(baseNode + ".blacklist.blocks");
					
					
					int index = blockList.indexOf("" + blockId);
					if(index == -1)
					{
						Util.sendError("There is no '" + args[4] + "' in protection blacklist of mine '" + curMine + "'");
						return;
					}
					blockList.remove(index);
					

					Regions.setList(baseNode + ".blacklist.blocks", blockList);
					
					Regions.saveData();
					Util.sendSuccess(args[4] + " was successfully removed from block breaking protection of mine '" + curMine + "'");
					return;
				}
				else
				{
					Util.sendInvalid(args);
					return;
				}
				Regions.saveData();
				return;
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
				if(Regions.getBoolean(baseNode + ".enabled"))
				{
					Regions.setBoolean(baseNode + ".enabled", false);
					Util.sendSuccess("Block placement protection has been turned OFF for mine '" + curMine + "'");
				}
				else
				{
					Regions.setBoolean(baseNode + ".enabled", true);
					Util.sendSuccess("Block placement protection has been turned ON for mine '" + curMine + "'");
				}
			}
			else if(args[2].equalsIgnoreCase("blacklist"))
			{
				if(args[3].equalsIgnoreCase("toggle"))
				{
					if(Regions.getBoolean(baseNode + ".blacklist.enabled"))
					{
						Regions.setBoolean(baseNode + ".blacklist.enabled", false);
						Util.sendSuccess("Block placement protection blacklist has been turned OFF for mine '" + curMine + "'");
					}
					else
					{
						Regions.setBoolean(baseNode + ".blacklist.enabled", true);
						Util.sendSuccess("Block placement protection blacklist has been turned ON for mine '" + curMine + "'");
					}
					
				}
				else if(args[3].equalsIgnoreCase("whitelist"))
				{
					if(Regions.getBoolean(baseNode + ".blacklist.whitelist"))
					{
						Regions.setBoolean(baseNode + ".blacklist.whitelist", false);
						Util.sendSuccess("Block breaking blacklist is no longer treated as a whitelist for mine '" + curMine + "'");
					}
					else
					{
						Regions.setBoolean(baseNode + ".blacklist.whitelist", true);
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
					
					List<String> blockList = Regions.getList(baseNode + ".blacklist.blocks");
					
					
					// Writing everything down
					blockList.add(""+blockID);
					Regions.setList(baseNode + ".blacklist.blocks", blockList);
					
					Regions.saveData();
					
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
					
					List<String> blockList = Regions.getList(baseNode + ".blacklist.blocks");
					
					
					int index = blockList.indexOf("" + blockID);
					if(index == -1)
					{
						Util.sendError("There is no '" + args[4] + "' in protection blacklist of mine '" + curMine + "'");
						return;
					}
					blockList.remove(index);
					

					Regions.setList(baseNode + ".blacklist.blocks", blockList);
					
					Regions.saveData();
					Util.sendSuccess(args[4] + " was successfully removed from block placement protection of mine '" + curMine + "'");
					return;
				}
				else
				{
					Util.sendInvalid(args);
					return;
				}

				Regions.saveData();
				return;
			}
			else
			{
				Util.sendInvalid(args);
				return;
			}
		}
		else
		{
			Util.sendInvalid(args);
			return;
		}
	}
}
