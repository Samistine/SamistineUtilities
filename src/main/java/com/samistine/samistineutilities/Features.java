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

import com.samistine.samistineutilities.api.SFeature;
import com.samistine.samistineutilities.features.ChatUtils;
import com.samistine.samistineutilities.features.FindTiles;
import com.samistine.samistineutilities.features.NoRainFall;
import com.samistine.samistineutilities.features.NoSandFall;
import com.samistine.samistineutilities.features.physicsdisabler.JCPhysicsDisabler;

/**
 *
 * @author Samuel Seidel
 */
public enum Features {
    NoSandFall("NoSandFall", NoSandFall.class),
    NoRainFall("NoRainFall", NoRainFall.class),
    FindTiles("FindTiles", FindTiles.class),
    DisablePhysics("DisablePhysics", JCPhysicsDisabler.class),
    ChatUtils("ChatUtils", ChatUtils.class);

    private final String name;
    private final SFeatureWrapper featureWrapper;

    private Features(String name, Class<? extends SFeature> clazz) {
        this.name = name;
        this.featureWrapper = new SFeatureWrapper<>(clazz);
    }

    public String getName() {
        return name;
    }

    public SFeatureWrapper getFeatureWrapper() {
        return featureWrapper;
    }

}
