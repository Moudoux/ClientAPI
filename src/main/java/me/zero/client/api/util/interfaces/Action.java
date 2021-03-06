package me.zero.client.api.util.interfaces;

/**
 * Action, can be started and stopped
 *
 * @author Brady
 * @since 2/16/2017 12:00 PM
 */
public interface Action {

    /**
     * Starts the action
     */
    void start();

    /**
     * Stops the action
     */
    void stop();
}
