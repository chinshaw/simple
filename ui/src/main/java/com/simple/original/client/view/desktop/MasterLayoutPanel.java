package com.simple.original.client.view.desktop;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IMasterLayoutPanel;
import com.simple.original.client.view.widgets.SlidingPanel;

public class MasterLayoutPanel extends Composite implements IMasterLayoutPanel {

	private static final DockLayoutPanel dockPanel = new DockLayoutPanel(Unit.PX);

	public class LayoutPanelImpl extends SimpleLayoutPanel {
		public void setWidget(IsWidget activityWidget) {
			Widget widget = Widget.asWidgetOrNull(activityWidget);
			if (widget == null) {
				// Logger.getLogger("FOO").info("Resizing widget to nothing " +
				// getWidget());
				// If we are setting a null widget then we need to resize the
				// panel to notthing.

				try {
					dockPanel.setWidgetSize(this, 0);
				} catch (Exception e) {
					Logger.getLogger("FOO").info("Removing widget " + getWidget());
				}
			}
			// SimpleLayoutPanel handles the null check so we are good with
			// this.
			super.setWidget(widget);
		}
	}

	private LayoutPanelImpl controlPanel = new LayoutPanelImpl();

	// Can't set the size on center so this is a simple layout panel.
	private SlidingPanel centerPanel = new SlidingPanel();

	@Inject
	public MasterLayoutPanel(Resources resources) {
		initWidget(dockPanel);
		dockPanel.addNorth(controlPanel, 0);
		dockPanel.add(centerPanel);
		// dockPanel.animate(1000);

		centerPanel.getElement().setId("contentPanel");

	}

	@Override
	public AcceptsOneWidget getTopDisplay() {
		return controlPanel;
	}

	@Override
	public AcceptsOneWidget getCenterDisplay() {
		return centerPanel;
	}

	public void setTopPanelSize(Double size) {
		dockPanel.setWidgetSize(controlPanel, size);
		// dockPanel.animate(1000);
	}
}
