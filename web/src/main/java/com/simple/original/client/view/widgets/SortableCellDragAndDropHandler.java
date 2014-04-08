package com.simple.original.client.view.widgets;

import static com.google.gwt.query.client.GQuery.$;
import gwtquery.plugins.draggable.client.events.DragEvent;
import gwtquery.plugins.draggable.client.events.DragEvent.DragEventHandler;
import gwtquery.plugins.draggable.client.events.DragStartEvent;
import gwtquery.plugins.draggable.client.events.DragStartEvent.DragStartEventHandler;
import gwtquery.plugins.draggable.client.events.DragStopEvent;
import gwtquery.plugins.draggable.client.events.DragStopEvent.DragStopEventHandler;
import gwtquery.plugins.droppable.client.events.DropEvent;
import gwtquery.plugins.droppable.client.events.DropEvent.DropEventHandler;
import gwtquery.plugins.droppable.client.events.OutDroppableEvent;
import gwtquery.plugins.droppable.client.events.OutDroppableEvent.OutDroppableEventHandler;
import gwtquery.plugins.droppable.client.events.OverDroppableEvent;
import gwtquery.plugins.droppable.client.events.OverDroppableEvent.OverDroppableEventHandler;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

/**
 * This class will handle all dnd events in order to place and move correctly
 * the place holder, keep the position where a book is dropped and update the
 * DataListProvider objects
 *
 * @author Julien Dramaix (julien.dramaix@gmail.com,
 *         http://twitter.com/#!/jDramaix)
 *
 */
public class SortableCellDragAndDropHandler<T> implements DropEventHandler,
    OverDroppableEventHandler, OutDroppableEventHandler, DragEventHandler,
    DragStartEventHandler, DragStopEventHandler {

  // ListDataProvider containing initially all books
  private ListDataProvider<T> allBooksData;
  // the place holder is inserted just before this element
  private Element insertPoint;
  // boolean indicating if a dragging cell is hovering the droppable target (i.e
  // the CellList element containing choices of the user)
  private boolean isHover;
  // place holder element
  private GQuery placeHolder = $("<div id='cellPlaceHolder'></div>");
  // position in the cell of the place holder
  private int placeHolderPosition;
  // Id of the CellList element containing choices of the user
  private String sortableListId;
  // ListDataProvider containing choices of the user
  private ListDataProvider<T> userBooksData;

  public SortableCellDragAndDropHandler(
      ListDataProvider<T> allData,
      ListDataProvider<T> userBooksData, String sortableListId) {
    this.userBooksData = userBooksData;
    this.sortableListId = sortableListId;
    this.isHover = false;
    insertPoint = null;
    placeHolderPosition = 0;
  }

  /**
   * When a cell is dragging inside the droppable target, check if the place
   * holder has to move
   */
  public void onDrag(DragEvent event) {
    if (isHover) {
      maybeMovePlaceHolder(event.getHelper());
    }
  }

  /**
   * We use a clone as dragging helper so when a cell starts to be drag, hide
   * the original cell.
   *
   * @param event
   */
  public void onDragStart(DragStartEvent event) {
    Element draggedCell = event.getDraggable();
    $(draggedCell).css(CSS.DISPLAY.with(Display.NONE));
  }

  /**
   * As we hidden the original cell when the drag operation started, make the
   * cell visible again.
   *
   * @param event
   */
  public void onDragStop(DragStopEvent event) {
      Element draggedCell = event.getDraggable();
      $(draggedCell).css(CSS.DISPLAY.with(Display.BLOCK));

  }

  /**
   * On drop, insert the book being dragged in the correct position in the
   * correct DataListProvider
   */
  public void onDrop(DropEvent event) {

    T droppedObject = event.getDraggableData();

    // Avoid doublon
    allBooksData.getList().remove(droppedObject);
    userBooksData.getList().remove(droppedObject);

    userBooksData.getList().add(placeHolderPosition, droppedObject);
    reset();

  }

  /**
   * When the dragging cell is out the CellList, reset the state of this object
   */
  public void onOutDroppable(OutDroppableEvent event) {
    reset();
  }

  /**
   * When the dragging cell is over the CellList, listen on the
   * {@link DragEvent} of this cell and set up the place holder.
   */
  public void onOverDroppable(OverDroppableEvent event) {
    Element dragHelper = event.getDragHelper();
    Element draggingCell = event.getDraggable();
    T draggingObject = event.getDraggableData();

    // set height of the placeholder
    placeHolder.height($(dragHelper).height());

    // dragging book is coming from the user choice list
    if (userBooksData.getList().contains(draggingObject)){
      // attach the placeholder just before the dragging cell
      insertPoint = draggingCell;
      placeHolderPosition = userBooksData.getList().indexOf(draggingObject);
      movePlaceHolder();

    } else {
      maybeMovePlaceHolder(dragHelper);
    }

    // enable listening on drag event
    isHover = true;
  }

  /**
   * Check if we have to move the place holder
   *
   * @param draggableHelper
   */
  private void maybeMovePlaceHolder(Element draggableHelper) {
    Element newCellAfterPlaceHolder = resolveCurrentInsertPoint(draggableHelper);

    if (newCellAfterPlaceHolder != null
        && newCellAfterPlaceHolder.equals(insertPoint)) {
      // placeHolder must not move
      return;
    }

    insertPoint = newCellAfterPlaceHolder;
    movePlaceHolder();

  }

  /**
   * insert the place holder before the insertPoint element if this last is not
   * null. Otherwise set the place holder at the end of the CellList
   */
  private void movePlaceHolder() {
    placeHolder.remove();
    if (insertPoint != null) {
      placeHolder.insertBefore(insertPoint);
    } else {
      // insert the place holder at the end
      placeHolder.appendTo($("#" + sortableListId));
    }
  }

  /**
   * reset the state of this object
   */
  private void reset() {
    isHover = false;
    // remove the place holder
    placeHolder.remove();
    insertPoint = null;
  }

  /**
   * Return the first cell having its absolute top property just greater than
   * drag helper. The place holder should be inserted just before this cell.
   *
   * @param draggableHelper
   * @return
   */
  private Element resolveCurrentInsertPoint(Element draggableHelper) {
    // retrieve the celllist via its id
    Widget cellList = $("#" + sortableListId).widget();

    // the code below should work but the :hidden selector seems buggy in
    // GwtQuery
    // see issue 79 http://code.google.com/p/gwtquery/issues/detail?id=79
    // GQuery allVisibleCells = $(".bookCell", cellList).not(":hidden");

    GQuery allCells = $(".bookCell", cellList);

    if (allCells.isEmpty()) {
      // no cells, the placeholder should just be added in the begin of the cell
      // list
      return null;
    }

    // compare absoluteTop of the draggable with absoluteTop of all cells
    int draggableAbsoluteTop = draggableHelper.getAbsoluteTop();

    int i = 0;

    for (Element cell : allCells.elements()) {
      // workaround for issue 79
      if ($(cell).parent().visible()) {
        int cellAbsoluteTop = cell.getAbsoluteTop();
        if (cellAbsoluteTop > draggableAbsoluteTop) {
          placeHolderPosition = i;
          return cell;
        }
        i++;
      }
    }
    placeHolderPosition = i;

    // the placeholder should just be added at the end of the cellList
    return null;
  }

}


