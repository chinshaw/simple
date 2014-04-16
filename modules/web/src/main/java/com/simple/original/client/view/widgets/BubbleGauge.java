package com.simple.original.client.view.widgets;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.TakesValue;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel.IGaugeModelRange;

public class BubbleGauge extends Gauge implements TakesValue<Double> {

	private class GaugeValueAnimation extends Animation {

		private double startValue = 0;
		private double diff = 0;

		@Override
		protected void onUpdate(double progress) {
			// Set the gauge value.
			gaugeValue = startValue - (diff * progress);
			// Clear the screen
			clear();
			context.drawImage(background.getCanvasElement(), 0, 0);

			drawValue();
			drawCenter();
		}

		@Override
		protected void onComplete() {
			super.onComplete();
		}

		@Override
		protected void onCancel() {

		}

		public void run(double startValue, double endValue, int duration) {
			this.startValue = startValue;
			diff = startValue - endValue;
			run(duration);
		}
	}

	// private static final Logger logger = Logger.getLogger("BubbleGauge");
	/**
	 * This is the starting degree of the gauge on the left (/
	 */
	private final int START_DEGREE = 145;
	/**
	 * This is the end degree on the right side of the gauge. \)
	 */
	private final int END_DEGREE = 395;
	/**
	 * Total number of degrees for the
	 */
	protected final int TOTAL_DEGREES = END_DEGREE - START_DEGREE;

	/**
	 * This is the largest radius of the outside of the gauge. If you think of
	 * the gauge as a complete circle this will be the value of the outside
	 * radius.
	 */
	protected int maxRadius = options.getWidth() / 2;;

	/**
	 * Gauge animation that makes the dial go from value to value.
	 */
	private GaugeValueAnimation animation;

	private static final NumberFormat valueFormatter = NumberFormat.getFormat("#.##");

	/**
	 * This is the pre-render canvas used to draw the background so that we
	 * don't have to re-render the entire background every time the animation
	 * updates the dial.
	 */
	protected Canvas background = Canvas.createIfSupported();

	/**
	 * TODO this needs to be finished. The animation needs a toggle to slow it
	 * down. Right now it kills cpu when animating the gauge dial.
	 */
	// private CriticalAnimation criticalAnimation;

	public BubbleGauge(Options options) {
		super(0d, options);
	}
	
	public BubbleGauge(Double value, Options options) {
		super(value, options);
		draw();
	}

	/**
	 * This is used to update the options of the gauge and will call draw to re
	 * render the gauge.
	 * 
	 * @param options
	 */
	public void setOptions(Options options, boolean redraw) {
		this.options = options;
		if (redraw) {
			redraw();
		}
	}

	/**
	 * This is called from the parent gauge class after initialization of the
	 * base canvas context. This will run through the animation and draw the
	 * gauge's correct value.
	 */
	@Override
	public void draw() {
		canvas.setCoordinateSpaceHeight(options.getHeight() + 25);
		canvas.setCoordinateSpaceWidth(options.getWidth() + 25);

		initBackground();

		animation = new GaugeValueAnimation();

		// This is a check to see if the value is larger than the maximum value.
		// If it is we set the value to the max and stop. This keeps the gauge
		// from
		// spinning around multiple times on large numbers. We don't want to
		// modify the
		// Gauge value though so that it still shows the correctly value at the
		// bottom.

		animation.run(options.getGaugeRangeMin(), gaugeValue.doubleValue(), 3000);
	}

	/**
	 * This function is used to create the background. This background will be
	 * cached for later rendering. This function is only called once to create
	 * the background canvas and later is simply added to the parent context.
	 */
	private void initBackground() {
		Context2d backgroundContext = background.getContext2d();
		drawOuter(backgroundContext);

		addValues(backgroundContext);
	}

	/**
	 * Draw the outer grey circle.
	 * 
	 * @param context
	 *            Context to add the circle to.
	 */
	protected void drawOuter(Context2d context) {

		if (options.isShadowEnabled()) {
			context.setShadowBlur(8);
			context.setShadowColor("#333333");
		}

		context.moveTo(center.x, center.y);
		context.setStrokeStyle("#333333");
		context.setFillStyle("#DDDDDD");
		context.setGlobalAlpha(.8d);
		
		context.arc(center.x, center.y, maxRadius, getRadianForDegree(START_DEGREE - 5), getRadianForDegree(END_DEGREE + 5), false);
		context.lineTo(center.x, center.y);
		context.fill();

		context.moveTo(center.x - 12, center.y);
		int radius = (int) Math.round(maxRadius * .90);

		if (options.getRanges() != null) {
			for (IGaugeModelRange range : options.getRanges()) {
				drawColoredRing(context, range.getMaximum(), getAngleForValue(range.getMinimum()), getAngleForValue(range.getMaximum()), radius, range.getColor(),
						range.getColor(), true);
			}
		}
	}

	protected void drawValue() {
		double x, y;

		context.beginPath();
		context.setFont("12px Arial");
		context.setFillStyle("#333333");
		String value = "" + valueFormatter.format(gaugeValue);
		double textWidth = (context.measureText(value).getWidth());

		x = (int) 0 - (textWidth / 2);
		y = (int) 0 + (options.getHeight() / 2) * .75;

		context.fillText(value, center.x + x, center.y + y);
		context.fill();
		context.restore();

		if (gaugeValue.doubleValue() > options.getGaugeRangeMax()) {
			gaugeValue = options.getGaugeRangeMax();
		}

		context.save();

		if (options.isShadowEnabled()) {
			context.setShadowBlur(8);
			context.setShadowColor("#333333");
		}

		context.translate(center.x, center.y);

		// Have to subtract the gauge min because the gauge may not start at
		// 0.
		int currentDegree = (int) (getDegreesOffset() * (gaugeValue.doubleValue() - options.getGaugeRangeMin()) + START_DEGREE);
		context.rotate(getRadianForDegree(currentDegree));
		context.setFillStyle("#CCCCCC");

		context.beginPath();
		x = (options.getWidth() / 2) * .45;
		y = (options.getHeight() / 2) * .1;
		context.moveTo(x, -y);
		context.lineTo(x, y);
		context.lineTo(x + (y * 2), 0);
		context.closePath();
		context.fill();

		context.restore();

	}

