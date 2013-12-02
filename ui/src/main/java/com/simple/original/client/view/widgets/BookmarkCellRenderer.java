package com.simple.original.client.view.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiRenderer;
import com.simple.original.client.proxy.ApplicationBookmarkProxy;

public class BookmarkCellRenderer extends AbstractCell<ApplicationBookmarkProxy> {
	
	public static interface Handler {
		void onDeleteBookmark(ApplicationBookmarkProxy bookmark);
		
	}
	
	public interface Renderer extends UiRenderer {
		void render(SafeHtmlBuilder sb, String bookmarkName, String bookmarkLocation);

		void onBrowserEvent(BookmarkCellRenderer o, NativeEvent e, Element p, ApplicationBookmarkProxy n);
	}

	private final Renderer uiRenderer;
	
	private final Handler handler;

	public BookmarkCellRenderer(final Renderer uiRenderer, Handler handler) {
		super(BrowserEvents.CLICK);
		this.uiRenderer = uiRenderer;
		this.handler = handler;
	}
	
	  /**
	   * {@inheritDoc}
	   * 
	   * <p>
	   * If you override this method to add support for events, remember to pass the
	   * event types that the cell expects into the constructor.
	   * </p>
	   */
	  public void onBrowserEvent(Context context, Element parent, ApplicationBookmarkProxy value,
	      NativeEvent event, ValueUpdater<ApplicationBookmarkProxy> valueUpdater) {
		  uiRenderer.onBrowserEvent(this, event, parent, value);
	  }

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, ApplicationBookmarkProxy value, SafeHtmlBuilder safeHtmlBuilder) {
		String bookmarkName = value.getName();
		String bookmarkLocation = value.getWhere();
		uiRenderer.render(safeHtmlBuilder, bookmarkName, bookmarkLocation);
	}

	@UiHandler({ "remove" })
	void onRemovePersonClicked(ClickEvent event, Element parent, ApplicationBookmarkProxy value) {
		if (handler != null) {
			handler.onDeleteBookmark(value);
		}
	}
}
