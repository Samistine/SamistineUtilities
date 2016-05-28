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
package com.samistine.samistineutilities.features;

import com.samistine.samistineutilities.api.SFeature;
import com.samistine.samistineutilities.utils.annotations.config.ConfigPath;
import com.samistine.samistineutilities.utils.annotations.config.ConfigPathProcessor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 *
 * @author Samuel
 */
public class ChatUtils extends SFeature implements Listener {

    public ChatUtils() {
        new ConfigPathProcessor(getLogger()).loadValues(getRootConfig().getConfigurationSection("ChatUtils"), this);
    }

    @ConfigPath(path = "stripNonEnglishCharacters")
    private boolean stripNonEnglishCharacters;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void stripNonEnglishCharacters(AsyncPlayerChatEvent event) {
        if (stripNonEnglishCharacters) {
            if (event.getMessage().matches("[^\\x00-\\x7F]+")) {
                event.setMessage(event.getMessage().replaceAll("[^\\x00-\\x7F]+", ""));
            }
        }
    }

}
