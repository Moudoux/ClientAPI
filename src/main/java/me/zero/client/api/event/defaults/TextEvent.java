package me.zero.client.api.event.defaults;

/**
 * Called in FontRenderer when text is rendered
 * and string width is checked
 *
 * @author Brady
 * @since 3/30/2017 12:00 PM
 */
public final class TextEvent {

    /**
     * The text being rendered
     */
    private String text;

    public TextEvent(String text) {
        this.text = text;
    }

    /**
     * @return The text being rendered
     */
    public final String getText() {
        return this.text;
    }

    /**
     * Sets the text being rendered
     *
     * @param text New text
     * @return This event
     */
    public final TextEvent setText(String text) {
        this.text = text;
        return this;
    }
}
