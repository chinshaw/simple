package com.simple.reporting;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jfree.chart.JFreeChart;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.simple.domain.dashboard.Dashboard;
import com.simple.domain.metric.MetricDouble;
import com.simple.domain.metric.NumberRange;
import com.simple.original.api.analytics.IMetricDouble;

/**
 * Class used to create PDF which includes dial(s), Table(s) & Images(s)
 * 
 * @author hemantha
 */
public class PDFGenerator {

	private static Logger logger = Logger.getLogger(PDFGenerator.class
			.getName());

	static final float dialWidth = 250f;
	static final float dialHeight = 250f;
	static final float space = 30f;

	static final float default_dialPosition_X = space;
	static float default_dialPosition_Y = 1350;

	static float dialPosition_X = default_dialPosition_X;
	static float dialPosition_Y = default_dialPosition_Y;

	/**
	 * Generates PDf file based on data available for drawing dials,writing
	 * Images and Tables
	 * 
	 * @return
	 * @throws ReportGenerationException
	 */
	public static void generatePDF(Dashboard dashboard)
			throws ReportGenerationException {

		Document document = null;
		File pdfFile = null;
		document = new Document();
		setUpDocument(document);
		default_dialPosition_Y = document.getPageSize().getHeight()
				- dialHeight - space;

		dialPosition_X = default_dialPosition_X;
		dialPosition_Y = default_dialPosition_Y;

		try {

			String pdfFilePath = "WTF";
			pdfFile = new File(pdfFilePath);
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(pdfFile, false));
			document.open();
			// write Dial to PDF
			boolean dialStatus = writeDialToPDF(document, writer, dashboard);

			if (dialStatus || !dialStatus) {
				// write Image to PDF
				boolean chartStatus = writeImageToPDF(document, dashboard);

				if (chartStatus || !chartStatus) {
					// write table data to PDF
					writeTableToPDF(document, dashboard);
				}
			}

		} catch (MalformedURLException e) {
			throw new ReportGenerationException("Image Path is not correct : ",
					e);
		} catch (FileNotFoundException e) {
			throw new ReportGenerationException(
					"Error while creating PDF file : ", e);
		} catch (IOException e) {
			throw new ReportGenerationException(
					"Error while adding Image to PDF file : ", e);
		} catch (DocumentException e) {
			throw new ReportGenerationException(
					"Error while creating Document", e);
		} finally {
			if (document.isOpen()) {
				document.close();
			}
		}
	}

	/**
	 * To set document properties
	 * 
	 * @param doc
	 */
	private static void setUpDocument(Document doc) {
		doc.addTitle("Create a PDF Document with gauges, Images and Tables");
		doc.addKeywords("Java, PDF ");
		doc.addAuthor("HP-VirtualFactory");
		// A4 = 210mm x 297mm ~ 605points x 855points
		doc.setPageSize(PageSize.A2);
		doc.setMargins(72f, 72f, 72f, 72f);
		logger.info("document.Width() ->" + doc.getPageSize().getWidth()
				+ " , document.Height() ->" + doc.getPageSize().getHeight());
	}

	/**
	 * writes dials to PDF
	 * 
	 * @param document
	 * @param writer
	 * @param taskExecution
	 * @return
	 * @throws DocumentException
	 */
	private static boolean writeDialToPDF(Document document, PdfWriter writer,
			Dashboard dashboard) throws DocumentException {
		
		boolean dialGenerated = false;
		/*
		List<String> addParagraphsList = new ArrayList<String>();

		List<Widget> widgetsList = dashboard.getWidgets();
		List<IMetricDouble> gaugeWidgetList = new ArrayList<IMetricDouble>();

		for (Widget widget : widgetsList) {
			if (widget instanceof GaugeWidget) {
				IMetricDouble gaugeValues = (IMetricDouble) ((GaugeWidget) widget)
						.getMetric();
				if (gaugeValues == null) {
					addParagraphsList
							.add(" MetricNumber is null for widget Id : "
									+ widget.getId());
				} else {
					List<? extends IMetric> metricsList = dashboard.getExecutionMetrics();
					for (IMetric metric : metricsList) {
						if (metric instanceof IMetricDouble
								&& metric.getName().equals(
										gaugeValues.getName())) {
							List<String> errorMessageList = validateMetricNumber((IMetricDouble) metric);
							if (errorMessageList.size() == 0) {
								gaugeWidgetList.add((IMetricDouble) metric);
							} else {
								addParagraphsList.addAll(errorMessageList);
							}
						}
					}
				}
			}

			dialGenerated = generateDial(document, writer, gaugeWidgetList);
		}

		for (String message : addParagraphsList) {
			addParagraph(document, message);
		}
		*/
		return dialGenerated;
	}

	/**
	 * 
	 * validates MetricNumber data
	 * 
	 * @param gaugeValues
	 * @return
	 */
	private static List<String> validateMetricNumber(IMetricDouble gaugeValues) {
		List<String> errorMessageList = new ArrayList<String>();
		/*
		 * String errorMessage1 = gaugeValues.getLowRange() == null ?
		 * " MetricNumber[Id : "+gaugeValues.getId()+"].getLowRange() is null" :
		 * gaugeValues.getLowRange().getMinimum() == null ?
		 * " MetricNumber[Id : "
		 * +gaugeValues.getId()+"].getLowRange().getMinimum() is null" :
		 * gaugeValues.getLowRange().getMaximum() == null ?
		 * " MetricNumber[Id : "
		 * +gaugeValues.getId()+"].getLowRange().getMaximum() is null" : null;
		 * 
		 * String errorMessage2 = gaugeValues.getMidRange() == null ?
		 * " MetricNumber[Id : "+gaugeValues.getId()+"].getMidRange() is null" :
		 * gaugeValues.getMidRange().getMinimum() == null ?
		 * " MetricNumber[Id : "
		 * +gaugeValues.getId()+"].getMidRange().getMinimum() is null" :
		 * gaugeValues.getMidRange().getMaximum() == null ?
		 * " MetricNumber[Id : "
		 * +gaugeValues.getId()+"].getMidRange().getMaximum() is null" : null;
		 * String errorMessage3 = gaugeValues.getHighRange() == null ?
		 * " MetricNumber[Id : "+gaugeValues.getId()+"].getHighRange() is null"
		 * : gaugeValues.getHighRange().getMinimum() == null ?
		 * " MetricNumber[Id : "
		 * +gaugeValues.getId()+"].getHighRange().getMinimum() is null" :
		 * gaugeValues.getHighRange().getMaximum() == null ?
		 * " MetricNumber[Id : "
		 * +gaugeValues.getId()+"].getHighRange().getMaximum() is null" : null;
		 * String errorMessage4 = gaugeValues.getDoubleValue() == null ?
		 * " MetricNumber[Id : "
		 * +gaugeValues.getId()+"].getDoubleValue() is null" : null;
		 * if(errorMessage1 != null){ errorMessageList.add(errorMessage1); }
		 * if(errorMessage2 != null){ errorMessageList.add(errorMessage2); }
		 * if(errorMessage3 != null){ errorMessageList.add(errorMessage3); }
		 * if(errorMessage4 != null){ errorMessageList.add(errorMessage4); }
		 */
		return errorMessageList;
	}

	/**
	 * generates dial and writes to PDF
	 * 
	 * @param document
	 * @param writer
	 * @param gaugeWidgetList
	 * @return
	 * 
	 * @throws DocumentException
	 */
	public static boolean generateDial(Document document, PdfWriter writer,
			List<IMetricDouble> gaugeWidgetList) throws DocumentException {
		boolean dialFlag = false;
		for (IMetricDouble gaugeValues : gaugeWidgetList) {
			logger.info("gaugeValues name : " + gaugeValues.getName());
			String dialLable = null;
			PdfContentByte contentByte = writer.getDirectContent();
			JFreeChart chart = GeneratePDFData.generateDial(gaugeValues,
					dialLable);
			PdfTemplate template = contentByte.createTemplate(dialWidth,
					dialHeight);
			Graphics2D graphics2d = template.createGraphics(dialWidth,
					dialHeight, new DefaultFontMapper());
			Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, dialWidth,
					dialHeight);
			chart.draw(graphics2d, rectangle2d);
			graphics2d.dispose();
			logger.info("Current Dial Positions [X,Y] -> [" + dialPosition_X
					+ "  , " + dialPosition_Y + " ]");
			contentByte.addTemplate(template, dialPosition_X, dialPosition_Y);
			dialFlag = true;
			setNextDialPosition(document);
		}
		addNewLines(document, 18);
		return dialFlag;
	}

	/**
	 * set X & Y positions of Dial
	 * 
	 * @param document
	 * @param addHeightSpace
	 * @throws DocumentException
	 */
	private static void setNextDialPosition(Document document)
			throws DocumentException {
		float addWidthSpace = dialWidth + space;
		float addHeightSpace = dialHeight + space;

		logger.fine(" Before changing values - dialPosition_X ->"
				+ dialPosition_X + " , dialPosition_Y ->" + dialPosition_Y);
		if (dialPosition_X + 2 * addWidthSpace <= document.getPageSize()
				.getWidth()) {
			dialPosition_X += addWidthSpace;
			logger.fine("dialPosition_X ->" + dialPosition_X
					+ " , document.Width() ->"
					+ document.getPageSize().getWidth());

		} else {
			if (dialPosition_Y - addHeightSpace > 0) {
				dialPosition_X = default_dialPosition_X;
				dialPosition_Y -= addHeightSpace;
				logger.fine("dialPosition_X ->" + dialPosition_X
						+ "  ,dialPosition_Y ->" + dialPosition_Y);
				addNewLines(document, (int) dialHeight / 10);
			} else {
				logger.info(" Added new page for dials ");
				addNewLines(document, (int) dialHeight / 10);
				dialPosition_X = default_dialPosition_X;
				dialPosition_Y = default_dialPosition_Y;

				logger.fine("Reset X & Y  ,dialPosition_X ->" + dialPosition_X
						+ " , dialPosition_Y ->" + dialPosition_Y);
			}
		}
		logger.fine("Next Dial Positions [X,Y] -> [" + dialPosition_X + "  , "
				+ dialPosition_Y + " ]");
	}

	/**
	 * writes charts to PDF
	 * 
	 * @param document
	 * @param taskExecution
	 * @return
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static boolean writeImageToPDF(Document document,
			Dashboard dashboard) throws DocumentException,
			MalformedURLException, IOException {
		/*
		 * List<String> imagePaths = new
		 * AnalyticsTaskExecutionDao().getChartsPath(taskExecution);
		 * if(imagePaths != null && imagePaths.size() > 0){ String
		 * chartsDirectoryPath =
		 * ServerProperties.instance().getChartsDirectory(); for(String
		 * imagePath :imagePaths){ Image image =
		 * Image.getInstance(chartsDirectoryPath+imagePath);
		 * document.add(image); } }else{
		 * addParagraph(document,"No Charts available"); }
		 */

		addNewLines(document, 2);
		return true;
	}

	/**
	 * 
	 * writes violations data to Table in PDF
	 * 
	 * @param document
	 * @param taskExecution
	 * @throws DocumentException
	 */
	private static void writeTableToPDF(Document document,
			Dashboard dashboard) throws DocumentException {
		GeneratePDFData.generatePdfTable(document, dashboard);
		addNewLines(document, 2);
	}

	/**
	 * adds newlines to document
	 * 
	 * @param document
	 * @param noOfLines
	 * @throws DocumentException
	 */
	private static void addNewLines(Document document, int noOfLines)
			throws DocumentException {
		// please do not remove below lines of code. this to a void overlapping
		document.add(new Paragraph(" "));
		for (int var1 = 0; var1 < noOfLines; var1++) {
			document.add(Chunk.NEWLINE);
		}
	}

	/**
	 * adds newlines to document
	 * 
	 * @param document
	 * @param noOfLines
	 * @throws DocumentException
	 */
	private static void addParagraph(Document document, String message)
			throws DocumentException {
		document.add(new Paragraph(message));
		addNewLines(document, 2);
	}

	/**
	 * generates PDf file with sample data (testing purpose) WTF
	 * 
	 * @return
	 * 
	 *         public synchronized static boolean generatePDFDemo(){ Document
	 *         document = null; File pdfFile = null; document = new Document();
	 *         setUpDocument(document); default_dialPosition_Y =
	 *         document.getPageSize().getHeight() - dialHeight - space;
	 * 
	 *         dialPosition_X = default_dialPosition_X; dialPosition_Y =
	 *         default_dialPosition_Y;
	 * 
	 *         try {
	 * 
	 *         String pdfFilePath = ServerProperties.getPDFFileNameWithPath(new
	 *         Long(12345)); pdfFile = new File(pdfFilePath); PdfWriter writer =
	 *         PdfWriter.getInstance(document, new
	 *         FileOutputStream(pdfFile,false)); document.open();
	 * 
	 *         sampleDial(document,writer); sampleImage(document);
	 * 
	 *         } catch (MalformedURLException e) {
	 *         logger.severe("Image Path is not correct : "+e.getMessage());
	 *         return false; } catch (FileNotFoundException e) {
	 *         logger.severe("Error while creating PDF file : "+e.getMessage());
	 *         return false; } catch (IOException e) {
	 *         logger.severe("Error while adding Image to PDF file : "
	 *         +e.getMessage()); return false; } catch (DocumentException e) {
	 *         logger.severe("Error while creating Document : "+e.getMessage());
	 *         return false; }finally{ if(document.isOpen()){ document.close();
	 *         } } return true; }
	 */

	/**
	 * draw sample dial in pdf (for testing dial use this method)
	 * 
	 * @param document
	 * @param writer
	 * @throws DocumentException
	 */
	private static void sampleDial(Document document, PdfWriter writer)
			throws DocumentException {
		Double lowMin = new Double(-30);
		Double lowMax = new Double(70);

		Double midMin = new Double(70);
		Double midMax = new Double(120);

		Double highMin = new Double(120);
		Double highMax = new Double(220);

		Double value = new Double(-5);
		List<IMetricDouble> gaugeWidgetList = new ArrayList<IMetricDouble>();
		for (int var1 = 1; var1 <= 3; var1++) {
			IMetricDouble metric = new MetricDouble();

			NumberRange lowRange = new NumberRange();
			lowRange.setMinimum(lowMin);
			lowRange.setMaximum(lowMax);

			NumberRange midRange = new NumberRange();
			midRange.setMinimum(midMin);
			midRange.setMaximum(midMax);

			NumberRange highRange = new NumberRange();
			highRange.setMinimum(highMin);
			highRange.setMaximum(highMax);

			// metric.setLowRange(lowRange);
			// metric.setMidRange(midRange);
			// metric.setHighRange(highRange);
			if (var1 % 2 == 0) {
				value = new Double(85);
			} else if (var1 % 3 == 0) {
				value = new Double(115);
			} else if (var1 % 5 == 0) {
				value = new Double(155);
			}
			metric.setValue(value);
			metric.setName("Sample Dial " + var1);

			gaugeWidgetList.add((IMetricDouble) metric);
		}

		generateDial(document, writer, gaugeWidgetList);
	}
}
