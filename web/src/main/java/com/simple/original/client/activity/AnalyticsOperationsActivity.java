package com.simple.original.client.activity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.events.NotificationEvent;
import com.simple.original.client.place.AnalyticsOperationPlace;
import com.simple.original.client.place.AnalyticsOperationsPlace;
import com.simple.original.client.place.CopyOperationBuilderPlace;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.service.DaoBaseDataProvider;
import com.simple.original.client.service.DaoRequestFactory.DaoRequest;
import com.simple.original.client.view.IOperationsView;
import com.simple.original.client.view.IOperationsView.Presenter;
import com.simple.original.client.view.widgets.AnalyticsPopupPanel;

public class AnalyticsOperationsActivity extends AbstractActivity<AnalyticsOperationsPlace, IOperationsView> implements Presenter {

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(AnalyticsOperationsActivity.class.getName());

	private class AnalyticsDataProvider extends DaoBaseDataProvider<AnalyticsOperationProxy> {

		@Override
		public String[] getWithProperties() {
			return new String[0];
		}

		@Override
		public DaoRequest<AnalyticsOperationProxy> getRequestProvider() {
			return dao().createAnalyticsOperationRequest();
		}
	}

	/**
	 * This is the analytics operation data provider used to fetch the analytics
	 * operation and updated when needed.
	 */
	private AnalyticsDataProvider dataProvider = new AnalyticsDataProvider();

	/**
	 * Selection model to handle the table selections.
	 */
	private MultiSelectionModel<AnalyticsOperationProxy> selectionModel = new MultiSelectionModel<AnalyticsOperationProxy>();

	@Inject
	public AnalyticsOperationsActivity(IOperationsView view) {
		super(view);
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this); // Bind the view to the presenter

		dataProvider.addDataDisplay(display.getOperationsList());
		display.getOperationsList().setSelectionModel(selectionModel);
		
	}
	
	/**
	 * Overriding parent cleanup so that we can add the call to remove the displays from the operartions
	 * dataprovider. Otherwise if we try to re add it will throw IllegalStateException.
	 */
	@Override
	protected void cleanup() {
		dataProvider.removeDataDisplay(display.getOperationsList());
		selectionModel.clear();
		super.cleanup();
	}

	@Override
	public void onAddAnalytics() {
		placeController().goTo(new AnalyticsOperationPlace(null));
	}

	@Override
	public void onDeleteAnalyticsOperations() {
		final Set<AnalyticsOperationProxy> selectedAnalyticsOperation = selectionModel.getSelectedSet();
		final Set<Long> idsToDelete = new HashSet<Long>();
		for (AnalyticsOperationProxy analyticsOperation : selectedAnalyticsOperation) {
			idsToDelete.add(analyticsOperation.getId());
		}
		dao().createAnalyticsTaskRequest().getAnalyticsTasksForOperationIds(idsToDelete).fire(new Receiver<List<AnalyticsTaskProxy>>() {

			@Override
			public void onSuccess(List<AnalyticsTaskProxy> tasks) {
				if (!tasks.isEmpty()) {
					// Show popup panel to display all the linked Tasks
					// associated with the selected operations.
					AnalyticsPopupPanel analyticsPopupPanel = new AnalyticsPopupPanel(false);
					analyticsPopupPanel.setMessage("Selected " + idsToDelete.size() + " Operation(s) has the following linked Task(s) :");
					int index = 1;
					for (AnalyticsTaskProxy task : tasks) {
						analyticsPopupPanel.setMessage(index + ". " + task.getName());
						index++;
					}
					analyticsPopupPanel.setMessage("Can not delete the operation(s), please remove the linked task(s)");
					analyticsPopupPanel.showPopup();
				} else {
					boolean confirm = Window.confirm("Are you sure you want to delete the " + selectedAnalyticsOperation.size() + " analytics operations");
					if (confirm) {
						dao().createAnalyticsOperationRequest().deleteList(idsToDelete).fire(new Receiver<Integer>() {
							@Override
							public void onSuccess(Integer numberOfEntitiesDeleted) {
								if (numberOfEntitiesDeleted != idsToDelete.size()) {
									display.showError("Unable to delete all the selected operations");
								} else {
									NotificationEvent ne = new NotificationEvent("Deleted Successfully");
									eventBus().fireEvent(ne);
								}
								// display.getAnalyticsTable().setVisibleRangeAndClearData(display.getAnalyticsTable().getVisibleRange(),
								// true);
								selectionModel.clear();
							}

							public void onFailure(ServerFailure error) {
								logger.info("AlertOperationActivity->onDeleteAnalyticsOperations->deleteList():" + error.toString());
								display.showError("Unable to delete Operation(s) ", error);
							}

						});
					}
				}
			}

			public void onFailure(ServerFailure error) {
				logger.info("AlertOperationActivity->onDeleteAnalyticsOperations->getTasksByOperation():" + error.toString());
				display.showError("Unable to get linked tasks: ", error);
			}
		});
	}

	@Override
	public void onEditAnalytics() {
		final Set<AnalyticsOperationProxy> selectedAnalyticsOperation = selectionModel.getSelectedSet();
		for (AnalyticsOperationProxy analyticsOperationProxy : selectedAnalyticsOperation) {
			placeController().goTo(new AnalyticsOperationPlace(analyticsOperationProxy.getId()));
		}
	}

	@Override
	public void onCopyAnalytics() {
		final Set<AnalyticsOperationProxy> selectedAnalyticsOperation = selectionModel.getSelectedSet();
		for (AnalyticsOperationProxy analyticsOperationProxy : selectedAnalyticsOperation) {
			placeController().goTo(new CopyOperationBuilderPlace(analyticsOperationProxy.getId()));
		}
	}

	/**
	 * this method is called on change of the Operation list item this will
	 * reload the data of cell table according to the selection of the ListBox
	 * item
	 */
	@Override
	public void onOperationSelected(AnalyticsOperationProxy operation) {
		placeController().goTo(new AnalyticsOperationPlace(operation.getId()));
	}

	/**
	 * This method is called on click of search button to load the data on
	 * celltable with the search results
	 */
	@Override
	public void onClickSearch() {
		selectionModel.clear();
	}

	@Override
	public void onSelectAll(Boolean selectAll) {
		/*
		 * Iterable<AnalyticsOperationProxy> selectable =
		 * display.getAnalyticsTable().getVisibleItems(); for
		 * (Iterator<AnalyticsOperationProxy> iter = selectable.iterator();
		 * iter.hasNext();) { selectionModel.setSelected(iter.next(),
		 * selectAll); } display.setEnabledCopyButton(false);
		 * display.setEnabledEditButton(false);
		 * display.setEnabledDeleteButton(selectAll);
		 */
	}

	@Override
	public void onCancel() {
		display.getSearchText().setText("");
		placeController().goTo(new AnalyticsOperationsPlace());
	}
}