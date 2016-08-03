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

import com.samistine.samistineutilities.api.FeatureInfo;
import com.samistine.samistineutilities.api.SFeature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

/**
 *
 * @author Samuel Seidel
 */
public class SamistineUtilities extends JavaPlugin {

    Map<Plugin, List<FeatureHelper>> features = new HashMap<>();
    Collection<FeatureHelper> utilities;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        utilities = loadFeatures(
                this,
                "com.samistine.samistineutilities.features",
                feature -> getConfig().get(feature.getName(), null) != null
        );
    }

    @Override
    public void onDisable() {
        utilities.stream()
                .filter(FeatureHelper::stateIsEnabled)
                .forEach(FeatureHelper::disable);
    }

    public static SamistineUtilities getInstance() {
        return getPlugin(SamistineUtilities.class);
    }

    public String[] getModuleStatus() {
        return features.keySet().stream().map((plugin) -> {
            return ChatColor.GOLD + "[" + plugin.getName() + "]" + ChatColor.GRAY + " Status: " + getModuleStatus(plugin);
        }).toArray(String[]::new);
    }

    public String getModuleStatus(Plugin plugin) {
        StringBuilder sb = new StringBuilder(300);
        for (FeatureHelper feature : features.get(plugin)) {
            sb.append(feature.getStatus().getStatusColor()).append(feature.getName()).append(", ");
        }
        String status = sb.toString();
        return status.substring(0, status.lastIndexOf(", "));
    }

    public Collection<FeatureHelper> loadFeatures(Plugin plugin, String classPath, Predicate<FeatureHelper> shouldEnable) {
        Reflections reflections = new Reflections(classPath);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(FeatureInfo.class);

        Collection<Class<SFeature>> featureClasses = new ArrayList<>();
        for (Class<?> clazz : annotated) {
            try {
                featureClasses.add(
                        (Class<SFeature>) clazz
                );
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
        }

        List<FeatureHelper> featureHelpers = new ArrayList<>();
        for (Class<SFeature> clazz : featureClasses) {
            FeatureInfo info = clazz.getAnnotation(FeatureInfo.class);
            FeatureHelper featureHelper = new FeatureHelper(info.name(), clazz);
            featureHelpers.add(featureHelper);
        }

        features.putIfAbsent(plugin, new ArrayList<>());

        List<FeatureHelper> list = features.get(plugin);

        list.addAll(featureHelpers);

        featureHelpers.stream()
                .filter(shouldEnable)
                .forEach(featureHelper -> featureHelper.init(this));

        featureHelpers.stream()
                .filter(FeatureHelper::stateIsInitialized)
                .forEach(featureHelper -> featureHelper.enable());

        getServer().getConsoleSender().sendMessage(getModuleStatus());

        return featureHelpers;
    }

}
