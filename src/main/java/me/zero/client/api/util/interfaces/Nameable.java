package me.zero.client.api.util.interfaces;

/**
 * Gives objects a name, alias, and description
 *
 * @author Brady
 * @since 2/10/2017 12:00 PM
 */
public interface Nameable {

    /**
     * @return Name of this object
     */
    String getName();

    /**
     * @return Description of this object
     */
    String getDescription();
}
