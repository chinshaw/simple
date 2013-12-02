package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IMenuView;
import com.simple.original.client.view.widgets.ErrorPanel;

public class MenuView extends AbstractView implements IMenuView {
	
	// private static final Logger logger =
	// Logger.getLogger(MenuViewImpl.class.getName());

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("MenuView.ui.xml")
	public interface Binder extends UiBinder<Widget, MenuView> {
	}

	//private List<MenuItem> activityMenuItems = new ArrayList<MenuItem>();

	@UiField
	MenuBar rootMenu;

	@UiField
	MenuBar optionsMenu;
	
	@UiField
	MenuBar favoritesMenu;
	
	@UiField
	HeadingElement contentTitle;
	
	@UiField
	HeadingElement contentDescription;

	@UiField
	MenuItem preferences;

	@UiField
	MenuItem addFavorite;
	
	@UiField 
	MenuItem defaultPage;

	@UiField
	MenuItem showDebugWindow;

	@UiField
	MenuItem enableRemoteLogging;
	
	@UiField
	MenuItem editFavorites;

	@UiField
	MenuItem showLogs;

	public MenuView(Resources resources) {
		super(resources);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}

	@Override
	protected ErrorPanel getErrorPanel() {
		return null;
	}

	@Override
	public void setPresenter(final Presenter presenter) {
		preferences.setCommand(new Command() {
			@Override
			public void execute() {
				presenter.onPreferencesSelected();
			}
		});

		
		addFavorite.setScheduledCommand(presenter.onAddFavoriteCommand());
		
		editFavorites.setScheduledCommand(presenter.onEditFavoritesCommand());

		defaultPage.setScheduledCommand(new Command() {

			@Override
			public void execute() {
				presenter.onSetDashboard();
			}

		});

		showDebugWindow.setScheduledCommand(new Command() {

			@Override
			public void execute() {
				boolean enabled = presenter.onEnableDebug();
				if (enabled) {
					showDebugWindow.setText("Hide Debug Window");
				} else {
					showDebugWindow.setText("Show Debug Window");
				}
			}
		});

		enableRemoteLogging.setScheduledCommand(new Command() {
			public void execute() {
				boolean enabled = presenter.onEnableRemoteLogging();
				if (enabled) {
					enableRemoteLogging.setText("Disable Remote Logging");
				} else {
					enableRemoteLogging.setText("Enable Remote Logging");
				}
			}
		});

		showLogs.setScheduledCommand(new Command() {
			public void execute() {
				presenter.onShowLogs();
			}
		});
	}

	@Override
	public void reset() {
		contentTitle.setInnerText("");
		contentDescription.setInnerText("");
	}

	@Override
	public void setApplicationTitle(String applicationTitle) {
		contentTitle.setInnerText(applicationTitle);
	}
	
	@Override
	public void setApplicationDescription(String description) {
		contentDescription.setInnerText(description);
	}

    @Override
    public MenuBar getRootMenuBar() {
        return rootMenu;
    }
	
    @Override
    public MenuBar getOptionsMenu() {
        return optionsMenu;
    }
    
    public MenuBar getFavoritesMenu() {
    	return favoritesMenu;
    }
}