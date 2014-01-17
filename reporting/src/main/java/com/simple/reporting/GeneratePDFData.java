package com.simple.reporting;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Point;
import java.text.NumberFormat;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer.Pin;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.simple.domain.model.ui.dashboard.Dashboard;
import com.simple.original.api.analytics.Criticality;
import com.simple.original.api.analytics.IMetricDouble;
import com.simple.original.api.analytics.INumberRange;

public class GeneratePDFData {
	//private static Logger logger = Logger.getLogger(GeneratePDFData.class.getName());

	static final Color LIGHTGREY_COLOR = Color.lightGray;
	static final Color DARKGREY_COLOR = Color.darkGray;
	static final Color BLUE_COLOR = Color.blue;
	static final Color YELLOW_COLOR = Color.yellow;
	static final Color RED_COLOR = Color.red;
	static final Color GREEN_COLOR = Color.green;
	static final Color PIN_COLOR = new Color(255, 255, 255);

	static final java.awt.Font TEXT_FONT = new java.awt.Font("Dialog", 1, 14);

	static final Font TABLE_DATA_FONT = new Font(Font.HELVETICA, 12, Font.NORMAL);

	static final Font TABLE_HEADER_FONT = new Font(Font.HELVETICA, 14, Font.BOLD);

	// Here Specify the column width of the table
	static final int headerwidths[] = { 15, 15, 15, 10, 10, 10, 10, 10, 10, 10 };

	public static JFreeChart generateDial(IMetricDouble metric, String dialLable) {

		//double dialLowRangeMin = values.getLowRange().getMinimum();
		//double dialLowRangeMax = values.getLowRange().getMaximum();

		//double dialMidRangeMin = values.getMidRange().getMinimum();
		//double dialMidRangeMax = values.getMidRange().getMaximum();

		//double dialHighRangeMin = values.getHighRange().getMinimum();
		//double dialHighRangeMax = values.getHighRange().getMaximum();

		//double tickerValue = (dialHighRangeMax - dialLowRangeMin) / 10;

		double gaugeValue = metric.getValue();

		//logger.info("dialLowRangeMin=" + dialLowRangeMin + "  dialLowRangeMax=" + dialLowRangeMax + " dialMidRangeMin" + dialMidRangeMin + " dialMidRangeMax" + dialMidRangeMax
		//		+ " dialHighRangeMin=" + dialHighRangeMin + " dialHighRangeMax=" + dialHighRangeMax);

		DefaultValueDataset dataset1 = new DefaultValueDataset(gaugeValue);
		DialPlot dialplot = new DialPlot();
		dialplot.setView(0.0, 0.0, 1.0, 1.0);
		dialplot.setDataset(0, dataset1);

		StandardDialFrame standarddialframe = new StandardDialFrame();
		standarddialframe.setBackgroundPaint(LIGHTGREY_COLOR);
		standarddialframe.setForegroundPaint(DARKGREY_COLOR);
		dialplot.setDialFrame(standarddialframe);

		GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(170, 170, 220));
		DialBackground dialbackground = new DialBackground(gradientpaint);
		dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
		dialplot.setBackground(dialbackground);

		if (dialLable != null && dialLable.trim().length() > 0) {
			DialTextAnnotation dialtextannotation = new DialTextAnnotation(dialLable);
			dialtextannotation.setFont(TEXT_FONT);
			dialtextannotation.setRadius(0.5);
			dialplot.addLayer(dialtextannotation);
		}

		StandardDialScale standarddialscale = new StandardDialScale();
		for (INumberRange range : metric.getRanges()) {
			standarddialscale.setLowerBound(range.getMinimum());
			standarddialscale.setUpperBound(range.getMaximum());
			
			StandardDialRange standarddialrange = new StandardDialRange(range.getMinimum(), range.getMaximum(), asColor(range.getCriticality()));
			dialplot.addLayer(standarddialrange);
		}
		standarddialscale.setStartAngle(-120D);
		standarddialscale.setExtent(-300D);
		//standarddialscale.setMajorTickIncrement(tickerValue);
		standarddialscale.setMinorTickCount(0);
		standarddialscale.setTickLabelFont(TEXT_FONT);
		standarddialscale.setTickRadius(0.88D);
		standarddialscale.setTickLabelOffset(0.15);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setParseIntegerOnly(true);
		nf.setGroupingUsed(false);
		standarddialscale.setTickLabelFormatter(nf);

		dialplot.addScale(0, standarddialscale);

		dialplot.mapDatasetToScale(1, 0);

		Pin pin = new Pin(0);
		pin.setPaint(new GradientPaint(10, 10, PIN_COLOR, 200, 200, PIN_COLOR));
		dialplot.addPointer(pin);

		JFreeChart jfreechart = new JFreeChart(dialplot);
		TextTitle title = new TextTitle();
		title.setFont(TEXT_FONT);
		title.setText("" + metric.getName());
		jfreechart.setTitle(title);

		return jfreechart;
	}

	public static void generatePdfTable(Document document, Dashboard dashboard) throws DocumentException {

		/*
		PdfPTable datatable = null;
		List<Metric> metricsList = (List<Metric>) completion.getExecutionMetrics();

		for (Metric metric : metricsList) {
			if (metric instanceof MetricCollection) {
				List<Metric> metricValues = ((MetricCollection) metric).getValue();
				List<Metric> firstMetricsList = ((MetricCollection) metricValues.get(0)).getValue();

				datatable = new PdfPTable(metricValues.size());

				datatable.setWidthPercentage(100);
				datatable.getDefaultCell().setPadding(5);
				datatable.setHeaderRows(1);
				datatable.getDefaultCell().setBorderWidth(1);
				datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				for (int j = 0; j < metricValues.size(); j++) {
					PdfPCell cell = new PdfPCell();
					cell.setGrayFill(0.9f);
					cell.setPhrase(new Phrase(((MetricCollection) metricValues.get(j)).getName().toUpperCase(), TABLE_HEADER_FONT));
					datatable.addCell(cell);
				}
				datatable.completeRow();

				for (int i = 0; i < firstMetricsList.size(); i++) {
					for (int j = 0; j < metricValues.size(); j++) {
						List<Metric> metrics = ((MetricCollection) metricValues.get(j)).getValue();
						PdfPCell cell = new PdfPCell();
						IMetricDouble val = (IMetricDouble) metrics.get(i);
						cell.setPhrase(new Phrase(val.getValue() + "", TABLE_DATA_FONT));
						datatable.addCell(cell);
					}
					datatable.completeRow();
				}
			}
		}

		if (datatable != null) {
			document.add(datatable);
		}
		*/
	}
	
	static Color asColor(Criticality criticality) {
		
		switch(criticality) {
		case NORMAL:
			return Color.GREEN;
		case WARNING:
			return Color.YELLOW;
		case CRITICAL:
			return Color.RED;
		default:
			return Color.GRAY;
		}
	}
}
