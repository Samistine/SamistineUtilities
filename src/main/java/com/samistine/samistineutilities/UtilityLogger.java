package com.samistine.samistineutilities;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * The UtilityLogger class is a modified {@link Logger} that prepends all
 * logging calls with the name of the root plugin plus the utility doing the
 * logging. The API for PluginLogger is exactly the same as {@link Logger}.
 *
 * @see Logger
 */
public final class UtilityLogger extends Logger {

    private String featureName;

    /**
     * Creates a new FeatureLogger that extracts the name from a plugin.
     *
     * @param context A reference to the plugin
     */
    public UtilityLogger(Utility context) {
        super(context.getClass().getCanonicalName(), null);
        String pluginName = context.getPlugin().getName();
        String pluginPrefix = context.getPlugin().getDescription().getPrefix();

        featureName = new StringBuilder().append("[")
                .append(
                        pluginPrefix != null ? pluginPrefix : pluginName
                                + " " + context.getName()
                )
                .append("] ").toString();

        setParent(context.getPlugin().getLogger());
        setLevel(Level.ALL);
    }

    @Override
    public void log(LogRecord logRecord) {
        logRecord.setMessage(featureName + logRecord.getMessage());
        super.log(logRecord);
    }

}
