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

import com.samistine.samistineutilities.FeatureHelper;
import com.samistine.samistineutilities.SamistineUtilities;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Samuel Seidel
 */
public final class FeatureManagerHelper {

    public static SFeature init(SamistineUtilities main, FeatureHelper feature) {
        Logger logger = SamistineUtilities.getInstance().getLogger();
        try {
            logger.log(Level.INFO, "Initializing {0}", feature.getName());
            Class<SFeature> clazz = (Class<SFeature>) feature.getClazz();
            Constructor<SFeature> constructor = clazz.getDeclaredConstructor(new Class[]{SamistineUtilities.class});
            logger.log(Level.FINE, "Initialized {0}", feature.getName());
            return constructor.newInstance(main);
        } catch (InstantiationException | IllegalAccessException | RuntimeException | NoSuchMethodException | InvocationTargetException ex) {
            main.getLogger().log(Level.SEVERE, "Could not instantiate " + feature.getName(), ex);
            return null;
        }
    }

    public static boolean enable(SFeature feature) {
        Logger logger = SamistineUtilities.getInstance().getLogger();
        if (feature == null) {
            throw new NullPointerException("Feature can not be null");
        } else {
            try {
                return feature.start();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Could not start " + feature.getName(), ex);
                return false;
            }
        }
    }

    public static boolean disable(SFeature feature) {
        Logger logger = SamistineUtilities.getInstance().getLogger();
        if (feature == null) {
            throw new NullPointerException("Feature can not be null");
        } else {
            try {
                feature.stop();
                return true;
            } catch (RuntimeException ex) {
                logger.log(Level.SEVERE, "An exception was thrown while attempting to disable " + feature.getName(), ex);
                return false;
            }
        }
    }
}
