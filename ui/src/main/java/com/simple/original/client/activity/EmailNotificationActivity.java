package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.place.EmailNotificationPlace;
import com.simple.original.client.place.MonitoringPlace;
import com.simple.original.client.proxy.AnalyticsTaskMonitorProxy;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.view.IEmailNotificationView;

public class EmailNotificationActivity extends AbstractActivity<EmailNotificationPlace, IEmailNotificationView> implements IEmailNotificationView.Presenter {

    private List<String> userEmailList = new ArrayList<String>();

    /**
     * Logger instance
     */
    private static final Logger logger = Logger.getLogger(EmailNotificationActivity.class.getName());

    /**
     * Constructor
     * 
     * @param place
     * @param clientFactory
     */
    @Inject
    public EmailNotificationActivity(IEmailNotificationView view) {
    	super(view);
    	display.getErrorPanel().clear();
    	display.getAlertSubscriptionList().setValue("Updating data");
    	display.getSubject().setValue("");
    	display.getDescription().setValue("");
    }

    /**
     * Bind view widgets to activity.
     */
    @Override
    protected void bindToView() {
        getUserSubscriptionEmailList();

        /**
         * This is used to handle Maximum characters allowed in the TextArea
         * using keypressHandler As there is no SetMaxCharacters option for
         * TextArea
         */
        HandlerRegistration messageBodyEntered = display.getDescription().addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                int maxCharactersAllowed = 8124;

                if (display.getDescription().getValue().length() > maxCharactersAllowed) {
                    display.getDescription().setValue(display.getDescription().getValue().substring(0, maxCharactersAllowed - 1));
                }
            }
        });
        handlers.add(messageBodyEntered);

        // ClickHandler when we click send button

        HandlerRegistration clickSendHandler = display.getSendNotification().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                if (!(display.getSubject().getValue().trim().isEmpty()) && !(display.getDescription().getValue().trim().isEmpty())) {
                    try {
                    	display.getErrorPanel().clear();
                        display.setCancelEnabled(false);
                        String subject = display.getSubject().getValue();
                        String description = display.getDescription().getValue();
                        sendEmailNotificationAsText(userEmailList, subject, description);
                        placeController().goTo(new MonitoringPlace());

                    } catch (Exception e) {
                        display.showError(" Mail Not Sent ");
                        logger.info("EmailNotificationActivity -> Mail Not Sent"+e.getMessage());
                        display.setCancelEnabled(false);
                        display.getSendNotification().setEnabled(true);
                    }
                } else {
                    if (!(display.getSubject().getValue().trim().isEmpty()) && !(display.getDescription().getValue().trim().isEmpty())) {
                        display.showError(" Subject and Message body cannot be blank ");
                    } else if (display.getSubject().getValue().trim().isEmpty()) {
                        display.showError(" Subject  cannot be blank ");
                    } else {
                        display.showError(" Message body  cannot be blank ");
                    }
                }
            }

        });
        handlers.add(clickSendHandler);

        HandlerRegistration clickCancelHandler = display.getCancelNotification().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	display.getErrorPanel().clear();
                if (display.getDescription().getValue().isEmpty()) {
                    placeController().goTo(new MonitoringPlace());
                } else {
                    boolean confirm = Window.confirm("Are you sure you want to cancel alert notifications?");
                    if (confirm) {
                        display.getSendNotification().setEnabled(true);
                        placeController().goTo(new MonitoringPlace());
                    }
                }
            }
        });
        // Add handler so it will be cleaned up.
        handlers.add(clickCancelHandler);

    }

    /**
     * This method retrieves user subscription emails ids and populate into
     * alert subscription text box.
     */
    public void getUserSubscriptionEmailList() {

        logger.info("Entered into EmailNotificationActivity-getUserList()");

        // Used to retrieve the HP Email's subscribed for the particular alert
        // using preferences table

        dao().alertDefinitionRequest().find(place().getArgs().getId()).with("subscribers.preferences").fire(new Receiver<AnalyticsTaskMonitorProxy>() {
            @Override
            public void onSuccess(AnalyticsTaskMonitorProxy response) {
                display.getSubject().setValue(place().getArgs().getName() + " -");
                if (response.getSubscribers() != null && response.getSubscribers().size() > 0) {
                	StringBuilder alertSubscriptionList = new StringBuilder();
                    logger.info("EmailNotificationActivity -> subscribers not null");
                    for (PersonProxy person : response.getSubscribers()) {
                        userEmailList.add(person.getPreferences().getSubscriberMailId());
                        alertSubscriptionList.append(person.getPreferences().getSubscriberMailId()).append("\n");
                    }
                    display.getAlertSubscriptionList().setValue(alertSubscriptionList.toString());
                    display.getSendNotification().setEnabled(true);
                }else{
                	display.getAlertSubscriptionList().setValue("No users Subscribed for this alert");
                    display.getSendNotification().setEnabled(false);
                    display.showError("Cannot send mail because no user subscribed for this alert");
                    logger.info("EmailNotificationActivity -> No users Subscribed for this alert");
                }

                if (!response.getAlertStatus()) {
                    display.getSendNotification().setEnabled(false);
                    display.showError("Cannot send mail because Alert is inactive");
                }

            }


            @Override
            public void onFailure(ServerFailure error) {
                logger.info("EmailNotificationActivity-getUserList : Failed when finding alert");
            }
        });
        display.setPresenter(this);
    }

    /**
     * This method sends email notification to the subscribers.
     * 
     * @param emailList
     *            lists that needs to notify
     * @param subject
     *            Subject to display in the email.
     * @param description
     *            description to be displayed in the email.
     * @return true if mail is sent successful else false.
     */
    public void sendEmailNotificationAsText(List<String> emailList, String subject, String description) {
    	logger.info("sendEmailNotificationAsText ");
    }
}
