package com.simple.original.client.view.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CommonResources;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.resources.ResourcesFactory;

public class NewPopinPanel extends Composite implements HasWidgets {

    public static class Tab extends SimplePanel {
        private Element inner;
        private Widget contentWidget;

        public Tab() {
            super(Document.get().createDivElement());
            getElement().appendChild(inner = Document.get().createDivElement());

            setStyleName(resources.style().dropDownTabPanelTab());
            inner.setClassName(resources.style().dropDownTabPanelTabInner());

            getElement().addClassName(CommonResources.getInlineBlockStyle());
        }

        public Tab(Widget widget) {
            this();
            setWidget(widget);
        }

        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addDomHandler(handler, ClickEvent.getType());
        }

        @Override
        public boolean remove(Widget w) {
            return super.remove(w);
        }

        public void setSelected(boolean selected) {
            if (selected) {
                addStyleName(resources.style().dropDownTabPanelTabSelected());
            } else {
                removeStyleName(resources.style().dropDownTabPanelTabSelected());
            }
        }

        @UiChild(limit = 1, tagname = "header")
        public void setHeader(Widget header) {
            header.setStyleName(resources.style().dropDownTabPanelTabInnerLabel());
            setWidget(header);
        }

        @UiChild(limit = 1, tagname = "widget")
        public void setContentWidget(Widget w) {
            this.contentWidget = w;
        }

        public Widget getContentWidget() {
            return contentWidget;
        }

        @Override
        protected com.google.gwt.user.client.Element getContainerElement() {
            return inner.cast();
        }
    }

    private class ContentAnimation extends Animation {

        int newHeight = 0;
        int currentHeight = 0;

        @Override
        protected void onUpdate(double progress) {
            Logger.getLogger("FOO").info("scrollheight is " + currentHeight + " newHeight is " + newHeight);
            contentPanel.setHeight((currentHeight - newHeight) * progress + "px");
        }

        public void run(int duration, Tab newTab) {
            Widget content = newTab.getContentWidget();

            // Window.alert("Height is " +
            // content.getElement().getOffsetHeight());

            if (!content.isAttached()) {
                DivElement d = Document.get().createDivElement();
                d.appendChild(content.getElement());
                int scrollheight = d.getScrollHeight();
                Logger.getLogger("FOOOOO").info("hidden scrollheight is " + scrollheight);

            }
            newHeight = newTab.getContentWidget().getOffsetHeight();
            if (contentPanel.getWidget() != null) {
                currentHeight = contentPanel.getWidget().getElement().getScrollHeight();
            }
            // super.run(duration);
        }
    }

    private static final Resources resources = ResourcesFactory.getResources();
    private final ArrayList<Tab> tabs = new ArrayList<Tab>();
    private final FlowPanel tabBar = new FlowPanel();
    private int selectedIndex = -1;
    private static final SimplePanel contentPanel = new SimplePanel();

    public NewPopinPanel() {
    
        FlowPanel vp = new FlowPanel();

        vp.addHandler(new ResizeHandler() {

            @Override
            public void onResize(ResizeEvent event) {
                Window.alert("Got a resize event");
            }

        }, ResizeEvent.getType());

        vp.setStyleName(resources.style().dropDownTabPanel());
        initWidget(vp);
        vp.add(tabBar);
        vp.add(contentPanel);

        tabBar.setStyleName(resources.style().dropDownTabPanelTabBar());
        contentPanel.addStyleName(resources.style().dropDownTabPanelContainer());
    }

    @Override
    public void clear() {
        contentPanel.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean remove(Widget w) {
        // TODO Auto-generated method stub
        return false;
    }

    @UiChild(tagname = "tab")
    public void addTab(final Tab tab) {
        tabBar.add(tab);
        tabs.add(tab);

        tab.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                setSelectedTab(tab);
            }
        });
    }

    public void selectTab(int index) {
        setSelectedTab(tabs.get(index));
    }

    public void setSelectedTab(Tab tab) {
        if (tabs.indexOf(tab) == selectedIndex) {
            if (contentPanel.getWidget() != null) {
                contentPanel.clear();
            }
            tabs.get(selectedIndex).setSelected(false);
            selectedIndex = -1;
            return;
        }

        if (selectedIndex != -1) {
            tabs.get(selectedIndex).setSelected(false);
        }

        // animation.run(1200, tab);
        contentPanel.setWidget(tab.getContentWidget());

        selectedIndex = tabs.indexOf(tab);
        tab.setSelected(true);
    }

    public Tab getSelectedTab() {
        Tab selectedTab = null;
        if (selectedIndex > -1) {
            tabs.get(selectedIndex);
        }
        return selectedTab;
    }

    /**
     * This will remove the tab along with it's content.
     * 
     * @param tab
     * @return
     */
    public boolean remove(Tab tab) {
        return tabBar.remove(tab);
    }

    @Override
    public void add(Widget w) {
        addTab((Tab) w);
    }

    public void close() {
        contentPanel.clear();
    }
}