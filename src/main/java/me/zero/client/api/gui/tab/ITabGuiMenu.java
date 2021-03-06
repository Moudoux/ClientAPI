package me.zero.client.api.gui.tab;

import java.util.List;

/**
 * Base for Tab Gui Menus.
 * A Menu is an Element that has sub-elements.
 *
 * @author Brady
 * @since 2/6/2017 12:00 PM
 */
public interface ITabGuiMenu extends ITabGuiElement {

    /**
     * @return The list of sub-elements that belong to this menu
     */
    List<ITabGuiElement> getElements();

    /**
     * @return The current selected element
     */
    ITabGuiElement getSelectedElement();

    /**
     * @return The current selected index
     */
    int getSelected();
}
