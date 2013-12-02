package com.simple.original.client.view.widgets;

import com.google.gwt.canvas.dom.client.Context2d;

public class BubbleGaugeSimple extends BubbleGauge {

    
    
    public BubbleGaugeSimple(double gaugeValue, Options options) {
        super(gaugeValue, options);
        draw();
    }

    /**
     * This is called from the parent gauge class after initialization of the
     * base canvas context. This will run through the animation and draw the gauge's
     * correct value.
     */
    @Override
    public void draw() {
        canvas.setCoordinateSpaceHeight(options.getHeight() + 5);
        canvas.setCoordinateSpaceWidth(options.getWidth() + 5);
        
        initBackground();
        context.drawImage(background.getCanvasElement(), 0, 0);
        drawValue();
        drawCenter();
    }
    
    
    /**
     * This function is used to create the background. This background will
     * be cached for later rendering. This function is only called once to create
     * the background canvas and later is simply added to the parent context.
     */
    private void initBackground() {
        Context2d backgroundContext = background.getContext2d();
        drawOuter(backgroundContext);
    }    
}