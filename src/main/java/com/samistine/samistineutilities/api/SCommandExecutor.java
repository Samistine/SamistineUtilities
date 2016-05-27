package com.samistine.samistineutilities.api;

/**
 * The base class that all command executors should extend. Commands are
 * registered using {@link CommandManager} and defined using the
 * {@link com.samistine.samistineutilities.utils.annotations.command.Command}
 * annotations.
 *
 * @author Jacek Kuzemczak
 *
 */
public interface SCommandExecutor {

    /**
     *
     * @param <T> this
     * @param feature the feature to register this listener to
     * @return this for easy chaining
     */
    public default <T extends SCommandExecutor> T registerCommand(SFeature feature) {
        feature.registerCommand(this);
        return (T) this;
    }

    /**
     *
     * @param <T> this
     * @param feature the feature that owns this listener
     * @return this for easy chaining
     */
    public default <T extends SCommandExecutor> T unRegisterCommand(SFeature feature) {
        feature.unregisterCommand(this);
        return (T) this;
    }
}
