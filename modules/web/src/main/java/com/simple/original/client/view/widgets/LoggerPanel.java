package com.simple.original.client.view.widgets;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.logging.client.HtmlLogFormatter;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.simple.original.client.resources.Resources;

public class LoggerPanel extends Handler {

    private final FlowPanel container = new FlowPanel();

    /**
     * Our container that holds the html log messages.
     */
    private final FlowPanel loggerPanel = new FlowPanel();

    /**
     * This is our animation to show and hide the panel.
     */
    private Animation animation = new Animation() {

        private static final int rightMargin = -650;

        @Override
        protected void onUpdate(double progress) {
            if (!isShowing()) {
                getElement().getStyle().setMarginRight(rightMargin - (progress * rightMargin), Unit.PX);
            } else {
                getElement().getStyle().setMarginRight(progress * rightMargin, Unit.PX);
            }
        }

        protected void onComplete() {
            super.onComplete();
            setShowing(showing ? false : true);
        }
    };

    /**
     * Boolean if the panel is currently showing or not.
     */
    private boolean showing = false;

    @Inject
    public LoggerPanel(Resources resources) {
        setFormatter(new HtmlLogFormatter(true));
        setLevel(Level.ALL);

        container.setStyleName(resources.style().loggingPanel());
        loggerPanel.setStyleName(resources.style().loggingMessages());
        container.add(loggerPanel);
        
        Button closeButton = new Button();
        closeButton.setStyleName(resources.style().modalCloseButton());
        closeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                toggle();
            }
        });

        container.add(closeButton);

        RootPanel.get().add(container);
    }

    @Override
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        Formatter formatter = getFormatter();
        String msg = formatter.format(record);
        // We want to make sure that unescaped messages are not output as HTML
        // to
        // the window and the HtmlLogFormatter ensures this. If you want to
        // write a
        // new formatter, subclass HtmlLogFormatter and override the
        // getHtmlPrefix
        // and getHtmlSuffix methods.
        loggerPanel.add(new HTML(msg));

        // If we get a severe exception and it is not showing then we will
        // show the panel.
        if (record.getLevel() == Level.SEVERE && !showing) {
            toggle();
        }
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    private Element getElement() {
        return container.getElement();
    }

    /**
     * Set whether the panel is currently showing.
     * 
     * @param showing
     */
    private void setShowing(boolean showing) {
        this.showing = showing;
    }

    /**
     * Is the panel currently showing
     * 
     * @return
     */
    public boolean isShowing() {
        return showing;
    }

    public void toggle() {
        animation.run(1000);
    }

    public void clear() {
        loggerPanel.clear();
    }
}