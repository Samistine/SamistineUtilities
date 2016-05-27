package com.samistine.samistineutilities.utils.annotations.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this method should be used to handle a sub-command.
 *
 * @author Jacek Kuzemczak
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {

    /**
     * @return	The name of the parent command.
     */
    String parent();

    /**
     * @return	The name of this sub-command
     */
    String name();

    /**
     * An empty string means that no permission is required for this command
     *
     * @return	permission required to execute this command.
     */
    String permission() default "";

}
