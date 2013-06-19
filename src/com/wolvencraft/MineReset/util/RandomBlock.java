/*
 * RandomBlock.java
 * 
 * MineReset
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.MineReset.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.wolvencraft.MineReset.mine.MineBlock;
import org.bukkit.material.MaterialData;


public class RandomBlock {
 
    List<MineBlock> weightedBlocks;
    
    /**
     * Creates a new RandomBlock instance with the composition provided
     * @param blocks Mine composition
     */
    public RandomBlock(List<MineBlock> blocks) {
        weightedBlocks = new ArrayList<MineBlock>();
        double tally = 0;
        for (MineBlock block : blocks) {
            tally += block.getChance();
            weightedBlocks.add(new MineBlock(block.getBlock(), tally));
            ChatUtil.debug("Block " + block.getBlock().getItemTypeId() + " was assigned the tally weight of " + tally);
        }
        ChatUtil.debug("RandomBlock initialized");
    }
    
    /**
     * Returns a random block according to the mine's composition
     * @return Random block
     */
    public MaterialData next()
    {
        double r = new Random().nextDouble();
        for (MineBlock block : weightedBlocks) {
            if (r <= block.getChance()) {
                return block.getBlock();
            }
        }
        //At this point, we've got a problem folks.
        return null;
    }

}