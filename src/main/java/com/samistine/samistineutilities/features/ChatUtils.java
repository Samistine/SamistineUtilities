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
package com.samistine.samistineutilities.features;

import com.samistine.samistineutilities.SamistineUtilities;
import com.samistine.samistineutilities.SFeature;
import com.samistine.samistineutilities.utils.BukkitUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.event.HandlerList;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author Samuel Seidel
 */
//@FeatureInfo(name = "ChatUtils", desc = "Various chat things")
public final class ChatUtils extends SFeature {

    public ChatUtils(SamistineUtilities main) {
        super(main,
                "ChatUtils",
                "Various chat things"
        );
    }

    private final List<Listener> registeredListeners = new ArrayList<>(1);

    @Override
    protected void onEnable() {
        PluginManager pm = getServer().getPluginManager();

        ConfigurationSection configRegex = getConfig().getConfigurationSection("Regex");
        if (configRegex != null) {
            Map<String, String> values = BukkitUtils.configSectionStringValues(configRegex);

            List<RegexRepl> regexAndReplacements = values.entrySet().parallelStream().map(entry -> {
                return new RegexRepl(
                        Pattern.compile(entry.getKey()),
                        (String) entry.getValue()
                );
            }).collect(Collectors.toList());

            getLogger().log(Level.INFO, "Loaded {0} regex statements.", regexAndReplacements.size());
            Listener regexListener = new RegexListener(regexAndReplacements);
            registeredListeners.add(regexListener);
            pm.registerEvents(regexListener, featurePlugin);
        }

    }

    @Override
    protected void onDisable() {
        registeredListeners.forEach(HandlerList::unregisterAll);
    }

}

class RegexListener implements Listener {

    final List<RegexRepl> regexAndReplacements;

    RegexListener(List<RegexRepl> regexAndReplacements) {
        this.regexAndReplacements = regexAndReplacements;
    }

    @EventHandler(priority = EventPriority.LOW)
    void regex(AsyncPlayerChatEvent event) {
        String msg = event.getMessage();

        for (RegexRepl regex : regexAndReplacements) {
            msg = regex.pattern.matcher(msg).replaceAll(regex.replacement);
        }

        event.setMessage(msg);
    }
}

class RegexRepl {

    final Pattern pattern;
    final String replacement;

    RegexRepl(Pattern regexPattern, String regexReplacement) {
        this.pattern = regexPattern;
        this.replacement = regexReplacement;
    }

}
