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

import com.samistine.samistineutilities.api.SFeature;
import com.samistine.samistineutilities.api.objects.FeatureInfo;

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

    private final JCPDListenerSimple listener = new JCPDListenerSimple();
    private final JCPDListenerRegions listener2 = new JCPDListenerRegions();

    public JCPhysicsDisabler() {
        //Register Command
        new JCPDCommandExecutor(this).registerCommand(this);
    }

    public void setDisabled(int i) {
        listener.unregisterListener(this);
        listener2.unregisterListener(this);

        if (i == 1) {
            listener.registerListener(this);
        } else if (i == 2) {
            listener2.registerListener(this);
        }
    }

    public void addRegion(String name, JCPDRegion region) {
        listener2.addRegion(name, region);
    }

    public void removeRegion(String name) {
        listener2.removeRegion(name);
    }

    public boolean existsRegion(String name) {
        return listener2.existsRegion(name);
    }
}
