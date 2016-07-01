/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Jasper Nalbach
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.samistine.samistineutilities.features.physicsdisabler;

import com.samistine.samistineutilities.SamistineUtilities;
import com.samistine.samistineutilities.api.FeatureInfo;
import com.samistine.samistineutilities.api.SFeature;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

/**
 * <h1>PhysicsDisabler</h1>
 *
 * <div>
 * <h2>Description:</h2>
 * <blockquote>
 * <p>
 * This is a tool to disable Minecrafts block updates, item- and creature
 * spawning with a single command. It's useful for illegal block placements with
 * WorldEdit or bad CraftBook-spawners, helps with most of buggy areas.</p>
 * <p>
 * Just type disablephysics mode on and fix the issues with WorldEdit etc and
 * re-enable physics with disablephysics mode off. For more details see "Usage"
 * section.</p>
 * <p>
 * The plugin does not affect the server performance, event-handlers will be
 * unregistered after re-enabling block updates. So you do not have to uninstall
 * it after use.</p>
 * </blockquote>
 * </div>
 *
 * <div>
 * <h2>Usage:</h2>
 * <blockquote>
 * <pre>Command syntax: /disablephysics [ mode [on|off|region] | addregion (regionname) (worldname) [(x)|(y)|(z) (x)|(y)|(z)] | removeregion (regionname) ]</pre>
 * <hr>
 * <pre>
 *  disablephysics mode on - Disable block updates, item- and creature spawing everywhere on the server
 *
 *  disablephysics mode off - Re-enable physics
 *
 *  disablephysics mode region - Disable physics in selected regions
 *
 *  disablephysics addregion (regionname) (worldname) - Add a world as region
 *
 *  disablephysics addregion (regionname) (worldname) (x)|(y)|(z) (x)|(y)|(z) - Add a region between two coordinates.
 *
 *  disablephysics removeregion (regionname) - Remova a region.
 *
 * </pre>
 * </blockquote>
 * </div>
 *
 * @author Jasper Nalbach
 * @version 1.2
 */
@FeatureInfo(name = "PhysicsDisabler", desc = "Block title entities from ticking in specific/all places", commands = "/disablephysics")
public final class JCPhysicsDisabler extends SFeature {

    private final JCPDCommandExecutor command = new JCPDCommandExecutor(this);
    private final JCPDListenerSimple listenerSimple = new JCPDListenerSimple();
    private final JCPDListenerRegions listenerRegions = new JCPDListenerRegions();

    public JCPhysicsDisabler(SamistineUtilities main) {
        super(main);
    }

    @Override
    protected void onEnable() {
        //Register Command
        registerCommand(command);
    }

    @Override
    protected void onDisable() {
        //UNRegister Command
        unregisterCommand(command);
    }

    /**
     * Sets the mode.
     * <ul>
     * <li>0 - Allow all physics updates</li>
     * <li>1 - Block all physics updates</li>
     * <li>2 - Block physics updates in registered regions</li>
     * </ul>
     *
     * @param i mode to set
     */
    public void setMode(int i) {
        HandlerList.unregisterAll(listenerSimple);
        HandlerList.unregisterAll(listenerRegions);

        PluginManager plm = getServer().getPluginManager();
        switch (i) {
            case 0:
                break;
            case 1:
                plm.registerEvents(listenerSimple, featurePlugin);
                break;
            case 2:
                plm.registerEvents(listenerRegions, featurePlugin);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void addRegion(String name, JCPDRegion region) {
        listenerRegions.addRegion(name, region);
    }

    public void removeRegion(String name) {
        listenerRegions.removeRegion(name);
    }

    public boolean existsRegion(String name) {
        return listenerRegions.existsRegion(name);
    }

}
