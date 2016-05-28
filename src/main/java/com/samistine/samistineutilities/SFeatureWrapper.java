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
package com.samistine.samistineutilities;

import com.samistine.samistineutilities.api.objects.FeatureInfo;
import com.samistine.samistineutilities.api.SFeature;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;

/**
 *
 * @author Samuel Seidel
 * @param <T> The class that this wrapper is wrapping
 */
public final class SFeatureWrapper<T extends SFeature> {

    private static final Logger logger = SamistineUtilities.getInstance().getLogger();

    private final Class<T> clazz;
    private final FeatureInfo featureInfo;

    public SFeatureWrapper(Class<T> clazz) {
        this.clazz = clazz;
        this.featureInfo = clazz.getAnnotation(FeatureInfo.class);
        Validate.notNull(featureInfo, "Features must contain the FeatureInfo annotation");
    }

    private T feature;

    public String getName() {
        return featureInfo.name();
    }

    public T getFeature() {
        return feature;
    }

    public void enable() {
        try {
            feature = clazz.newInstance();
            feature.enable();
        } catch (InstantiationException | IllegalAccessException | RuntimeException ex) {
            logger.log(Level.SEVERE, "Could not instantiate " + featureInfo.name(), ex);
        }
    }

    public void disable() {
        if (feature != null) {
            try {
                feature.disable();
            } catch (RuntimeException ex) {
                logger.log(Level.SEVERE, "An exception was thrown while attempting to disable " + featureInfo.name(), ex);
            }

            //Remove references
            feature = null;

        } else {
            //feature wasn't enabled
        }
    }

}
