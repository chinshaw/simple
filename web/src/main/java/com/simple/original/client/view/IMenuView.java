package com.simple.original.client.view;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.MenuBar;

public interface IMenuView extends IView {

    public interface Presenter {
        public abstract void onPreferencesSelected();

        public abstract void onPlaygroundSelected();

        public abstract void onSetDashboard();

        public abstract boolean onEnableDebug();

        public abstract boolean onEnableRemoteLogging();

        public abstract void onShowLogs();

		public abstract ScheduledCommand onAddFavoriteCommand();

		public abstract ScheduledCommand onEditFavoritesCommand();
    }

    public abstract void setPresenter(Presenter presenter);

    public abstract void setApplicationTitle(String applicationTitle);

	public abstract MenuBar getRootMenuBar();
	
	public abstract MenuBar getOptionsMenu();

	public abstract void setApplicationDescription(String description);

	public abstract MenuBar getFavoritesMenu();
}
