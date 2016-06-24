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

import com.samistine.samistineutilities.api.FeatureManagerHelper;
import com.samistine.samistineutilities.api.SFeature;
import com.samistine.samistineutilities.utils.SFeatureDisabled;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Samuel Seidel
 */
public final class SamistineUtilities extends JavaPlugin {

    Map<Features, SFeature> m;

    @Override
    public void onEnable() {
        m = new EnumMap<>(Features.class);
        saveDefaultConfig();

        for (Features feature : Features.values()) {
            SFeature sFeature;
            if (getConfig().get(feature.getName(), null) == null) {
                sFeature = new SFeatureDisabled(this);
            } else {
                sFeature = FeatureManagerHelper.init(this, feature);
            }
            m.put(feature, sFeature);
        }

        m.values().stream().filter(Objects::nonNull).forEach(FeatureManagerHelper::enable);

        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[SamistineUtilities]" + ChatColor.GRAY + " Status: " + getModuleStatus());
    }

    @Override
    public void onDisable() {
        m.values().stream().filter(Objects::nonNull).forEach(FeatureManagerHelper::disable);
    }

    public static SamistineUtilities getInstance() {
        return getPlugin(SamistineUtilities.class);
    }

    public String getModuleStatus() {
        StringBuilder sb = new StringBuilder();
        for (Features feature : Features.values()) {
            SFeature featureInstance = m.get(feature);
            if (featureInstance == null) {
                sb.append(ChatColor.RED);
            } else {
                sb.append((featureInstance.isRunning() ? ChatColor.GREEN : ChatColor.DARK_GRAY));
            }
            sb.append(feature.getName()).append(", ");
        }
        String status = sb.toString();
        return status.substring(0, status.lastIndexOf(", "));
    }

}