	protected void drawCenter() {
		context.save();

		if (options.isShadowEnabled()) {
			context.setShadowBlur(8);
			context.setShadowColor("#333333");
		}

		context.beginPath();
		context.setFillStyle("#CCCCCC");
		context.arc(center.x, center.y, maxRadius * .45, 0, ARC_FULL_CIRCLE);
		context.closePath();
		context.fill();

		context.beginPath();

		context.restore();

		// Drawing the light mark on the center to make it appear as though it
		// is raised. We need to calculate
		// an offset smaller than the size of the center circle which at this
		// point it .38 so we set the offset to be .30
		// This gives the center the effect of being raised kind of like the
		// easy button :)

		// Need to figure out what color the center gauge is going to be.
		if (options.getRanges() != null) {
			for (IGaugeModelRange range : options.getRanges()) {
				if (gaugeValue.doubleValue() >= range.getMinimum() && gaugeValue.doubleValue() <= range.getMaximum()) {
					drawCenterCircle("#FFFFFF", range.getColor());
					break;
				}
			}
		}
	}

	private void drawCenterCircle(String fromColor, String toColor) {
		long offset = Math.round(maxRadius * .10);
		context.save();

		CanvasGradient gradient = context.createRadialGradient(center.x - offset, center.y - offset, 3, center.x + 2, center.y, 16);

		if (options.isShadowEnabled()) {
			context.setShadowBlur(8);
			context.setShadowColor("#333333");
		}

		context.setFillStyle(gradient);
		gradient.addColorStop(0, fromColor);
		gradient.addColorStop(1, toColor);
		context.arc(center.x, center.y, Math.round(maxRadius * .38), 0, 2 * Math.PI);
		context.closePath();
		context.fill();

		context.restore();
	}

	public void setGaugeValue(double gaugeValue) {
		animation.run(this.gaugeValue.doubleValue(), gaugeValue, 1000);
		this.gaugeValue = gaugeValue;
	}

	private void clear() {
		context.clearRect(0, 0, canvas.getOffsetHeight(), canvas.getOffsetWidth());
	}

	private void clearBackground() {
		clear();
		background.getContext2d().clearRect(0, 0, canvas.getOffsetHeight(), canvas.getOffsetWidth());

	}

	private void drawColoredRing(Context2d context, double maxValue, double startDegree, double endDegree, double radius, String startColor, String endColor, boolean outerColor) {
		// Gradient for all the colors.

		context.save();
		context.beginPath();
		CanvasGradient gradient = context.createRadialGradient(center.x, center.y + 6, radius - 10, center.x, center.y + 6, radius);
		gradient.addColorStop(0, startColor);
		gradient.addColorStop(1, endColor);

		context.setFillStyle(gradient);

		context.arc(center.x, center.y, radius, getRadianForDegree(startDegree), getRadianForDegree(endDegree));

		double centerY = center.y;

		if (outerColor) {
			centerY = center.y - 8;
		}

		context.lineTo(center.x, centerY);
		context.closePath();
		context.setGlobalAlpha(.6);
		context.fill();
		context.restore();
	}

	/**
	 * Gets an angle for a specific value.
	 * 
	 * @param value
	 *            Value to get angle for.
	 * @return
	 */
	private double getAngleForValue(double value) {
		return (value - options.getGaugeRangeMin()) * getDegreesOffset() + START_DEGREE;
	}

	/**
	 * Get a radian for a numeric gauge value.
	 * 
	 * @param value
	 *            Numeric value in the gauge range.
	 * @return
	 */
	private double getRadianForValue(double value) {
		return getRadianForDegree(getAngleForValue(value));
	}

	/**
	 * Add the values to the outside of the gauge.
	 * 
	 * @param context
	 */
	private void addValues(Context2d context) {
		context.setFillStyle("#333333");

		if (options.getRanges() != null) {
			for (IGaugeModelRange range : options.getRanges()) {
				addValue(context, range.getMaximum());
			}
		}
	}

	private double getDegreesOffset() {
		double gaugeRange = options.getGaugeRangeMax() - options.getGaugeRangeMin();
		return (gaugeRange == 0) ? 0 : TOTAL_DEGREES / gaugeRange;
	}

	/**
	 * Add an individual value to the outside of a gauge.
	 * 
	 * @param context
	 * @param value
	 */
	private void addValue(Context2d context, double value) {
		double radian = getRadianForValue(value);

		double xCoord = (center.x + (maxRadius * Math.cos(radian)));
		double yCoord = (center.y + (maxRadius * Math.sin(radian)));

		context.fillText("" + value, xCoord, yCoord);
	}

	@Override
	public void setValue(Double value) {
		setValue(value, true);
	}
	
	public void setValue(Number value, boolean fastDraw) {
		this.gaugeValue = value;
		if (fastDraw) {
			draw();
		} else {
			redraw();
		}
	}

	private void redraw() {
		clear();
		clearBackground();
		draw();
	}

	@Override
	public Double getValue() {
		return gaugeValue.doubleValue();
	}
}
