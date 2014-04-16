package com.simple.original.client.activity;

import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.place.ServerLogsPlace;
import com.simple.original.client.view.IServerLogsView;

public class ServerLogsActivity extends AbstractActivity<ServerLogsPlace, IServerLogsView> implements IServerLogsView.Presenter {
    
    /**
     * This is our log receiver class that will handle the callback from server and
     * update the log accordingly. It takes a HasText object in the constructor that should
     * come from the view.
     * @author chinshaw
     */
    class LogReceiver extends Receiver<String> {

        private final HasText logText;
        public LogReceiver(HasText logText) {
            this.logText = logText;
        }
        
        @Override
        public void onSuccess(String response) {
            logText.setText(response);
        }
        
        @Override
        public void onFailure(ServerFailure failure)  {
            logText.setText("Unable to retrieve log information");
        }
    }

    @Inject
    public ServerLogsActivity(IServerLogsView view) {
    	super(view);
    }

    @Override
    protected void bindToView() {
        display.setPresenter(this);
        display.setSelectedLog(0);
    }
    
    @Override
    public void updateServerLogs(int lineCount) {
        service().loggingServiceRequest().getServerLog(lineCount).fire(new LogReceiver(display.getServerLog()));
    }
    
    @Override
    public void updateRLogs(int lineCount) {
    	service().loggingServiceRequest().getRLog(lineCount).fire(new LogReceiver(display.getRLog()));
    }
    
    @Override
    public void updateTaskengineLogs(int lineCount) {
    	service().loggingServiceRequest().getTaskEngineLog(lineCount).fire(new LogReceiver(display.getTaskEngineLog()));
    }
}
