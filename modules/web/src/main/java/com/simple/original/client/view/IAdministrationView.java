package com.simple.original.client.view;

public interface IAdministrationView extends IView {

    public interface Presenter {

		void onSendNotification(String value);

        void clearExecutionHistory();
        
    }

    void setPresenter(Presenter presenter);
    
}
