package me.zero.client.api.module;

import me.zero.client.api.util.interfaces.Bindable;
import me.zero.client.api.util.interfaces.Helper;
import me.zero.client.api.util.interfaces.Toggleable;

/**
 * Base for Module
 *
 * @see me.zero.client.api.module.Module
 *
 * @author Brady
 * @since 1/21/2017 12:00 PM
 */
interface IModule extends Helper, Toggleable, Bindable {

    /**
     * @return The type of the Module
     */
    Class<?> getType();
}
