package com.simple.original.client.view.widgets;

import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.SimplePanel;

public class PopupPanel extends SimplePanel implements HasAnimation, HasCloseHandlers<PopupPanel> {

    @Override
    public HandlerRegistration addCloseHandler(CloseHandler<PopupPanel> handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAnimationEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setAnimationEnabled(boolean enable) {
        // TODO Auto-generated method stub
        
    }

}
