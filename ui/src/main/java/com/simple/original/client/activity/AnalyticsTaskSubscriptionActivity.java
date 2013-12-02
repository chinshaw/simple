package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;
import com.simple.original.client.place.ReportAdminSubscriptionPlace;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.view.IAnalyticsTaskSubscriptionView;

/**
 * @author hemantha
 * 
 *         Only admin can access this page and modify subscribers list for
 *         Reporting Task
 */
public class AnalyticsTaskSubscriptionActivity extends AbstractActivity<ReportAdminSubscriptionPlace, IAnalyticsTaskSubscriptionView> implements IAnalyticsTaskSubscriptionView.Presenter {

	/*
	 * Logger instance
	 */
	private static final Logger logger = Logger.getLogger(AnalyticsTaskSubscriptionActivity.class.getName());

	/**
	 * List of users
	 */
	private List<PersonProxy> ldapUsersList = new ArrayList<PersonProxy>();

	/**
	 * Selection Model for the Users Celllist
	 */
	protected SelectionModel<PersonProxy> usersListSelectionModel = new MultiSelectionModel<PersonProxy>();

	/**
	 * Selection Model for Subscribers Cell list
	 */
	protected SelectionModel<PersonProxy> subscribersListSelectionModel = new MultiSelectionModel<PersonProxy>();

	/**
	 * Constructor of ReportSubscriptionActivity
	 * 
	 * @param place
	 * @param clientFactory
	 */
	@Inject
	public AnalyticsTaskSubscriptionActivity(IAnalyticsTaskSubscriptionView view) {
		super(view);
	}

	/**
	 * Bind view widgets to activity.
	 */
	@Override
	protected void bindToView() {
		display.setPresenter(this);
		display.getUsersList().setSelectionModel(usersListSelectionModel);
		display.getSubsribersList().setSelectionModel(subscribersListSelectionModel);

		logger.info("Calling get Reporting Task");
	}

	/**
	 * Method to get all the virtual factory users
	 */
	public void getVirtualFactoryUserList() {
		/*
		 * clientFactory.daoRequestFactory().personRequest().
		 * getVirtualFactoryUsersList().fire(new Receiver<List<PersonProxy>>(){
		 * 
		 * @Override public void onSuccess(List<PersonProxy> users) {
		 * 
		 * removeDuplicates(users); List<PersonProxy> effectiveUsers = new
		 * ArrayList<PersonProxy>(users); for (PersonProxy user : users) { for
		 * (PersonProxy subscriber :
		 * display.getSubsribersList().getVisibleItems()) { if
		 * (user.getId().longValue() == subscriber.getId().longValue()) {
		 * effectiveUsers.remove(user); break; } } }
		 * 
		 * sortPersonList(effectiveUsers);
		 * display.getUsersList().setRowData(effectiveUsers);
		 * ldapUsersList=effectiveUsers;
		 * 
		 * }
		 * 
		 * 
		 * public void onFailure(ServerFailure error) {
		 * display.showError("Unable to retrieve Virtual Factory users ",error);
		 * } });
		 */
	}

	/**
	 * TO remove the Users from report Subscription list.
	 */
	@Override
	public void onRemoveUsers() {

		final Set<PersonProxy> selectedUsers = ((MultiSelectionModel<PersonProxy>) subscribersListSelectionModel).getSelectedSet();
		if (selectedUsers.size() <= 0) {
			display.showError("Please select atleast one subsriber to move to the user list");
		} else {
			display.getErrorPanel().clear();
			List<PersonProxy> selectedUsersList = new ArrayList<PersonProxy>(selectedUsers);

			List<PersonProxy> subscribersList = display.getSubscribersEditor().getList();
			for (PersonProxy person : selectedUsersList) {
				for (PersonProxy subscriber : subscribersList) {
					if (person.getId().longValue() == subscriber.getId().longValue()) {
						display.getSubscribersEditor().getList().remove(subscriber);
						break;
					}
				}
			}

			ldapUsersList.addAll(selectedUsersList);
			sortPersonList(ldapUsersList);
			display.getUsersList().setRowCount(0);
			display.getUsersList().setRowData(0, ldapUsersList);
			((MultiSelectionModel<PersonProxy>) subscribersListSelectionModel).clear();
		}

	}

	/**
	 * To add the VF user's into the Report Subscription list.
	 */
	@Override
	public void onAddVirtualFactoryUsers() {

		final Set<PersonProxy> selectedUsers = ((MultiSelectionModel<PersonProxy>) usersListSelectionModel).getSelectedSet();
		if (selectedUsers.size() <= 0) {
			display.showError("Please select atleast one the user to move to subsribers list");
		} else {
			display.getErrorPanel().clear();
		}

		List<PersonProxy> selectedUsersList = new ArrayList<PersonProxy>(selectedUsers);
		ldapUsersList.removeAll(selectedUsersList);
		sortPersonList(ldapUsersList);
		display.getUsersList().setRowCount(0);
		display.getUsersList().setRowData(0, ldapUsersList);
		List<PersonProxy> currentSubscribersList = display.getSubscribersEditor().getList();
		currentSubscribersList.addAll(selectedUsersList);
		sortPersonList(display.getSubscribersEditor().getList());
		((MultiSelectionModel<PersonProxy>) usersListSelectionModel).clear();
	}

	/**
	 * Called when Save Button is Clicked.
	 */
	@Override
	public void onSave() {
		Window.alert("TODO");
	}

	/**
	 * Called when Cancel Button is Clicked.
	 */
	@Override
	public void onCancel() {
	}

	/**
	 * Helper function to sort list of users
	 * 
	 * @param personList
	 */
	public void sortPersonList(List<PersonProxy> personList) {
		Collections.sort(personList, new Comparator<PersonProxy>() {
			public int compare(PersonProxy person1, PersonProxy person2) {
				return person1.getEmail().compareTo(person2.getEmail());
			}
		});
	}

	/**
	 * Helper function to remove duplicate entries in the given list
	 * 
	 * @param list
	 */
	public static void removeDuplicates(List<PersonProxy> list) {
		Set<Long> uniqueEntries = new HashSet<Long>();
		for (Iterator<PersonProxy> iter = list.iterator(); iter.hasNext();) {
			PersonProxy task = iter.next();
			if (!uniqueEntries.add(task.getId())) // if current element is a
													// duplicate,
				iter.remove();// remove it
		}
	}
}
