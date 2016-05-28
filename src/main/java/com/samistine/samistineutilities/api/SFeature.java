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

import com.samistine.samistineutilities.api.objects.FeatureInfo;
import com.samistine.samistineutilities.api.objects.FeatureLogger;
import com.samistine.samistineutilities.SamistineUtilities;
import com.samistine.samistineutilities.utils.annotations.command.backend.CommandManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Samuel Seidel
 */
public abstract class SFeature implements Feature {
    
    private final String name;
    private final String desc;
    private final Logger logger;
    
    {
        FeatureInfo annotation = getClass().getAnnotation(FeatureInfo.class);
        Validate.notNull(annotation, "Implementing class must contain FeatureInfo anotation");
        this.name = annotation.name();
        this.desc = annotation.desc();
        this.logger = new FeatureLogger(this);
    }
    
    @Override
    public final String getName() {
        return name;
    }
    
    @Override
    public final String getDesc() {
        return desc;
    }
    
    @Override
    public final Logger getLogger() {
        return logger;
    }
    
    @Override
    public final Server getServer() {
        return getRootPlugin().getServer();
    }
    
    @Override
    public final SamistineUtilities getRootPlugin() {
        return SamistineUtilities.getInstance();
    }
    
    @Override
    public final FileConfiguration getRootConfig() {
        return getRootPlugin().getConfig();
    }

    /**
     * The configuration section for this feature
     *
     * @return config
     */
    public final ConfigurationSection getConfig() {
        ConfigurationSection section = getRootConfig().getConfigurationSection(name);
        Validate.notNull(section);
        return section;
    }
    
    public final void enable() {
        logger.log(Level.FINE, "{0}.enable()", getClass().getName());
        if (!getConfig().getBoolean("enabled", false)) {
            disable(false);
            return;
        }
        if (this instanceof SListener) {//Auto register main class if it implements listener
            ((SListener) this).registerListener(this);
        }
        if (this instanceof SCommandExecutor) {
            ((SCommandExecutor) this).registerCommand(this);
        }
        logger.log(Level.INFO, "Enabled");
    }

    /**
     * This method can only be called a single time.
     *
     * @param log true if we should log that we are disabling
     */
    public final void disable(boolean log) {
        logger.log(Level.FINE, "{0}.disable()", getClass().getName());
        onDisable();
        listeners.forEach(HandlerList::unregisterAll);
        listeners.clear();
        commands.forEach(getCommandManager()::unRegisterCommandExecutor);
        commands.clear();
        if (log) {
            logger.log(Level.INFO, "Disabled");
        }
    }

    /**
     * Called when the feature is being disabled, but before anything else takes
     * place.
     * <br>
     */
    protected void onDisable() {
    }

    //
    //
    //
    //Begin Event/Command Methods & Fields
    //
    //
    //
    private final List<SListener> listeners = new ArrayList<>();
    private final List<SCommandExecutor> commands = new ArrayList<>();
    
    protected final void registerListener(SListener listener) {
        if (!listeners.contains(listener)) {
            getServer().getPluginManager().registerEvents(listener, getRootPlugin());
            listeners.add(listener);
        } else {
            //The listener was already registed
        }
    }
    
    protected final void unregisterListener(SListener listener) {
        if (listeners.contains(listener)) {
            HandlerList.unregisterAll(listener);
            listeners.remove(listener);
        } else {
            //The listener was not registered
        }
    }
    
    protected final void registerCommand(SCommandExecutor command) {
        if (!commands.contains(command)) {
            getCommandManager().registerCommandExecutor(command);
            commands.add(command);
        } else {
            //The listener has already been registered by this feature
        }
    }
    
    protected final void unregisterCommand(SCommandExecutor command) {
        if (commands.contains(command)) {
            getCommandManager().unRegisterCommandExecutor(command);
            commands.remove(command);
        } else {
            //The listener is not registered
        }
    }
    
    private CommandManager cm;
    
    private CommandManager getCommandManager() {
        if (cm == null) {
            cm = new CommandManager(getRootPlugin());
        }
        return cm;
    }
    
}
