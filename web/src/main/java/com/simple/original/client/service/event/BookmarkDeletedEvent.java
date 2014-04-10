package com.simple.original.client.service.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.simple.original.client.proxy.ApplicationBookmarkProxy;

public class BookmarkDeletedEvent extends GwtEvent<BookmarkDeletedEvent.Handler> {

    /**
     * Implemented by handlers of BookmarkEvent.
     */
    public interface Handler extends EventHandler {
        /**
         * Called when a {@link BookmarkDeletedEvent} is fired.
         * 
         * @param event
         *            the {@link BookmarkDeletedEvent}
         */
        void onBookmarkDeleted(BookmarkDeletedEvent event);
    }

    /**
     * A singleton instance of Type&lt;Handler&gt;.
     */
    public static final Type<Handler> TYPE = new Type<Handler>();

    private final ApplicationBookmarkProxy bookmark;
    
    public BookmarkDeletedEvent(ApplicationBookmarkProxy bookmark) {
    	this.bookmark = bookmark;
    }
   

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Return the new bookmark
     * 
     * @return a message
     */
    public ApplicationBookmarkProxy getBookmark() {
        return bookmark;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onBookmarkDeleted(this);
    }
}
