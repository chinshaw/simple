package com.simple.original.client.view.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.events.EntityCreateEvent;
import com.simple.original.client.events.NotificationEvent;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.resources.ResourcesFactory;
import com.simple.original.client.view.widgets.AnimationBuilder.AnimationCompletionHandler;
import com.simple.original.shared.NotificationCriticality;

public class NotificationPanel extends Composite implements NotificationEvent.Handler, PlaceChangeEvent.Handler, EntityCreateEvent.Handler {

	interface Binder extends UiBinder<Widget, NotificationPanel> {
	}

	private final int SHOW_TIME = 6000;

	private class StoredMessage {
		String message;
		NotificationCriticality criticality;

		public StoredMessage(String message, NotificationCriticality criticality) {
			this.message = message;
			this.criticality = criticality;
		}
	}

	private static final Resources resources = ResourcesFactory.getResources();

	/**
	 * This is our container widget.
	 */
	@UiField
	FlowPanel container = new FlowPanel();

	/**
	 * Label that contains the message set from show message.
	 */
	@UiField
	Label messageContent;

	/**
	 * bool to tell if panel is currently showing.
	 */
	private boolean isShowing = false;

	@UiField
	Image criticalityImage;

	/**
	 * Queue of messages to show.
	 */
	protected List<StoredMessage> messageQueue = new ArrayList<StoredMessage>();

	/**
	 * Animation builder that will handle fading in and fading out of the
	 * widget.
	 */
	private AnimationBuilder animator = AnimationBuilder.create(container.getElement());

	@Inject
	protected NotificationPanel(EventBus eventBus, Resources resources) {
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		eventBus.addHandler(NotificationEvent.TYPE, this);
		eventBus.addHandler(PlaceChangeEvent.TYPE, this);
		eventBus.addHandler(EntityCreateEvent.TYPE, this);
		RootPanel.get().add(this);

	}

	@UiHandler("closeButton")
	void onClose(ClickEvent event) {
		hide();
	}

	public void showMessage(String message) {
		showMessage(message, NotificationCriticality.INFO);
	}

	public void showMessage(String message, NotificationCriticality criticality) {

		container.getElement().getStyle().setZIndex(10000);
		container.getElement().getStyle().setDisplay(Display.INLINE);

		if (!isShowing) {
			isShowing = true;

			messageContent.setText(message);
			setCriticalityImage(criticality);

			animator.reset();
			animator.addFadeInAnimation().run(1000);

			if (criticality != NotificationCriticality.CRITICAL) {
				Timer t = new Timer() {

					@Override
					public void run() {
						hide();
					}
				};
				t.schedule(SHOW_TIME); // Schedule it for 6 seconds from now.
			}
		} else {
			// add message to queue because we are already showing message.
			messageQueue.add(new StoredMessage(message, criticality));
		}
	}

	private void showMessage(StoredMessage storedMessage) {
		showMessage(storedMessage.message, storedMessage.criticality);
	}

	private void setCriticalityImage(NotificationCriticality criticality) {
		// Image criticalityImage = new Image();

		switch (criticality) {
		case CRITICAL:
			criticalityImage.setResource(resources.iconStatusCritical());
		case INFO:
			break;
		case WARN:
			break;
		default:
			break;
		}
	}

	/**
	 * Simple function to hide the panel.
	 */
	private void hide() {
		animator.reset();
		animator.addFadeOutAnimation().addCompletionHandler(new AnimationCompletionHandler() {
			@Override
			public void onAnimationsComplete() {
				isShowing = false;
				container.getElement().getStyle().setDisplay(Display.NONE);
				// Show the next message if there is one in the queue.
				if (messageQueue.size() > 0) {
					showMessage(messageQueue.remove(0));
				}
			}
		}).run(1000);
	}

	/**
	 * This is our callback to show messages from the event bus. This will be
	 * the common usage of usage because most views only have a connection to
	 * the event bus.
	 */
	@Override
	public void onNotification(NotificationEvent event) {
		if (NotificationCriticality.CRITICAL == event.getCriticality()) {
			Window.alert(event.getMessage());
		}
		showMessage(event.getMessage());
	}

	/**
	 * On a place change event we will hide the panel and clear the messge
	 * queue.
	 */
	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		messageQueue.clear();
		container.getElement().getStyle().setDisplay(Display.NONE);
	}

	/**
	 * This is the callback method to show message when a entity created event
	 * is fired by event bus.
	 */
	@Override
	public void onEntityCreated(EntityCreateEvent event) {
	}
}