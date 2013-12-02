package com.simple.original.client.view.widgets;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.Composite;

public class CanvasWidget extends Composite {

    protected final Canvas canvas = Canvas.createIfSupported();

    public static final double ARC_FULL_CIRCLE = Math.PI * 2;
    
    static class Point {
        double x;
        double y;

        public Point() {
            this(0, 0);
        }

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
    
    public CanvasWidget() {
        initWidget(canvas);
    }
    
    
    /**
     * Gets the radian for a whole number in degrees.
     * @param degree
     * @return
     */
    public double getRadianForDegree(double degree) {
        return (degree * (Math.PI / 180));
    }
}
