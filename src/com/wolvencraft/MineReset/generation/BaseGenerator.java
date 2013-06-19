/*
 * BaseGenerator.java
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

package com.wolvencraft.MineReset.generation;

import com.wolvencraft.MineReset.mine.Mine;

public interface BaseGenerator {
    
    /**
     * Resets the mine specified in a parameter. The method is used both in automatic and manual resets.
     * <br /><br />
     * 
     * Return <b>true</b> if mine reset is successful, <b>false</b> if a critical error occurs. If the
     * method returns false, the current command will be aborted. Don't forget to send your own error
     * messages as well.
     * @param mine Mine to be reset
     * @return true is reset was successful, false otherwise
     */
    public boolean run(Mine mine);
    
    /**
     * This method is utilized when the generator is being initialized. The initialization might occur on
     * two occasions: /mine save and /mine generator. In both cases, the method is invoked right before
     * the mine data is saved.<br /><br />
     * 
     * Return <b>true</b> if initialization is successful, <b>false</b> if a critical error occurs. If the
     * method returns false, the current command will be aborted. Don't forget to send your own error
     * messages as well.<br /><br />
     * 
     * If your generator does not require initialization, leave this method empty, returning <b>true</b>.
     * @param mine Mine to be used with the generator
     * @return true if initialization was successful, false otherwise
     */
    public boolean init(Mine mine);
    
    /**
     * This method is invoked when a mine is being explicitly removed through a command.<br /><br />
     * 
     * Return <b>true</b> if initialization is successful, <b>false</b> if a critical error occurs. If the
     * method returns false, the current command will be aborted. Don't forget to send your own error
     * messages as well.
     * @param mine Mine being removed
     * @return true if removal was successful, false if it was not
     */
    public boolean remove(Mine mine);
    
    /**
     * Returns the name of the generator. By convention, this is the first part of your generator name, in
     * upper case. For example:<br /><br />
     * 
     * RandomGenerator -> <b>RANDOM</b>
     * @return The name of the generator
     */
    public String getName();
    
    /**
     * Returns the description of the generator. The description should be of 127 symbols or less, so keep
     * it straight-to-the-point.
     * @return Description of the generator
     */
    public String getDescription();
}
