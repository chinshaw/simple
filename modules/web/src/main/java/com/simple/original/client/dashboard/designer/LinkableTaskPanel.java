package com.simple.original.client.dashboard.designer;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.HasDataEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.simple.original.client.dashboard.IProvidesDashboards;
import com.simple.original.client.dashboard.events.AnalyticsTaskNamesUpdatedEvent;
import com.simple.original.client.proxy.DashboardProxy;
import com.simple.original.client.proxy.LinkableDashboardProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.resources.ResourcesFactory;
import com.simple.original.client.view.widgets.EditableDNDCellList;

public class LinkableTaskPanel extends Composite implements IsEditor<HasDataEditor<LinkableDashboardProxy>>, AnalyticsTaskNamesUpdatedEvent.Handler {

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	public interface Binder extends UiBinder<Widget, LinkableTaskPanel> {
	}

	private class LinkableCell extends AbstractCell<LinkableDashboardProxy> {
		
	    private Resources resources = ResourcesFactory.getResources();
	    
		public LinkableCell() {
			super("click", "keyup");
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, LinkableDashboardProxy value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.appendHtmlConstant("<div class=\"operationCell\">");
				sb.appendHtmlConstant("<button style=\"float:right\">remove</button>");
				//sb.appendHtmlConstant("<label>" + getTaskName(value.getDashboardId()) + "</label>");
				sb.appendHtmlConstant("<input type='text' value='" + value.getContext() + "'></input>");
				sb.appendHtmlConstant("</div>");
			}
		}

		@Override
		public void onBrowserEvent(Context context, Element parent, LinkableDashboardProxy value, NativeEvent event, ValueUpdater<LinkableDashboardProxy> valueUpdater) {

			// Let AbstractCell handle the enter key.
			super.onBrowserEvent(context, parent, value, event, valueUpdater);

			// Ignore events that occur outside of the button element.
			Element target = event.getEventTarget().cast();
			if (!parent.getFirstChildElement().isOrHasChild(target)) {
				return;
			}

			String eventType = event.getType();
			if ("click".equals(eventType)) {
				EventTarget eventTarget = event.getEventTarget();
				if (Element.as(eventTarget).getTagName().equalsIgnoreCase("button")) {
					asEditor().getList().remove(value);
				}
			}

			if ("keyup".equals(eventType)) {
				NodeList<Element> elements = parent.getElementsByTagName("input");
				InputElement inputContext = elements.getItem(0).cast();
				if (event.getKeyCode() == 32) {
					inputContext.setValue(inputContext.getValue() + " ");
				}
				
				value.setContext(inputContext.getValue());
			}
		}
	}

	@UiField(provided = true)
	EditableDNDCellList<LinkableDashboardProxy> linkableTasks = new EditableDNDCellList<LinkableDashboardProxy>(new LinkableCell());

	@Editor.Ignore
	@UiField(provided = true)
	ValueListBox<DashboardProxy> availableDashboards = new ValueListBox<DashboardProxy>(new AbstractRenderer<DashboardProxy>() {

		@Override
		public String render(DashboardProxy task) {
			return (task == null) ? "Select..." : task.getName();
		}
	});

	private RequestContext requestContext;

	public LinkableTaskPanel(EventBus eventBus, IProvidesDashboards dashboardsProvider) {
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}

	@UiHandler("addLinkableTask")
	void onAddLinkableTask(ClickEvent event) {
		List<LinkableDashboardProxy> linkedTasks = linkableTasks.asEditor().getList();
		if (linkedTasks == null) {
			linkedTasks = new ArrayList<LinkableDashboardProxy>();
		}
		
		LinkableDashboardProxy linkedTask = requestContext.create(LinkableDashboardProxy.class);

		//TODO change to dashboard
		// THIS MAY BE WRONG
		linkedTask.setDashboard(availableDashboards.getValue());		
		linkedTask.setContext("");
		linkedTasks.add(linkedTask);

		// linkableTasks.add(requestContextavailableTasks.getValue());
		linkableTasks.asEditor().setValue(linkedTasks);
	}

	@Override
	public HasDataEditor<LinkableDashboardProxy> asEditor() {
		return linkableTasks.asEditor();
	}

	public void setRequestContext(RequestContext requestContext) {
		this.requestContext = requestContext;
	}
	
	public void setAvailableDashboards(List<DashboardProxy> dashboards) {
	    availableDashboards.setAcceptableValues(dashboards);
	}

    @Override
    public void onAnalyticsTaskNamesUpdated(AnalyticsTaskNamesUpdatedEvent event) {
      //  setAvailableDashboards(WidgetControllerDesigner.getTaskNames());
    }
  
}