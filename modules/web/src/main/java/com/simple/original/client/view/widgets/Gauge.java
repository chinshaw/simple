package com.simple.original.client.view.widgets;

import java.util.List;

import com.google.gwt.canvas.dom.client.Context2d;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel.IGaugeModelRange;

public abstract class Gauge extends CanvasWidget {
	// private static final Logger logger = Logger.getLogger("GaugeWidget");

	public static class Options {

		private int height;
		
		private int width;
		
		private double min;
		
		private double max;
		
		private List<IGaugeModelRange> ranges;

		private int totalTicks;

		private boolean shadowEnabled = true;

		public Options() {
			this.totalTicks = 8;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public List<IGaugeModelRange> getRanges() {
			return ranges;
		}

		public void setRanges(List<IGaugeModelRange> ranges) {
			this.ranges = ranges;
			updateRangeMax();
			updateRangeMin();
		}

		public void setTotalTicks(int totalTicks) {
			this.totalTicks = totalTicks;
		}

		protected int getTotalTicks() {
			return totalTicks;
		}

		public void setShadowEnabled(boolean shadowEnabled) {
			this.shadowEnabled = shadowEnabled;
		}

		public boolean isShadowEnabled() {
			return this.shadowEnabled;
		}

		private void updateRangeMax() {
			this.max = 0.0;
			if (ranges != null) {
				for (IGaugeModelRange range : ranges) {
					if (range.getMaximum() > this.max) {
						this.max = range.getMaximum();
					}
				}
			}
		}
		
		private void updateRangeMin() {
			this.min = getGaugeRangeMax();
			if (ranges != null) {
				for (IGaugeModelRange range : ranges) {
					if (range.getMinimum() < this.min) {
						min = range.getMinimum();
					}
				}
			}
		}
		
		public double getGaugeRangeMax() {
			return this.max;
		}
		
		public double getGaugeRangeMin() {
			return this.min;
		}
	}

	protected final Context2d context;

	protected final Point center;

	protected Number gaugeValue;

	protected Options options;

	public Gauge(Double gaugeValue, Options options) {
		this.gaugeValue = gaugeValue;
		this.options = options;
		center = new Point((options.width + 15) / 2, (options.height + 15) / 2);

		context = canvas.getContext2d();
	}

	public abstract void draw();

	public Options getOptions() {
		return options;
	}
}
