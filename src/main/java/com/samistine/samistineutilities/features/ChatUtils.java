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

import com.samistine.samistineutilities.api.SFeature;
import com.samistine.samistineutilities.api.SListener;
import com.samistine.samistineutilities.api.objects.FeatureInfo;
import com.samistine.samistineutilities.utils.Pair;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 *
 * @author Samuel Seidel
 */
@FeatureInfo(name = "ChatUtils", desc = "Various chat things")
public final class ChatUtils extends SFeature implements SListener {

    private final List<Pair<Pattern, String>> regexAndReplacements;

    public ChatUtils() {
        Map<String, Object> values = getConfig().getConfigurationSection("Regex").getValues(false);

        regexAndReplacements = values.entrySet().parallelStream().map(entry -> {
            return new Pair<>(
                    Pattern.compile(entry.getKey()),
                    (String) entry.getValue()
            );
        }).collect(Collectors.toList());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void regex(AsyncPlayerChatEvent event) {
        String msg = event.getMessage();

        for (Pair<Pattern, String> regexAndReplacement : regexAndReplacements) {
            Pattern pattern = regexAndReplacement.first;
            String replacement = regexAndReplacement.second;

            msg = pattern.matcher(msg).replaceAll(replacement);
        }

        event.setMessage(msg);
    }

}
