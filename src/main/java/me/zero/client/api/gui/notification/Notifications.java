package me.zero.client.api.gui.notification;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Essentially a Notification Manager, only takes in a renderer
 *
 * @author Brady
 * @since 1/6/2017 12:00 PM
 */
public class Notifications {

	/**
	 * The list of notifications for this notification handler
	 */
	private final Set<INotification> notifications = new LinkedHashSet<>();

	/**
	 * The renderer for this notification handler
	 */
	private final INotificationRenderer renderer;
	
	public Notifications(INotificationRenderer renderer) {
		this.renderer = renderer;
	}

	/**
	 * Creates a notification
	 *
	 * @param header The header text
	 * @param subtext The subtext
	 */
	public void post(String header, String subtext) {
		this.post(header, subtext, 2500);
	}

	/**
	 * Creates a notification
	 *
	 * @param header The header text
	 * @param subtext The subtext
	 * @param displayTime The display time
	 */
	public void post(String header, String subtext, long displayTime) {
		this.post(header, subtext, (long) (displayTime / 16F), displayTime);
	}

	/**
	 * Creates a notification
	 *
	 * @param header The header text
	 * @param subtext The subtext
	 * @param fade The fade time
	 * @param displayTime The display time
	 */
	public void post(String header, String subtext, long fade, long displayTime) {
		this.notifications.add(new Notification(header, subtext, fade, displayTime));
	}

	/**
	 * Renders the nofications
	 */
	public void render() {
		renderer.draw(notifications);
	}
}
