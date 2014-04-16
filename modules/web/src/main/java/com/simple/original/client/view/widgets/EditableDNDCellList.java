package com.simple.original.client.view.widgets;

import static com.google.gwt.query.client.GQuery.$;
import gwtquery.plugins.draggable.client.DraggableOptions;
import gwtquery.plugins.draggable.client.DraggableOptions.AxisOption;
import gwtquery.plugins.draggable.client.DraggableOptions.HelperType;
import gwtquery.plugins.draggable.client.DraggableOptions.RevertOption;
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
import gwtquery.plugins.droppable.client.gwt.DragAndDropCellList;
import gwtquery.plugins.droppable.client.gwt.DroppableWidget;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.HasDataEditor;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.css.BorderStyleProperty.BorderStyle;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.Length;
import com.google.gwt.query.client.css.RGBColor;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public class EditableDNDCellList<T> extends Composite implements IsEditor<HasDataEditor<T>> {
	
	private class DropHandler implements DropEventHandler, OverDroppableEventHandler, OutDroppableEventHandler, DragEventHandler, DragStartEventHandler, DragStopEventHandler {

		private boolean isHover;
		// position in the cell of the place holder
		private int placeHolderPosition = 0;
		// the place holder is inserted just before this element
		private Element insertPoint;

		private GQuery placeHolder = $("<div id='cellPlaceHolder'></div>");

		public DropHandler() {

			placeHolder.css(CSS.BACKGROUND_COLOR.with(RGBColor.rgb("#B5D5FF")));
			placeHolder.css(CSS.BORDER.with(Length.px(1), BorderStyle.DASHED, RGBColor.BLACK));
			placeHolder.css(CSS.WIDTH.with(Length.pct(100)));
		}

		@Override
		public void onDragStop(DragStopEvent event) {
			Element draggedCell = event.getDraggable();
			$(draggedCell).css(CSS.DISPLAY.with(Display.BLOCK));
		}

		@Override
		public void onDragStart(DragStartEvent event) {
			Element draggedCell = event.getDraggable();
			$(draggedCell).css(CSS.DISPLAY.with(Display.NONE));
		}

		@Override
		public void onDrag(DragEvent event) {
			if (isHover) {
				maybeMovePlaceHolder(event.getHelper());
			}
		}

		@Override
		public void onOutDroppable(OutDroppableEvent event) {
			reset();
		}

		@Override
		public void onOverDroppable(OverDroppableEvent event) {
			Element dragHelper = event.getDragHelper();
			Element draggingCell = event.getDraggable();
			T operation = event.getDroppableData();

			placeHolder.height($(dragHelper).height());
			// attach the placeholder just before the dragging cell
			insertPoint = draggingCell;
			placeHolderPosition = cellList.getVisibleItems().indexOf(operation);
			movePlaceHolder();

			// enable listening on drag event
			isHover = true;
		}

		@Override
		public void onDrop(DropEvent event) {
			T operation = event.getDraggableData();

			asEditor().getList().remove(operation);
			asEditor().getList().add(placeHolderPosition, operation);
			//asEditor().getList().set(placeHolderPosition, operation);
			reset();
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
		 * Check if we have to move the place holder
		 * 
		 * @param draggableHelper
		 */
		private void maybeMovePlaceHolder(Element draggableHelper) {
			Element newCellAfterPlaceHolder = resolveCurrentInsertPoint(draggableHelper);

			if (newCellAfterPlaceHolder != null && newCellAfterPlaceHolder.equals(insertPoint)) {
				// placeHolder must not move
				return;
			}

			insertPoint = newCellAfterPlaceHolder;
			movePlaceHolder();

		}

		/**
		 * insert the place holder before the insertPoint element if this last
		 * is not null. Otherwise set the place holder at the end of the
		 * CellList
		 */
		private void movePlaceHolder() {
			placeHolder.remove();
			if (insertPoint != null) {
				placeHolder.insertBefore(insertPoint);
			} else {
				GQuery allCells = $(".operationCell", cellList);
				
				int last = allCells.elements().length;
				// insert the place holder at the end
				placeHolder.insertAfter(allCells.get(last));
				//Window.alert("ADDED THE VALUE");
			}
		}

		/**
		 * Return the first cell having its absolute top property just greater
		 * than drag helper. The place holder should be inserted just before
		 * this cell.
		 * 
		 * @param draggableHelper
		 * @return
		 */
		private Element resolveCurrentInsertPoint(Element draggableHelper) {
			GQuery allCells = $(".operationCell", cellList);

			if (allCells.isEmpty()) {
				// no cells, the placeholder should just be added in the begin
				// of the cell
				// list
				return null;
			}

			// compare absoluteTop of the draggable with absoluteTop of all
			// cells
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

			return null;
		}
	}

	
	/**
	 * This is our editor used to edit our ScriptInputProxy classes.
	 */
	private HasDataEditor<T> editor = null;

	private final DragAndDropCellList<T> cellList;

	private AbsolutePanel container = new AbsolutePanel();

	private DroppableWidget<DragAndDropCellList<T>> dropPanel;
	private DropHandler dragHandler = new DropHandler();


	public EditableDNDCellList(Cell<T> cell) {
		initWidget(container);
		
		cellList = new DragAndDropCellList<T>(cell);
		dropPanel = new DroppableWidget<DragAndDropCellList<T>>(cellList);

		DraggableOptions options = new DraggableOptions();

		options.setHelper(HelperType.CLONE);
		options.setAppendTo("body");
		options.setOpacity(0.8f);
		options.setCursor(Cursor.MOVE);
		options.setAxis(AxisOption.Y_AXIS);
		options.setRevert(RevertOption.ON_INVALID_DROP);
		cellList.setDraggableOptions(options);

		dropPanel.addOverDroppableHandler(dragHandler);
		dropPanel.addOutDroppableHandler(dragHandler);
		dropPanel.addDropHandler(dragHandler);

		cellList.addDragHandler(dragHandler);
		cellList.addDragStartHandler(dragHandler);
		cellList.addDragStopHandler(dragHandler);

		editor = HasDataEditor.of(cellList);
		container.add(dropPanel);
	}
	
	public void setMaxListSize(int maxListSize) {
	    cellList.setPageSize(maxListSize);
	}

	@Override
	public HasDataEditor<T> asEditor() {
		return editor;
	}
	
	public CellList<T> getCellList() {
		return cellList;
	}
}
