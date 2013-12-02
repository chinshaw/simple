package com.simple.original.client.view.widgets;


public class GWTGauge extends Gauge {

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}
	
	public GWTGauge(double gaugeValue, Options options) {
		super(gaugeValue, options);
		
	}
	
	
	// This needs to be converted to the new system that uses arbitrary ranges.
/*
    private final double RADIUS_START = 140;
    private final double RADIUS_END = 40;
    private final double TOTAL_RADIANS = (360 - RADIUS_START) + RADIUS_END;

    private final double GAUGE_RANGE;

    private final int outerArcRadius;
    private final int innerArcRadius;

    public GWTGauge(double gaugeValue, Options options) {
        super(gaugeValue, options);

        GAUGE_RANGE = options.getGaugeRangeMax() - options.getGaugeRangeMin();
        // Get the center of the canvas.
        
        outerArcRadius = (int) (center.x - 10);
        innerArcRadius = outerArcRadius - 14;
    }

    public void draw() {
        // Draw the circles
        drawOuterCircles();

        // Draw the color ranges before we draw the ticks so that the ticks show
        // up correctly.
        drawColorRanges();

        // Draw the ticks around the circle.
        drawTicks();

        // finally draw the pointer.
        drawValue();
    }

    private void drawOuterCircles() {
        // Add four to the height and width so we don't chop off part of our
        // gauge.

        // Draw the outer thin black circle circle.
        context.beginPath();
        context.setLineWidth(2);
        context.setStrokeStyle("#333333");
        context.setFillStyle("#CCCCCC");
        context.arc(center.x, center.y, center.x - 1, 0, Math.PI * 2, true);
        context.closePath();
        context.stroke();
        context.fill();

        // Draw the next inner grey circle.
        context.beginPath();
        context.setLineWidth(4);
        context.setStrokeStyle("#e0e0e0");
        context.arc(center.x, center.y, (center.x - 7), 0, Math.PI * 2, true);
        context.closePath();

        context.setFillStyle("#f7f7f7");
        context.stroke();
        context.fill();

    }

    private void drawValue() {

        context.beginPath();
        context.setStrokeStyle("#dc3912");
        context.setLineWidth(3);
        context.moveTo(center.x, center.y);

        double percentageOfRange = (gaugeValue - options.getGaugeRangeMin() / GAUGE_RANGE);

        double x = (center.x + (outerArcRadius * Math.cos(TOTAL_RADIANS * percentageOfRange + RADIUS_START * Math.PI / 180)));
        double y = (center.y + (outerArcRadius * Math.sin(TOTAL_RADIANS * percentageOfRange + RADIUS_START * Math.PI / 180)));
        context.lineTo(x, y);
        context.closePath();
        context.stroke();

        // Draw the center
        context.beginPath();
        context.setLineWidth(1);

        if (gaugeValue < options.getGreenRangeMax()) {
            context.setFillStyle("#109618");
        } else if (gaugeValue < options.getYellowRangeMax()) {
            context.setFillStyle("#ff9900");
        } else {
            context.setFillStyle("#dc3912");
        }

        context.setStrokeStyle("#666666");
        context.arc(center.x, center.y, 10, 0, Math.PI * 2, true);
        context.closePath();
        context.fill();
        context.stroke();

        // Add the start and end of the range numbers.
        x = center.x + (innerArcRadius * Math.cos(90 * Math.PI / 180));
        y = center.y + (outerArcRadius * Math.sin(90 * Math.PI / 180));

        context.setFont("12px Arial");
        context.setFillStyle("#000000");
        String value = "" + gaugeValue;
        double textWidth = (context.measureText(value).getWidth());
        context.fillText(value, x - (textWidth / 2), y - 3);
    }

    private void drawTicks() {

        int totalTicks = options.getTotalTicks();
        double arc = TOTAL_RADIANS / totalTicks;

        for (int i = 0; i <= totalTicks; i++) {
            int radius = innerArcRadius;
            if (i % 2 == 1) {
                context.setLineWidth(.5);
                radius += 5;
            } else {
                context.setLineWidth(2);
            }

            double start_x = (center.x + (radius * Math.cos(((i * arc) + RADIUS_START) * Math.PI / 180)));
            double start_y = (center.y + (radius * Math.sin(((i * arc) + RADIUS_START) * Math.PI / 180)));
            double end_x = (center.x + (outerArcRadius * Math.cos(((i * arc) + RADIUS_START) * Math.PI / 180)));
            double end_y = (center.y + (outerArcRadius * Math.sin(((i * arc) + RADIUS_START) * Math.PI / 180)));

            context.beginPath();
            context.moveTo(start_x, start_y);
            context.lineTo(end_x, end_y);
            context.setStrokeStyle("#333333");
            context.closePath();
            context.stroke();
        }

        // Add the Gauge range values to the start and end of the gauge values.
        double x = center.x + (innerArcRadius * Math.cos(RADIUS_START * Math.PI / 180));
        double y = center.y + (innerArcRadius * Math.sin(RADIUS_START * Math.PI / 180));

        context.save();

        context.setFont("6px Arial");
        context.setFillStyle("#333333");
        context.fillText("" + options.getGaugeRangeMin(), x + 1, y);
        context.stroke();

        x = center.x + (innerArcRadius * Math.cos(RADIUS_END * Math.PI / 180));
        y = center.y + (innerArcRadius * Math.sin(RADIUS_END * Math.PI / 180));

        String maxRange = "" + options.getGaugeRangeMax();
        // Need to subtract the width of the range
        context.fillText(maxRange, x - (context.measureText(maxRange).getWidth()), y);
        context.restore();
    }

    private void drawColorRanges() {
        drawRange(options.getGreenRangeMin(), options.getGreenRangeMax(), "#109618");
        drawRange(options.getYellowRangeMin(), options.getYellowRangeMax(), "#ff9900");
        drawRange(options.getRedRangeMin(), options.getRedRangeMax(), "#dc3912");
    }

    private void drawRange(double min, double max, String color) {
        // Need to figure out where on our gauge our green will start so we take
        // the total number of radians and multiply it times the percentage of
        // the range.
        double startRadian = (TOTAL_RADIANS * ((min - options.getGaugeRangeMin()) / GAUGE_RANGE)) + RADIUS_START;
        double endRadian = (TOTAL_RADIANS * ((max - options.getGaugeRangeMin()) / GAUGE_RANGE)) + RADIUS_START;

        context.beginPath();

        // context.setStrokeStyle("green");
        context.setFillStyle(color);

        // draw the first arc.
        context.arc(center.x, center.y, outerArcRadius, (Math.PI / 180) * startRadian, (Math.PI / 180) * endRadian, false);

        // Next need to draw the small piece before we draw the second arc. This
        // needs to point back to the center so.
        double x = (center.x + (innerArcRadius * Math.cos(endRadian * Math.PI / 180)));
        double y = (center.y + (innerArcRadius * Math.sin(endRadian * Math.PI / 180)));

        context.lineTo(x, y);

        // draw the second arc.
        context.arc(center.x, center.y, innerArcRadius, (Math.PI / 180) * endRadian, (Math.PI / 180) * startRadian, true);

        // Close the path and fill it.
        context.closePath();
        context.fill();
    }*/
}
