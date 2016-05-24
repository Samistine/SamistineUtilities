/*
 * The MIT License
 *
 * Copyright 2016 Samuel.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.samistine.samistineutilities.api;

import com.samistine.samistineutilities.ListenerManager;
import com.samistine.samistineutilities.SamistineUtilities;
import org.bukkit.event.Listener;

/**
 *
 * @author Samuel
 */
public interface SListener extends Listener {

    /**
     *
     * @param <T>
     * @param feature the feature to register this listener to
     * @return this for easy chaining
     */
    public default <T extends SListener> T registerListener(Feature feature) {
        ListenerManager manager = SamistineUtilities.getInstance().getHandlerManager();
        manager.registerListener(feature, this);
        return (T) this;
    }

    /**
     *
     * @param <T>
     * @param feature the feature that owns this listener
     * @return this for easy chaining
     */
    public default <T extends SListener> T unRegisterListener(Feature feature) {
        ListenerManager manager = SamistineUtilities.getInstance().getHandlerManager();
        manager.unRegisterListener(feature, this);
        return (T) this;
    }

}
