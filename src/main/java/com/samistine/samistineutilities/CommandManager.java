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
package com.samistine.samistineutilities;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.samistine.samistineutilities.api.Feature;
import com.samistine.samistineutilities.api.SCommandExecutor;
import java.util.Collection;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Samuel
 */
public class CommandManager {

    private final com.samistine.samistineutilities.utils.annotations.command.CommandManager cm;
    private final Multimap<Feature, SCommandExecutor> commands;

    public CommandManager(Plugin plugin) {
        this.cm = new com.samistine.samistineutilities.utils.annotations.command.CommandManager(plugin);
        this.commands = MultimapBuilder.hashKeys().arrayListValues().build();
    }

    public void unRegisterCommands(Feature feature, SCommandExecutor command) {
        if (commands.containsValue(command)) {
            cm.unRegisterCommandExecutor(command);
            commands.remove(feature, command);
        } else {
            //The listener is not registered
        }
    }

    public void registerCommands(Feature feature, SCommandExecutor command) {
        if (!commands.containsEntry(feature, command)) {
            if (!commands.containsValue(command)) {
                cm.registerCommandExecutor(command);
                commands.put(feature, command);
            } else {
                //The listener was already registed with an other feature
            }
        } else {
            //The listener has already been registered by this feature
        }
    }

    public Collection<SCommandExecutor> getCommandsForFeature(Feature feature) {
        return commands.get(feature);
    }

}
