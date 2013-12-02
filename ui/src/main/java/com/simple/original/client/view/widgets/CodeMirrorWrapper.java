package com.simple.original.client.view.widgets;
/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */




import com.google.codemirror2_gwt.client.CodeMirrorCommand;
import com.google.codemirror2_gwt.client.CodeMirrorConfig;
import com.google.codemirror2_gwt.client.Coordinates;
import com.google.codemirror2_gwt.client.Cursor;
import com.google.codemirror2_gwt.client.HistorySize;
import com.google.codemirror2_gwt.client.KeyEventHandler;
import com.google.codemirror2_gwt.client.KeyMap;
import com.google.codemirror2_gwt.client.LineHandle;
import com.google.codemirror2_gwt.client.LineInfo;
import com.google.codemirror2_gwt.client.Position;
import com.google.codemirror2_gwt.client.Rectangle;
import com.google.codemirror2_gwt.client.SearchCursor;
import com.google.codemirror2_gwt.client.TextMark;
import com.google.codemirror2_gwt.client.Token;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.Command;

/**
 * A javascript overlay object over a CodeMirror object.
 *
 * (See http://codemirror.net/2/manual.html for the codemirror2 documentation).
 *
 * @author Nikhil Singhal
 */
public final class CodeMirrorWrapper extends JavaScriptObject {

  protected CodeMirrorWrapper() { }

  /**
   * Binds the specified command to the given key combination (e.g. "Ctrl-A)".
   */
  public native void addShortcut(String keys, Command command) /*-{
    var extraKeys = this.getOption('extraKeys') || {};
    extraKeys[keys] = $entry(function() {
      command.@com.google.gwt.user.client.Command::execute()();
    });
    this.setOption('extraKeys', extraKeys);
  }-*/;

  /**
   * Get the current editor content.
   */
  public native String getValue() /*-{
    return this.getValue();
  }-*/;

  /**
   * Set the editor content.
   */
  public native void setValue(String code) /*-{
    this.setValue(code);
  }-*/;

  /**
   * Clears the undo/redo history.
   */
  public native void clearHistory() /*-{
    this.clearHistory();
  }-*/;

  /**
   * Get the currently selected code.
   */
  public native String getSelection() /*-{
    return this.getSelection();
  }-*/;

  /**
   * Replace the selection with the given string.
   */
  public native void replaceSelection(String replacement) /*-{
    this.replaceSelection(replacement);
  }-*/;

  /**
   * Give the editor focus.
   */
  public native void focus() /*-{
    this.focus();
  }-*/;

  /**
   * Change the configuration of the editor. option should the name of an option, and value
   * should be a valid value for that option.
   */
  public native void setOption(String option, String value) /*-{
    this.setOption(option, value);
  }-*/;

  /**
   * Change the configuration of the editor. option should the name of an option, and value
   * should be a valid integer value for that option.
   */
  public native void setOption(String option, int value) /*-{
    this.setOption(option, value);
  }-*/;

  /**
   * Retrieves the current value of the given option for this editor instance,
   * as a string.
   */
  public native String getOption(String option) /*-{
    return String(this.getOption(option));
  }-*/;

  /**
   * Returns a {@link Coordinates} object containing the coordinates of the cursor relative to the
   * top-left corner of the page. start is a boolean indicating whether you want the start or the
   * end of the selection.
   */
  public native Coordinates getCursorCoords(boolean start) /*-{
    return this.cursorCoords(start);
  }-*/;

  /**
   * Like {@link #getCursorCoords(boolean)}, but returns the position of an arbitrary characters.
   */
  public native Coordinates getCharCoords(Cursor position) /*-{
    return this.charCoords(position);
  }-*/;

  /**
   * Given an {@link Position} object (in page coordinates), returns the {@link Cursor} position
   * that corresponds to it.
   */
  public native Cursor getCoordsChar(Position position) /*-{
    return this.coordsChar(position);
  }-*/;

  /**
   * Undo one edit (if any undo events are stored).
   */
  public native void undo() /*-{
    this.undo();
  }-*/;

  /**
   * Redo one undone edit.
   */
  public native void redo() /*-{
    this.redo();
  }-*/;

  /**
   * Returns a {@link HistorySize} object indicating the amount of stored undo and redo operations.
   */
  public native HistorySize getHistorySize() /*-{
    return this.historySize();
  }-*/;

  /**
   * Reset the given line's indentation to the indentation prescribed by the mode.
   */
  public native void indentLine(int lineNumber) /*-{
    this.indentLine(lineNumber);
  }-*/;

  /**
   * Reset the given line's indentation to the indentation prescribed by the mode.
   */
  public native void indentLine(LineHandle lineHandle) /*-{
    this.indentLine(lineHandle);
  }-*/;

  /**
   * Used to implement search/replace functionality.
   *
   * @param query can be a regular expression or a string (only strings will match across lines if
   *   they contain newlines).
   * @param start provides the starting position of the search.
   * @param caseFold is only relevant when matching a string. It will cause the search to be
   *   case-insensitive.
   */
  public native SearchCursor getSearchCursor(String query, Cursor start, boolean caseFold) /*-{
    return this.getSearchCursor(query, start, caseFold);
  }-*/;

