package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IServerLogsView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * This is a simple tab'd panel view of the different server logs.
 * 
 * @author chinshaw
 */
public class ServerLogsView extends AbstractView implements IServerLogsView {

    @UiField
    TabLayoutPanel tabLayoutPanel;

    @UiField
    TextArea serverLog;

    @UiField
    TextArea rLog;

    @UiField
    TextArea taskengineLog;

    private Presenter presenter = null;

    /**
     * This is the uibinder and it will use the view.DefaultView.ui.xml
     * template.
     */
    @UiTemplate("DebuggingView.ui.xml")
    public interface Binder extends UiBinder<Widget, ServerLogsView> {
    }

    @Inject
    public ServerLogsView(EventBus eventBus, Resources resources) {
        super(eventBus, resources);
        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

        tabLayoutPanel.addSelectionHandler(new SelectionHandler<Integer>() {

            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                switch (event.getSelectedItem()) {
                case 0:
                    presenter.updateServerLogs(1000);
                    break;
                case 1:
                    break;
                case 2:
                    presenter.updateRLogs(1000);
                    break;
                case 3:
                    presenter.updateTaskengineLogs(1000);
                    break;
                }
            }
        });

        tabLayoutPanel.setAnimationVertical(true);

    }

    @Override
    protected ErrorPanel getErrorPanel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public HasText getServerLog() {
        return this.serverLog;
    }

    @Override
    public HasText getRLog() {
        return this.rLog;
    }

    @Override
    public HasText getTaskEngineLog() {
        return this.taskengineLog;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setSelectedLog(int index) {
        tabLayoutPanel.selectTab(index);
        SelectionEvent.fire(tabLayoutPanel, index);
    }
}
