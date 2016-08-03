package com.samistine.samistineutilities;

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
import com.samistine.samistineutilities.api.FeatureInfo;
import com.samistine.samistineutilities.api.SFeature;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.plugin.Plugin;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.reflections.Reflections;

/**
 *
 * @author Samuel Seidel
 */
@RunWith(Parameterized.class)
public class TestFeatures {

    @Parameters
    public static Collection<Class<? extends SFeature>> featureClassesToTest() {
        System.out.print("Gathering features");

        Reflections reflections = new Reflections("com.samistine");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(FeatureInfo.class);

        Collection<Class<? extends SFeature>> features = new ArrayList<>();
        for (Class<?> clazz : annotated) {
            try {
                features.add(
                        (Class<SFeature>) clazz
                );
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
        }

        return features;
    }

    Class<? extends SFeature> feature;

    public TestFeatures(Class<? extends SFeature> feature) {
        this.feature = feature;
    }

    /**
     * Verifies that features using the convenience constructor have declared a
     * {@link FeatureInfo} annotation containing non-empty nor null data.
     */
    @Test
    public void testConstructor() {
        Constructor<?>[] constructors = feature.getDeclaredConstructors();

        assertTrue("Feature should have a single constructor", constructors.length == 1);

        Constructor<?> constructor = constructors[0];

        assertArrayEquals("Feature should take an instance of SamistineUtilities.class as its only parameter", constructor.getParameterTypes(), new Object[]{SamistineUtilities.class});

        System.out.println(Arrays.toString(constructors));
        assertTrue("Features does not have a ", true);
        assertEquals(true, true);
    }

}
