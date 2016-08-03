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
import org.bukkit.ChatColor;

/**
 *
 * @author Samuel Seidel
 */
public final class FeatureHelper {

    public static enum Status {
        OFF("This feature is off"),
        INITIALIZING("This feature is being initialized"),
        INITIALIZED("This feature has been initialized but is not enabled"),
        ENABLED("This feature is running"),
        ERROR_INITIALIZATION("An error was encountered while initializing this feature: Check the constructor"),
        ERROR_INITIALIZATION2("Feature was not enabled, if there is no stacktrace above then the feature itself decided not to enable"),
        ERROR_DISABLING("An error was encountered while attempting to disable this feature");

        String detailedMessage;

        private Status(String detailedMessage) {
            this.detailedMessage = detailedMessage;
        }

        public ChatColor getStatusColor() {
            switch (this) {
                case OFF:
                    return ChatColor.DARK_GRAY;
                case INITIALIZING:
                    return ChatColor.YELLOW;
                case INITIALIZED:
                    return ChatColor.GOLD;
                case ENABLED:
                    return ChatColor.GREEN;
                case ERROR_INITIALIZATION:
                    return ChatColor.RED;
                case ERROR_INITIALIZATION2:
                    return ChatColor.RED;
                case ERROR_DISABLING:
                    return ChatColor.RED;
                default:
                    return ChatColor.MAGIC;
            }
        }

    }

    private final String name;
    private final Class<? extends SFeature> clazz;
    private Status status;
    private SFeature instance;

    FeatureHelper(String name, Class<? extends SFeature> clazz) {
        this.status = Status.OFF;
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<? extends SFeature> getClazz() {
        return clazz;
    }

    public Status getStatus() {
        return status;
    }

    public SFeature getInstance() {
        return instance;
    }

    synchronized void init(SamistineUtilities main) {
        switch (status) {
            case ERROR_DISABLING:
                throw new IllegalStateException("Can not initialize this feature because there was a previously encountered issue disabling it, State was:" + status.name());
            case INITIALIZING:
                throw new IllegalStateException("Can not initialize an already initialized/inititializing feature, State was:" + status.name());
            case INITIALIZED:
                throw new IllegalStateException("Can not initialize an already initialized/inititializing feature, State was:" + status.name());
            case ENABLED:
                throw new IllegalStateException("Can not initialize an already initialized/inititializing feature, State was:" + status.name());
            default: {
                status = Status.INITIALIZING;
                instance = FeatureManagerHelper.init(main, this);
                if (instance != null) {
                    status = Status.INITIALIZED;
                } else {
                    status = Status.ERROR_INITIALIZATION;
                }
            }
        }
    }

    synchronized void enable() {
        if (status == Status.INITIALIZED) {
            status = FeatureManagerHelper.enable(instance) ? Status.ENABLED : Status.ERROR_INITIALIZATION2;
        } else {
            throw new IllegalStateException("Can not enable a non initialized feature, State was:" + status.name());
        }
    }

    synchronized void disable() {
        if (status == Status.ENABLED) {
            status = FeatureManagerHelper.disable(instance) ? Status.INITIALIZED : Status.ERROR_DISABLING;
        } else {
            throw new IllegalStateException("Can not disable a non enabled feature, State was:" + status.name());
        }
    }

    public boolean stateIsEnabled() {
        return status == Status.ENABLED;
    }

    public boolean stateIsInitialized() {
        return status == Status.INITIALIZED;
    }
}
