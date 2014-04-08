package com.simple.original.client.place;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceChangeRequestEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.web.bindery.event.shared.EventBus;

public class PlaceController extends com.google.gwt.place.shared.PlaceController {

    private static final Logger log = Logger.getLogger(PlaceController.class.getName());

    private final EventBus eventBus;

    private final Delegate delegate;

    private Place where = Place.NOWHERE;

    /**
     * Previous place from history.
     */
    private Place previousPlace = null;

    /**
     * Legacy method tied to the old location for {@link EventBus}.
     * 
     * @deprecated use {@link #PlaceController(EventBus)}
     */
    @Deprecated
    public PlaceController(com.google.gwt.event.shared.EventBus eventBus) {
        this((EventBus) eventBus);
    }

    /**
     * Legacy method tied to the old location for {@link EventBus}.
     * 
     * @deprecated use {@link #PlaceController(EventBus, Delegate)}
     */
    @Deprecated
    public PlaceController(com.google.gwt.event.shared.EventBus eventBus, Delegate delegate) {
        this((EventBus) eventBus, delegate);
    }

    /**
     * Create a new PlaceController with a {@link DefaultDelegate}. The
     * DefaultDelegate is created via a call to GWT.create(), so an alternative
     * default implementation can be provided through &lt;replace-with&gt; rules
     * in a {@code .gwt.xml} file.
     * 
     * @param eventBus
     *            the {@link EventBus}
     */
    public PlaceController(EventBus eventBus) {
        this(eventBus, (Delegate) GWT.create(DefaultDelegate.class));
    }

    /**
     * Create a new PlaceController.
     * 
     * @param eventBus
     *            the {@link EventBus}
     * @param delegate
     *            the {@link Delegate} in charge of Window-related events
     */
    public PlaceController(EventBus eventBus, Delegate delegate) {
        super(eventBus, delegate);
        this.eventBus = eventBus;
        this.delegate = delegate;
        delegate.addWindowClosingHandler(new ClosingHandler() {
            public void onWindowClosing(ClosingEvent event) {
                String warning = maybeGoTo(Place.NOWHERE);
                if (warning != null) {
                    event.setMessage(warning);
                }
            }
        });
    }

    /**
     * Get the previous place, if null then this is the first place.
     * 
     * @return a {@link Place} instance
     */
    public Place getPreviousPlace() {
        return previousPlace;
    }

    /**
     * Returns the current place.
     * 
     * @return a {@link Place} instance
     */
    public Place getWhere() {
        return where;
    }

    /**
     * Request a change to a new place. It is not a given that we'll actually
     * get there. First a {@link PlaceChangeRequestEvent} will be posted to the
     * event bus. If any receivers post a warning message to that event, it will
     * be presented to the user via {@link Delegate#confirm(String)} (which is
     * typically a call to {@link Window#confirm(String)}). If she cancels, the
     * current location will not change. Otherwise, the location changes and a
     * {@link PlaceChangeEvent} is posted announcing the new place.
     * 
     * @param newPlace
     *            a {@link Place} instance
     *            
     */
    public void goTo(Place newPlace) {
        log().fine("goTo: " + newPlace);

        // if (getWhere().equals(newPlace)) {
        // log().fine("Asked to return to the same place: " + newPlace);
        // return;
        // }

        String warning = maybeGoTo(newPlace);
        if (warning == null || delegate.confirm(warning)) {
            previousPlace = where;
            where = newPlace;
            eventBus.fireEvent(new PlaceChangeEvent(newPlace));
        }
    }

    /**
     * This is useful for going back unless there is,
     * not a previous place, commonly when a user refreshes
     * the screen and then tries to click back.
     * @param place Place to go if there is not a previous place set.
     */
    public void goBackOr(Place place) {
        Place previousPlace = getPreviousPlace();

        if (previousPlace != null && previousPlace != Place.NOWHERE) {
            goTo(previousPlace);
        } else if (place != null) {
            goTo(place);
        } else {
            throw new RuntimeException("Previous place was null and the optional place was not specified");
        }

    }

    /**
     * Visible for testing.
     */
    private Logger log() {
        return log;
    }

    private String maybeGoTo(Place newPlace) {
        PlaceChangeRequestEvent willChange = new PlaceChangeRequestEvent(newPlace);
        eventBus.fireEvent(willChange);
        String warning = willChange.getWarning();
        return warning;
    }

}
