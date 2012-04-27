package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;

public class Edit
{
	private static String curMine;
	
	// TODO Add a method to set up a mine spawn point
	
	public static void run(String[] args)
	{
		if(Util.isPlayer())
			if(!Util.senderHasPermission("edit"))
			{
				Util.sendDenied(args);
				return;
			}
		
		if(args.length == 1)
		{
			Help.getEdit();
			return;
		}
		if(args.length > 3)
		{
			Util.sendInvalid(args);
		}
		
		curMine = CommandManager.getMine();
		
		if(args[0].equalsIgnoreCase("edit"))
		{
			if(args.length != 2)
			{
				Util.sendInvalid(args);
				return;
			}
			String mineName = args[1];
			if(!Util.mineExists(mineName))
			{
				Util.sendError("Mine '" + mineName + "' does not exist");
				return;
			}
			CommandManager.setMine(mineName);
			Util.sendSuccess("Mine '" + mineName + "' has been selected");
		}
		else if(args[0].equalsIgnoreCase("none"))
		{
			if(args.length != 2)
			{
				Util.sendInvalid(args);
				return;
			}
			String mineName = args[1];
			if(!Util.mineExists(mineName))
			{
				Util.sendError("Mine '" + mineName + "' does not exist");
				return;
			}
			CommandManager.setMine(null);
			Util.sendSuccess("Mine '" + mineName + "' has been de-selected");
		}
		else if(args[0].equalsIgnoreCase("cooldown"))
		{
			if(curMine == null)
			{
				Util.sendError("Select a mine first with /mine edit <name>");
				return;
			}
			
			if(args.length != 3)
			{
				Util.sendInvalid(args);
				return;
			}
			
			if(curMine == null)
			{
				Util.sendError("Select a mine first with /mine edit <name>");
				return;
			}
			
			if(Util.isNumeric(args[2]))
			{
				Util.setRegionInt("mines." + curMine + ".reset.manual", Integer.parseInt(args[2]));
				Util.sendSuccess("The cooldown of mine '" + curMine + "' has been set to " + args[2]);
				return;
			}
			else if(args[2].equalsIgnoreCase("toggle"))
			{
				return;
			}
			else
			{
				Util.sendInvalid(args);
				return;
			}
		}
		else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("+"))
		{
			if(curMine == null)
			{
				Util.sendError("Select a mine first with /mine edit <name>");
				return;
			}
			
			if(args.length != 3)
			{
				Util.sendError("I find your lack of parameters disturbing");
				return;
			}
			String blockName = args[1];
			int blockId = Util.getBlockId(blockName);
			
			if(blockId == -1)
			{
				Util.sendError("Block '"+ args[1] + "' does not exist");
				return;
			}
			
			
			List<String> itemList = Util.getRegionList("mines." + curMine + ".materials.blocks");
			List<String> weightList = Util.getRegionList("mines." + curMine + ".materials.weights");
			

			if(blockId == Integer.parseInt(itemList.get(0)))
			{
				Util.sendError("You do not need to do this. The weight of the default block is calculated automatically.");
				return;
			}
			
			double percent;
			if(Util.isNumeric(args[2]))
			{
				percent = Double.parseDouble(args[2]);
				if(percent <= 0) Util.sendInvalid(args);
				percent = (double)(Math.round(percent * 1000)) / 1000;
			}
			else {
				if(Util.debugEnabled()) Util.log("Argument is not numeric, attempting to parse");
				String awkwardValue = args[2];
				String[] awkArray = awkwardValue.split("%");
				try
				{
				percent = Double.parseDouble(awkArray[0]);
				}
				catch(NumberFormatException nfe)
				{
					Util.sendInvalid(args);
					return;
				}
			}
			if(Util.debugEnabled()) Util.log("Percent value is " + percent);
			
			double percentAvailable = Double.parseDouble(weightList.get(0));
			double newStonePercent;
			if((percentAvailable - percent) < 0)
			{
				Util.sendError("Invalid percentage. Use /mine info " + curMine + " to review the percentages");
				return;
			}
			else newStonePercent = percentAvailable - percent;
			
			newStonePercent = (double)(Math.round(newStonePercent * 1000)) / 1000;
			int index = itemList.indexOf("" + blockId);
			if(Util.debugEnabled()) Util.log(blockId + " ? " + index);
			if(index == -1)
			{
				itemList.add(blockId + "");
				weightList.add(""+percent);
			}
			else
			{
				String weight = Double.parseDouble(weightList.get(index)) + percent + "";
				weightList.set(index, weight);
			}
			// Writing everything down
			
			weightList.set(0, ""+newStonePercent);
			Util.setRegionList("mines." + curMine + ".materials.blocks", itemList);
			Util.setRegionList("mines."+ curMine + ".materials.weights", weightList);
			
			Util.saveRegionData();
			
			Util.sendSuccess(percent + "% of " + blockName + " added to " + curMine);
			Util.sendSuccess("Reset the mine for the changes to take effect");
			return;
		}
		else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("-"))
		{
			if(curMine == null)
			{
				Util.sendError("Select a mine first with /mine edit <name>");
				return;
			}
			
			int blockId = Util.getBlockId(args[1]);
			
			if(blockId == -1)
			{
				Util.sendError("Block '"+ args[1] + "' does not exist");
				return;
			}
			
			if(blockId == Util.getConfigInt("defaults.materials.default-block"))
			{
				Util.sendError("You cannot remove the default block from the mine");
				return;
			}
			
			List<String> itemList = Util.getRegionList("mines." + curMine + ".materials.blocks");
			List<String> weightList = Util.getRegionList("mines." + curMine + ".materials.weights");
			
			
			int index = itemList.indexOf("" + blockId);
			if(Util.debugEnabled()) Util.log(blockId + " ? " + index);
			if(index == -1)
			{
				Util.sendError("There is no '" + args[2] + "' in mine '" + curMine + "'");
				return;
			}
			double oldStoneWeight = Double.parseDouble(weightList.get(0));
			double newStoneWeight = oldStoneWeight + Double.parseDouble("" + weightList.get(index));
			weightList.set(0, "" + newStoneWeight);
			itemList.remove(index);
			weightList.remove(index);
			

			Util.setRegionList("mines." + curMine + ".materials.blocks", itemList);
			Util.setRegionList("mines." + curMine + ".materials.weights", weightList);
			
			Util.saveRegionData();
			Util.sendSuccess(args[1] + " was successfully removed from mine '" + curMine + "'");
			return;
		}
		else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del"))
		{
			CommandManager.getPlugin().getRegionData().set("mines." + curMine, null);
			List<String> regionList = Util.getRegionList("data.list-of-mines");
			regionList.remove(regionList.indexOf(args[1]));
			Util.setRegionList("data.list-of-mines", regionList);
			Util.saveRegionData();
			CommandManager.setMine(null);
			Util.sendSuccess("Mine '" + args[1] + "' removed successfully");
			return;
		}
		else if(args[0].equalsIgnoreCase("name"))
		{
			if(curMine == null)
			{
				Util.sendError("Select a mine first with /mine edit <name>");
				return;
			}
			
			String name = args[1];
			for(int i = 2; i < args.length; i++)
			{
				name = name + " " + args[i];
			}
			Util.setRegionString("mines." + curMine + ".display-name", name);
			Util.sendSuccess("Mine '" + curMine + "' now has a display name '" + name + "'");
			Util.saveRegionData();
			return;
		}
		else
		{
			Util.sendInvalid(args);
			return;
		}
	}
}
