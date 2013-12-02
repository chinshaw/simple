package com.simple.original.client.view.desktop;

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

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.HasDataEditor;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.css.BorderStyleProperty.BorderStyle;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.Length;
import com.google.gwt.query.client.css.RGBColor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.resources.ResourcesFactory;

public class AnalyticsChainEditor extends Composite implements IsEditor<HasDataEditor<AnalyticsOperationProxy>>, HasEditorErrors<List<AnalyticsOperationProxy>> {

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
			AnalyticsOperationProxy operation = event.getDroppableData();

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
			AnalyticsOperationProxy operation = event.getDraggableData();

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
	 * This is the operation cell that displays the name and description
	 * of the analtyics operation. This also has a button for removing the
	 * operation from the task chain.
	 * @author chinshaw
	 *
	 */
	public class OperationCell extends AbstractCell<AnalyticsOperationProxy> implements Editor<AnalyticsOperationProxy> {

		public OperationCell() {
			super("click");
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, AnalyticsOperationProxy value, SafeHtmlBuilder sb) {
			sb.appendHtmlConstant("<div class=\"operationCell\">");
			sb.appendHtmlConstant("<a href=\"#CreateEditOperationBuilderPlace:" + value.getId() + " style=\"float:right;\">edit</a>");
			sb.appendHtmlConstant("<button style=\"float:right\">remove</button>");
			sb.appendHtmlConstant("<label>Name</label>");
			sb.appendHtmlConstant("<input type=\"text\" readonly=\"readonly\" value=\"" + value.getName() + "\"></input>");
			sb.appendHtmlConstant("<label>Description</label>");
			sb.appendHtmlConstant("<textarea readonly=\"readonly\" >" + value.getDescription() + "</textarea>");
			sb.appendHtmlConstant("</div>");
			// ActionCell ac;
		}

		public void onBrowserEvent(Context context, Element parent, AnalyticsOperationProxy value, NativeEvent event, ValueUpdater<AnalyticsOperationProxy> valueUpdater) {

			super.onBrowserEvent(context, parent, value, event, valueUpdater);
			if ("click".equals(event.getType())) {
				EventTarget eventTarget = event.getEventTarget();
				if (!Element.is(eventTarget)) {
					return;
				}

				if (Element.as(eventTarget).getTagName().equalsIgnoreCase("button")) {
					(asEditor().getList()).remove(value);
				}
				
				if (Element.as(eventTarget).getTagName().equalsIgnoreCase("edit")) {
					boolean edit = (asEditor().getList()).remove(value);
				}
			}
		}
	}

	/**
	 * This is our editor used to edit our ScriptInputProxy classes.
	 */
	private HasDataEditor<AnalyticsOperationProxy> editor = null;

	private final Resources resources = ResourcesFactory.getResources();

	private final DragAndDropCellList<AnalyticsOperationProxy> cellList = new DragAndDropCellList<AnalyticsOperationProxy>(new OperationCell());

	private AbsolutePanel container = new AbsolutePanel();
	private Label errorLabel = new Label();

	private DroppableWidget<DragAndDropCellList<AnalyticsOperationProxy>> dropPanel = new DroppableWidget<DragAndDropCellList<AnalyticsOperationProxy>>(cellList);
	private DropHandler dragHandler = new DropHandler();

	@UiConstructor
	public AnalyticsChainEditor() {
		initWidget(container);

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

		errorLabel.getElement().getStyle().setDisplay(Display.NONE);
		errorLabel.setStyleName(resources.style().constraintErrorLabel());
		container.add(errorLabel);

		editor = HasDataEditor.of(cellList);

		container.add(dropPanel);
	}

	@Override
	public HasDataEditor<AnalyticsOperationProxy> asEditor() {
		return editor;
	}

	@Override
	public void showErrors(List<EditorError> errors) {
		StringBuilder sb = new StringBuilder();
		for (EditorError error : errors) {
			if (error.getEditor().equals(this.asEditor())) {
				sb.append("\n").append(error.getMessage());
			}
		}

		if (sb.length() == 0) {
			errorLabel.setText("");
			errorLabel.getElement().getStyle().setDisplay(Display.NONE);
			return;
		}

		errorLabel.setText(sb.substring(1));
		errorLabel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
	}
}