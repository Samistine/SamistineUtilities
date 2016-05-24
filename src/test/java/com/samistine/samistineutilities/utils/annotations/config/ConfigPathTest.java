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
package com.samistine.samistineutilities.utils.annotations.config;

import java.lang.reflect.Field;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Samuel
 */
public class ConfigPathTest {

    /**
     * Test of path method, of class ConfigPath.
     */
    @Test
    public void testPath() {
        System.out.println("path");
        ConfigPathTestClass instance = new ConfigPathTestClass();

        try {
            Field field = instance.getClass().getDeclaredField("test");
            try {
                ConfigPath configPath = field.getAnnotation(ConfigPath.class);
                if (configPath != null) {

                    String expResult = "Test.location";
                    String result = configPath.path();
                    assertEquals(expResult, result);

                } else {
                    fail("annotation, obtained from field, was null");
                }
            } catch (Exception ex) {
                fail("failed obtaining annotation on field");
            }
        } catch (NoSuchFieldException | SecurityException ex) {
            fail("failed field reflection");
        }
    }

    /**
     * Test of colorChar method, of class ConfigPath.
     */
//    @Test
//    public void testColorChar() {
//        System.out.println("colorChar");
//        ConfigPath instance = new ConfigPathTestClass();
//        char expResult = ' ';
//        char result = instance.colorChar();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of translateColors method, of class ConfigPath.
     */
//    @Test
//    public void testTranslateColors() {
//        System.out.println("translateColors");
//        ConfigPathTestClass instance = new ConfigPathTestClass();
//        instance
//        boolean expResult = false;
//        boolean result = instance.translateColors();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    public class ConfigPathTestClass {

        @ConfigPath(path = "Test.location")
        private String test;

    }

}
