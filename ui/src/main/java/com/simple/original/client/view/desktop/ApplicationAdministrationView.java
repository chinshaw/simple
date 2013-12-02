/**
 * 
 */
package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IAdministrationView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author chinshaw
 *
 */
public class ApplicationAdministrationView extends AbstractView implements IAdministrationView {

    @UiTemplate("ApplicationAdministration.ui.xml")
    public interface Binder extends UiBinder<Widget, ApplicationAdministrationView> {
    }
    
    
    @UiField TextArea
    notificationMessage;

    
    private Presenter presenter;
    
    
    /**
     * @param eventBus
     * @param resources
     */
    @Inject
    public ApplicationAdministrationView(EventBus eventBus, Resources resources) {
        super(eventBus, resources);
        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    }

    /* (non-Javadoc)
     * @see com.simple.original.client.view.desktop.ViewImpl#getErrorPanel()
     */
    @Override
    protected ErrorPanel getErrorPanel() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.simple.original.client.view.desktop.ViewImpl#reset()
     */
    @Override
    public void reset() {
        // TODO Auto-generated method stub
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("clearExecutionHistory")
    void onClearExecutionHistory(ClickEvent event) {
        presenter.clearExecutionHistory();
    }
    
    @UiHandler("sendNotification")
    void onSendNotification(ClickEvent event) {
    	presenter.onSendNotification(notificationMessage.getValue());
    }
}