  /**
   * Used to implement search/replace functionality. This starts looking from the start of the
   * document.
   *
   * @param query can be a regular expression or a string (only strings will match across lines if
   *   they contain newlines).
   * @param caseFold is only relevant when matching a string. It will cause the search to be
   *   case-insensitive.
   */
  public native SearchCursor getSearchCursor(String query, boolean caseFold) /*-{
    return this.getSearchCursor(query, null, caseFold);
  }-*/;

  /**
   * Retrieves information about the token the current mode found at the given {@link Cursor}
   * object.
   */
  public native Token getTokenAt(Cursor position) /*-{
    return this.getTokenAt(position);
  }-*/;

  /**
   * Can be used to mark a range of text with a specific CSS class name.
   *
   * @param from The {@link Cursor} position to start from.
   * @param to The {@link Cursor} position to end at.
   *
   * @return A {@link TextMark} that can be called to remove the marking.
   */
  public final native TextMark markText(Cursor from, Cursor to, String className) /*-{
    return this.markText(from, to, className);
  }-*/;

  /**
   * Add a gutter marker for the given line. Gutter markers are shown in the line-number area
   * (instead of the number for this line).
   *
   * @param className To put a picture in the gutter, set className to something that sets a
   *   background image.
   *
   * @return A {@link LineHandle} object corresponding to the line associated with the marker. This
   *   is useful since markers move with lines and therefore line numbers might change over time.
   */
  public native LineHandle setMarker(int lineNumber, String text, String className) /*-{
    return this.setMarker(lineNumber, text, className);
  }-*/;

  /**
   * Clears a marker created with setMarker.
   */
  public native void clearMarker(int lineNumber) /*-{
    this.clearMarker(lineNumber);
  }-*/;

  /**
   * Clears a marker created with setMarker.
   *
   * @param lineHandle A handle returned by setMarker (since it may now refer to a different
   *   line if something was added or deleted).
   */
  public native void clearMarker(LineHandle lineHandle) /*-{
    this.clearMarker(lineHandle);
  }-*/;

  /**
   * Set a CSS class name for the given line. Pass null to clear the class for a line.
   */
  public native void setLineClass(int lineNumber, String className) /*-{
    this.setLineClass(lineNumber, className);
  }-*/;

  /**
   * Set a CSS class name for the given line. Pass null to clear the class for a line.
   */
  public native void setLineClass(LineHandle lineHandle, String className) /*-{
    this.setLineClass(lineHandle, className);
  }-*/;

  /**
   * Returns a {@link LineInfo} object containing information about the current line.
   */
  public native LineInfo getLineInfo(int lineNumber) /*-{
    return this.lineInfo(lineNumber);
  }-*/;

  /**
   * Returns a {@link LineInfo} object containing information about the current line.
   */
  public native LineInfo getLineInfo(LineHandle line) /*-{
    return this.lineInfo(line);
  }-*/;

  /**
   * Puts node, which should be an absolutely positioned DOM node, into the editor. To remove the
   * widget again, simply use DOM methods (move it somewhere else, or call removeChild on its
   * parent).
   *
   * @param position The node will be positioned right below this.
   * @param node The node to insert into the DOM.
   * @param scrollIntoView When this is true, the editor will ensure that the entire node is
   *   visible (if possible).
   */
  public native void addWidget(Cursor position, Element node, boolean scrollIntoView) /*-{
    this.addWidget(position, node, scrollIntoView);
  }-*/;

  /**
   * Force matching-bracket-highlighting to happen.
   */
  public native void matchBrackets() /*-{
    this.matchBrackets();
  }-*/;

  /**
   * Returns the number of lines in the editor.
   */
  public native int getLineCount() /*-{
    return this.lineCount();
  }-*/;

  /**
   * start is a boolean indicating whether the start or the end of the selection must be retrieved.
   * A {@link Cursor} object will be returned.
   */
  public native Cursor getCursor(boolean start) /*-{
    return this.getCursor(start);
  }-*/;

  /**
   * Return true if any text is selected.
   */
  public native boolean isSomethingSelected() /*-{
    return this.somethingSelected();
  }-*/;

  /**
   * Set the cursor position.
   */
  public native void setCursor(int line, int ch) /*-{
    this.setCursor(line, ch);
  }-*/;

  /**
   * Set the cursor position.
   */
  public native void setCursor(Cursor cursor) /*-{
    this.setCursor(cursor);
  }-*/;

  /**
   * Set the selection range. start and end should be {@Cursor} objects.
   */
  public native void setSelection(Cursor start, Cursor end) /*-{
    this.setSelection(start, end);
  }-*/;

  /**
   * Get the content of line lineNumber.
   */
  public native String getLine(int lineNumber) /*-{
    return this.getLine(lineNumber);
  }-*/;

  /**
   * Set the content of the given line.
   */
  public native void setLine(int lineNumber, String value) /*-{
    this.setLine(lineNumber, value);
  }-*/;

