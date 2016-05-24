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

import com.samistine.samistineutilities.api.objects.FeatureInfo;
import com.samistine.samistineutilities.api.objects.FeatureLogger;
import com.samistine.samistineutilities.SamistineUtilities;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Samuel Seidel
 */
public abstract class SFeature implements Feature {

    private final String name;
    private final String desc;
    private final Logger logger;

    {
        FeatureInfo annotation = getClass().getAnnotation(FeatureInfo.class);
        Validate.notNull(annotation, "Implementing class must contain FeatureInfo anotation");
        this.name = annotation.name();
        this.desc = annotation.desc();
        this.logger = new FeatureLogger(this);
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String getDesc() {
        return desc;
    }

    public final Logger getLogger() {
        return logger;
    }

    /**
     * Called when the feature is being disabled.
     * <br>
     * All listeners will be automatically deRegistered after this method call
     */
    public void onDisable() {
    }

    public final SamistineUtilities getPlugin() {
        return SamistineUtilities.getInstance();
    }

    public final Server getServer() {
        return getPlugin().getServer();
    }

    public final FileConfiguration getRootConfig() {
        return getPlugin().getConfig();
    }

}
