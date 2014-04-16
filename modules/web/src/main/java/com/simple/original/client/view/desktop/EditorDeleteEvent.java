package com.simple.original.client.view.desktop;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EditorDeleteEvent extends GwtEvent<EditorDeleteEvent.Handler> {

	public interface Handler extends EventHandler {

		void onEditorDeleted(EditorDeleteEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private Editor<?> editor;

	public EditorDeleteEvent(Editor<?> editor) {
		this.editor = editor;
	}

	public Editor<?> getEditor() {
		return editor;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onEditorDeleted(this);
	}
}