  /**
   * Remove the given line from the document.
   */
  public native void removeLine(int lineNumber) /*-{
    this.removeLine(lineNumber);
  }-*/;

  /**
   * Get the text between the given points in the editor, which should be {@link Cursor} objects.
   */
  public native String getRange(Cursor from, Cursor to) /*-{
    return this.getRange(from, to);
  }-*/;

  /**
   * Sets the value of the smart-indent option.
   */
  public native void setSmartIndent(boolean smartIndent) /*-{
    this.setOption('smartIndent', smartIndent);
  }-*/;

  /**
   * Replace the part of the document between from and to with the given string. from and to must be
   * {@link Cursor} objects.
   */
  public native void replaceRange(String replacement, Cursor from, Cursor to) /*-{
    this.replaceRange(replacement, from, to);
  }-*/;

  /**
   * This simply inserts the string at position from.
   */
  public native void replaceRange(String replacement, Cursor from) /*-{
    this.replaceRange(replacement, from);
  }-*/;

  /**
   * If your code does something to change the size of the editor element (window resizes are
   * already listened for), or unhides it, you should probably follow up by calling this method to
   * ensure CodeMirror is still looking as intended.
   */
  public native void refresh() /*-{
    this.refresh();
  }-*/;

  /**
   * Programatically set the size of the editor (overriding the applicable CSS rules).
   * width and height height can be either numbers (interpreted as pixels) or CSS units
   * ("100%", for example). You can pass null for either of them to indicate that that
   * dimension should not be changed.
   */
  public native void setSize(String width, String height) /*-{
    this.setSize(width, height);
  }-*/;

  /**
   * Returns the hiden textarea used to read input.
   */
  public native Element getInputField() /*-{
    return this.getInputField();
  }-*/;

  /**
   * Fetches the DOM element that represents the editor gutter.
   */
  public native Element getGutterElement() /*-{
    return this.getGutterElement();
  }-*/;

  /**
   * Returns the DOM node that is responsible for the vertical sizing and horizontal scrolling of
   * the editor. You can change the height style of this element to resize an editor. (You might
   * have to call the refresh method afterwards.)
   */
  public native Element getScrollerElement() /*-{
    return this.getScrollerElement();
  }-*/;

  /**
   * Returns the DOM node that represents the editor, and controls its width. Remove this from
   * your tree to delete an editor instance. Set it's width style when resizing.
   */
  public native Element getWrapperElement() /*-{
    return this.getWrapperElement();
  }-*/;

  /**
   * Returns the current scroll area as a rectangle.
   */
  public native Rectangle getScrollInfo() /*-{
    return this.getScrollInfo();
  }-*/;

  public native void setOnKeyEvent(final KeyEventHandler handler) /*-{
    this.setOption('onKeyEvent', function(editorInstance, e) {
      return handler.
        @com.google.codemirror2_gwt.client.KeyEventHandler::onKeyEvent(Lcom/google/gwt/dom/client/NativeEvent;)
        (e);
    });
  }-*/;

  /**
   * Creates a new CodeMirror instance attached to a DOM element.
   *
   * @param hostElement The {@code Element} object the new CodeMirror instance should be added to.
   *
   * @return An overlay type representing a codemirror2 object.
   */
  public static final native CodeMirrorWrapper createEditor(
      Element hostElement,
      CodeMirrorConfig config) /*-{
    return $wnd.CodeMirror(hostElement, config);
  }-*/;
  
  /**
   * Creates a new CodeMirror instance attached to a DOM element.
   *
   * @param hostElement The {@code Element} object the new CodeMirror instance should be added to.
   *
   * @return An overlay type representing a codemirror2 object.
   */
  public static final native CodeMirrorWrapper createEditorFromTextArea(
      TextAreaElement hostElement,
      CodeMirrorConfig config) /*-{
    return $wnd.CodeMirror.fromTextArea(hostElement, config);
  }-*/;

  /**
   * Retrieves the default key map used by CodeMirror instances.
   *
   * @return the default key map object, mapping keyboard shortcuts to CodeMirror
   *     commands.
   */
  public static final native KeyMap getDefaultKeyMap() /*-{
    return $wnd.CodeMirror.keyMap['default'];
  }-*/;

  /**
   * Defines or redefines key map with given name.
   *
   * @param keyMap key map to be published
   * @param name a name to bind key map to
   */
  public static final native void defineKeyMap(KeyMap keyMap, String name) /*-{
    return $wnd.CodeMirror.keyMap[name] = keyMap;
  }-*/;

  /**
   * Updates the specified keyboard shortcut binding in the default CodeMirror keymap.
   * If the command is null, the existing binding will be removed.
   *
   * @param keys the key value to bind (e.g. Shift-Ctrl-G)
   * @param command the {@link CodeMirrorCommand} to ding
   */
  public static final void setDefaultKeyMapBinding(String keys,
      CodeMirrorCommand command) {
    KeyMap defaultKeyMap = getDefaultKeyMap();
    if (defaultKeyMap != null) {
      defaultKeyMap.setBinding(keys, command);
    }
  }
}
