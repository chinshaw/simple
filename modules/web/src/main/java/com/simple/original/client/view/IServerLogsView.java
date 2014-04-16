/**
 * 
 */
package com.simple.original.client.view;

import com.google.gwt.user.client.ui.HasText;

/**
 * @author chinshaw
 *
 */
public interface IServerLogsView extends IView {
    
    public interface Presenter {

        void updateServerLogs(int lineCount);

        void updateRLogs(int lineCount);

        void updateTaskengineLogs(int logCount);
    }

    void setPresenter(Presenter debuggingActivity);

    HasText getServerLog();
    
    HasText getRLog();

    HasText getTaskEngineLog();

    void setSelectedLog(int index);
    
}
