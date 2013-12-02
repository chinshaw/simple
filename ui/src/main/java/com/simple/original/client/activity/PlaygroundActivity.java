package com.simple.original.client.activity;

import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.simple.original.client.events.NotificationEvent;
import com.simple.original.client.place.PlaygroundPlace;
import com.simple.original.client.proxy.QuartzTriggerProxy;
import com.simple.original.client.view.desktop.PlaygroundViewImpl;
import com.simple.original.client.view.widgets.BubbleGauge;
import com.simple.original.client.view.widgets.BubbleGaugeSimple;
import com.simple.original.client.view.widgets.ColorPicker;
import com.simple.original.client.view.widgets.DefaultTextBox;
import com.simple.original.client.view.widgets.GWTGauge;

public class PlaygroundActivity extends AbstractActivity<PlaygroundPlace, PlaygroundViewImpl> {

	@Inject
    public PlaygroundActivity(PlaygroundViewImpl view) {
        super(view);
    }

    @Override
    protected void bindToView() {

        GWTGauge.Options options = new GWTGauge.Options();

        options.setHeight(116);
        options.setWidth(116);

        //options.setGaugeRange(0, 100);

        //options.setGreenRange(0, 40);

        //options.setYellowRange(40, 60);

        //options.setRedRange(60, 100);

        GWTGauge gauge = new GWTGauge(80, options);
        gauge.draw();

        display.getPlayground().add(gauge);

        Canvas canvas = Canvas.createIfSupported();
        canvas.setCoordinateSpaceHeight(100);
        canvas.setCoordinateSpaceWidth(100);
        Context2d context = canvas.getContext2d();

        context.setFillStyle("yellow");
        context.setShadowBlur(5);
        context.setShadowColor("#333333");
        context.setShadowOffsetX(3);
        context.setShadowOffsetY(1);
        context.arc(50, 50, 30, 0, Math.PI * 2);
        context.fill();
        
        
        display.getPlayground().add(canvas);

        BubbleGaugeSimple simpleBubbleGauge = new BubbleGaugeSimple(40, options);
        display.getPlayground().add(simpleBubbleGauge);

        final BubbleGauge bubbleGauge = new BubbleGauge(50d, options);
        display.getPlayground().add(bubbleGauge);

        // Value textbox
        final DefaultTextBox tb = new DefaultTextBox("gaugeValue asdf");
        tb.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    bubbleGauge.setGaugeValue(Double.valueOf(tb.getValue()));
                }
            }
        });

        display.getPlayground().add(tb);

        //fetchRunningJobs();

        Button showNotificationPanel = new Button("Show Notification Panel");
        showNotificationPanel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                for (int i = 0; i < 4; i++) {
                    NotificationEvent ne = new NotificationEvent("SHOWING PANEL MESSAGE " + i);
                    eventBus().fireEvent(ne);
                    // clientFactory.getNotificationPanel().showMessage("SHOWING PANEL MESSAGE "
                    // + i);
                }
            }
        });
        display.getPlayground().add(showNotificationPanel);
        
        FlowPanel sliderContainer = new FlowPanel();
        sliderContainer.setWidth("90%");
        
        
        display.getPlayground().add(new ColorPicker());
        
        display.getPlayground().add(new ColorPicker());
    }

    protected void fetchRunningJobs() {
        final TextArea ta = new TextArea();
        service().schedulerRequest().getScheduledJobs().fire(new Receiver<List<QuartzTriggerProxy>>() {

            @Override
            public void onSuccess(List<QuartzTriggerProxy> triggers) {
                String triggerDetails = "";
                for (QuartzTriggerProxy trigger : triggers) {
                    triggerDetails += trigger.getDescription();
                    triggerDetails += " Start time " + trigger.getStartTime();
                    triggerDetails += " End Time " + trigger.getEndTime();
                    triggerDetails += " next time " + trigger.getNextFireTime();
                    triggerDetails += " final time " + trigger.getFinalFireTime();
                }
                ta.setValue(triggerDetails);
                display.getPlayground().add(ta);
            }
        });
    }
}