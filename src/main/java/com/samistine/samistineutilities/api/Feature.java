/*
 * The MIT License
 *
 * Copyright 2016 Samuel Seidel.
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

import com.samistine.samistineutilities.SamistineUtilities;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Samuel Seidel
 */
public interface Feature {

    /**
     * The name of this feature
     *
     * @return name
     */
    public String getName();

    /**
     * A short one-to-two liner description about this feature
     *
     * @return description
     */
    public String getDesc();

    /**
     * Gets the logger for the feature.
     * <p>
     * This logger contains the features {@link #getName() name} to assist in
     * debugging.
     *
     * @return logger
     */
    public Logger getLogger();

    /**
     * Gets the running minecraft(bukkit) server.
     *
     * @return server
     */
    public Server getServer();

    /**
     * Get the plugin that this feature is running under
     *
     * @return plugin
     */
    public SamistineUtilities getRootPlugin();

    /**
     * Gets the {@link SamistineUtilities plugin}'s root/main config.
     *
     * @return config
     */
    public FileConfiguration getRootConfig();

}