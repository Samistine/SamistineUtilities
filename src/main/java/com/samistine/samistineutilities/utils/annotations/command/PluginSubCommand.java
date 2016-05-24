package com.samistine.samistineutilities.utils.annotations.command;

import com.samistine.samistineutilities.api.SCommandExecutor;
import java.lang.reflect.Method;

class PluginSubCommand {

    protected SCommandExecutor handler;
    protected Method handlerMethod;
    protected String[] tabCompletion;

    public PluginSubCommand(SCommandExecutor handler, Method handlerMethod, String[] tabCompletion) {
        this.handler = handler;
        this.handlerMethod = handlerMethod;
        this.tabCompletion = tabCompletion;
    }

}
