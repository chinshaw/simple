package com.simple.original.server.service.scheduler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPFactor;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPRaw;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.RList;

import com.simple.domain.metric.Metric;
import com.simple.domain.metric.MetricCollection;
import com.simple.domain.metric.MetricDouble;
import com.simple.domain.metric.MetricMatrix;
import com.simple.domain.metric.MetricPlot;
import com.simple.domain.metric.MetricString;
import com.simple.domain.metric.NumberRange;
import com.simple.domain.metric.Violation;
import com.simple.domain.metric.MetricMatrix.DoubleColumn;
import com.simple.domain.metric.MetricMatrix.IntegerColumn;
import com.simple.domain.metric.MetricMatrix.StringColumn;
import com.simple.original.api.analytics.Criticality;
import com.simple.original.api.analytics.IHasViolations;
import com.simple.original.api.analytics.IMetric;
import com.simple.original.api.analytics.IMetricMatrix;
import com.simple.original.api.analytics.IViolation;
import com.simple.original.api.exceptions.RAnalyticsException;

// TODO change these strings to constants and put them somewhere.
public class MetricUtils {

    public static final String METRIC_PACKAGE = "VFAnalytics";
    public static final String METRIC_STRING = "MetricString";
    public static final String METRIC_NUMBER = "MetricNumber";
    public static final String METRIC_PLOT = "MetricPlot";
    public static final String METRIC_COLLECTION = "MetricDataFrame";
    public static final String METRIC_MATRIX = "MetricMatrix";

    public static final String RANGE_MIN = "min";
    public static final String RANGE_MAX = "max";
    public static final String RANGE_CRITICALITY = "criticality";

    // private static final Logger logger =
    // Logger.getLogger(MetricUtils.class.getName());

    public static MetricDouble asNumber(String name, REXP rexp) throws REXPMismatchException {
        MetricDouble metric = new MetricDouble();
        metric.setName(name);
        return bind(metric, rexp);
    }

    public static MetricString asString(String name, REXP rexp) throws REXPMismatchException {
        return bind(new MetricString(name), rexp);
    }

    /**
     * Returns a rexp as a metric collection this does not descend down more
     * than one level.
     * 
     * @param name
     * @param rexp
     * @return
     * @throws REXPMismatchException
     */
    public static MetricCollection asCollection(String name, REXP rexp) throws REXPMismatchException {
        return bind(new MetricCollection(name), rexp);
    }

    /**
     * This will try to figure out what type of metric it is and return you the
     * metric from the rexp object. This uses the REXP.inhertits function to
     * figure out if the type is found.
     * 
     * @param name
     *            The name of the object. All metrics must have a name.
     * @param rexp
     * @return The raw Metric<?> object.
     * @throws REXPMismatchException
     */
    public static Metric asMetric(String name, REXP rexp) throws REXPMismatchException {
        Metric metric = null;

        if (isMetricNumber(rexp)) {
            metric = asNumber(name, rexp);
        }

        if (isMetricString(rexp)) {
            metric = asString(name, rexp);
        }

        if (isMetricCollection(rexp)) {
            metric = asCollection(name, rexp);
        }

        // checkForViolations(metric, rexp);

        return metric;
    }

    public static MetricDouble bind(MetricDouble metricNumber, REXP rexp) throws REXPMismatchException {
        REXP rexpRanges = rexp.getAttribute("ranges");

        if (rexpRanges.isList()) {
            RList ranges = rexpRanges.asList();

            if (ranges != null && !rexp.isNull()) {
                for (Iterator<?> iter = ranges.listIterator(); iter.hasNext();) {
                    REXP rexpRange = (REXP) iter.next();
                    NumberRange range = asRange(rexpRange);

                    if (range != null) {
                        metricNumber.getRanges().add(range);
                    }
                }
            }
        }

        metricNumber.setValue(getAttributeValueDouble(rexp, "value"));

        checkForViolations(metricNumber, rexp);
        return metricNumber;
    }

    public static MetricString bind(MetricString metricString, REXP rexp) throws REXPMismatchException {
        metricString.setValue(((REXPString) rexp.getAttribute("value")).asString());
        checkForViolations(metricString, rexp);

        return metricString;
    }

    public static MetricPlot bind(MetricPlot metric, REXP rexp) throws RAnalyticsException {
        if (rexp instanceof REXPRaw) {
            return bindRawPlot(metric, (REXPRaw)rexp); 
        }
        
        if (rexp.hasAttribute("plot")) {
            REXPRaw plotAttribute = (REXPRaw) rexp.getAttribute("plot");
            return bindRawPlot(metric, plotAttribute);
        }
        throw new RAnalyticsException("Did not supply a valid REXP object to bind(MetricPlot), rexp is " + rexp.toDebugString());
    }
    
    
    private static MetricPlot bindRawPlot(MetricPlot metric, REXPRaw rawPlot) throws RAnalyticsException {
        InputStream in = null;

        byte[] imageData = rawPlot.asBytes();
        metric.setImageData(imageData);
        if (imageData.length > 0) {
            in = new ByteArrayInputStream(imageData);
            try {
                ImageInputStream iis = ImageIO.createImageInputStream(in);
                ImageReader reader = ImageIO.getImageReaders(iis).next();
                String imageFormat = reader.getFormatName();
                metric.setImageFormat(imageFormat);
                
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new RAnalyticsException("Error occurred while reading our static graphic " + e.getMessage());
            }
        }
        return metric;
    }
    

    public static MetricCollection bind(MetricCollection metricCollection, REXP rexp) throws REXPMismatchException {

        RList lst = rexp.asList();

        // int colCount = lst.size();
        int rowCount = lst.at(0).length();

        @SuppressWarnings("unchecked")
        Vector<String> columnNames = lst.names;

        for (String columnName : columnNames) {
            MetricCollection collumnCollection = new MetricCollection(columnName);

            RList column = lst.at(columnName).asList();

            for (int r = 0; r < rowCount; r++) {
                REXP v = column.at(r);
                Metric metric = asMetric("", v);
                collumnCollection.add(metric);
            }

            metricCollection.add(collumnCollection);
        }
        return metricCollection;
    }

    public static IMetricMatrix bind(MetricMatrix metric, REXP rexp) throws REXPMismatchException {
        RList dList = rexp.asList();
        
        int colCount = dList.size();
        
        for (int colIndex = 0; colIndex < colCount; colIndex++) {
            String columnHeader = dList.keyAt(colIndex);
            REXP columnValues = dList.at(colIndex);
            
            // WOW this has to be first because factor is also an integer even
            // though the value contains strings. WTF.
            if (columnValues instanceof REXPFactor) {
                String[] values = columnValues.asStrings();
                StringColumn column = new StringColumn(columnHeader, values);
                metric.getValue().add(column);
                continue;
            }

            if (columnValues instanceof REXPInteger) {
                int[] integers = columnValues.asIntegers();
                IntegerColumn integerColumn = new IntegerColumn(columnHeader, integers);
                metric.getValue().add(integerColumn);
                continue;
            }

            if (columnValues instanceof REXPDouble) {
                double[] values = columnValues.asDoubles();
                DoubleColumn column = new DoubleColumn(columnHeader, values);
                metric.getValue().add(column);
                continue;
            }

            if (columnValues instanceof REXPString) {
                String[] values = columnValues.asStrings();
                StringColumn column = new StringColumn(columnHeader, values);
                metric.getValue().add(column);
                continue;
            }
        }

        System.out.println("DEBUGGGG  size of metric is "+ metric.getColumns().size());
        return metric;
    }

    public static Metric createMetric(String name, REXP rexp) throws REXPMismatchException, RAnalyticsException {
        Metric metric = null;

        if (rexp.inherits(METRIC_NUMBER)) {
            metric = new MetricDouble(name);
            bind((MetricDouble) metric, rexp);
        } else if (rexp.inherits(METRIC_MATRIX)) {
            metric = new MetricMatrix(name);
            bind((MetricMatrix) metric, rexp);
        } else if (rexp.inherits(METRIC_STRING)) {
            metric = new MetricString(name);
            bind((MetricString) metric, rexp);
        } else if (rexp.inherits(METRIC_COLLECTION)) {
            metric = new MetricCollection(name);
            bind((MetricCollection) metric, rexp);
        } else if (rexp.inherits(METRIC_PLOT)) {
            metric = new MetricPlot(name);
            bind((MetricPlot)metric, rexp);
        } else if (rexp instanceof REXPRaw) {
            // REXPRaw should be a plot.
            metric = new MetricPlot(name);
            bind((MetricPlot) metric, rexp);
        }

        if (metric == null) {
            throw new IllegalArgumentException("Invalid rexp class type " + rexp.toDebugString());
        }

        return metric;
    }

    public static IMetric bindMetric(IMetric metric, REXP rexp) throws REXPMismatchException, RAnalyticsException {
        if (metric instanceof MetricDouble) {
            bind((MetricDouble) metric, rexp);
        } else if (metric instanceof MetricString) {
            bind((MetricString) metric, rexp);
        } else if (metric instanceof MetricCollection) {
            bind((MetricCollection) metric, rexp);
        } else if (metric instanceof MetricMatrix) {
            bind((MetricMatrix)metric, rexp);
        } else if (metric instanceof MetricPlot) {
            bind((MetricPlot) metric, rexp);
        }

        return metric;
    }

    public static NumberRange asRange(REXP rexp) throws REXPMismatchException {
        NumberRange range = null;
        if (isRange(rexp)) {
            range = new NumberRange();
            range.setMinimum(getAttributeValueDouble(rexp, RANGE_MIN));
            range.setMaximum(getAttributeValueDouble(rexp, RANGE_MAX));

            Integer criticality = getAttributeValueInteger(rexp, RANGE_CRITICALITY);
            System.out.println("Criticality is " + criticality);
            if (criticality != null) {
                try {
                    range.setCriticality(Criticality.values()[criticality - 1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    range.setCriticality(Criticality.UNKNOWN);
                }
            }
        }
        return range;
    }

    /**
     * First tests to see if the object is indeed a violation and if so it will
     * build up the violation from the rexp object retrieving the rule, detail
     * severity and subgroup.
     * 
     * @param rexp
     * @return The violation or null.
     * @throws REXPMismatchException
     */
    public static Violation asViolation(REXP rexp) throws REXPMismatchException {
        Violation violation = null;
        if (isViolation(rexp)) {
            violation = new Violation();

            violation.setRuleName(getAttributeValueString(rexp, IViolation.RTYPE_RULE));
            violation.setDetail(getAttributeValueString(rexp, IViolation.RTYPE_DETAIL));
            violation.setSubgroup(getAttributeValueInteger(rexp, IViolation.RTYPE_SUBGROUP));

            // Get the severity from the class.
            Integer integerSeverity = getAttributeValueInteger(rexp, IViolation.RTYPE_SEVERITY);
            if (integerSeverity != null) {
                IViolation.Severity severity = IViolation.Severity.values()[integerSeverity];
                violation.setSeverity(severity);
            }

        }
        return violation;
    }

    /**
     * Get a value as string. This will check to make sure the value is not null
     * and that the value is indeed a string before trying to get the attribute.
     * 
     * @param source
     * @param attribute
     * @return The string value or null if not found.
     * @throws REXPMismatchException
     */
    public static String getAttributeValueString(REXP source, String attribute) throws REXPMismatchException {
        String value = null;
        REXP rexp = getAttribute(source, attribute);
        if (rexp != null && rexp.isString()) {
            value = rexp.asString();
        }
        return value;
    }

    /**
     * Get an attribute value as integer. THis has checks to make sure the
     * object is not null and also that the value is numeric.
     * 
     * @param source
     * @param attribute
     * @return The Integer or null if not found.
     * @throws REXPMismatchException
     */
    public static Integer getAttributeValueInteger(REXP source, String attribute) throws REXPMismatchException {
        Integer value = null;
        REXP rexp = getAttribute(source, attribute);
        System.out.println("rexp is " + rexp.toDebugString());
        if (rexp != null && !rexp.isNull() && rexp.isNumeric()) {
            int[] values = rexp.asIntegers();
            if (values.length > 0) {
                value = values[0];
            }
        }
        return value;
    }

    public static Double getAttributeValueDouble(REXP source, String attribute) throws REXPMismatchException {
        Double value = null;
        REXP rexp = getAttribute(source, attribute);
        if (rexp != null && !rexp.isNull() && rexp.isNumeric()) {
            double[] values = rexp.asDoubles();
            if (values.length > 0) {
                value = values[0];
            }
        }
        return value;
    }

    /**
     * Used to get an attribute by the name. This has a check to see if the
     * object has the attribute first before trying to get the attribute.
     * 
     * @param source
     * @param attribute
     * @return
     */
    public static REXP getAttribute(REXP source, String attribute) {
        REXP rexp = null;
        if (source.hasAttribute(attribute)) {
            rexp = source.getAttribute(attribute);
        }
        return rexp;
    }

    /**
     * This will check for violations in the metric and add them to the metrics
     * list of violations if it finds any.
     * 
     * @param metric
     * @param rexp
     * @throws REXPMismatchException
     */
    private static void checkForViolations(Metric metric, REXP rexp) throws REXPMismatchException {
        REXP violationsAttribute = getAttribute(rexp, "violations");

        if (metric instanceof IHasViolations) {
            if (violationsAttribute == null) {
                return;
            }

            if (violationsAttribute.isList()) {
                RList violations = violationsAttribute.asList();
                if (!violations.isEmpty()) {
                    int violationCount = violations.size();
                    for (int i = 0; i < violationCount; i++) {
                        REXP rViolation = violations.at(i);
                        metric.getViolations().add(asViolation(rViolation));
                    }
                }
            }
        }
    }

    /**
     * Test to see if the rexp is a metric collection.
     * 
     * @param rexp
     * @return
     */
    public static final boolean isMetricCollection(REXP rexp) {
        return isType(rexp, METRIC_COLLECTION);
    }

    /**
     * Test to see if rexp is metric string
     * 
     * @param rexp
     * @return
     */
    public static final boolean isMetricString(REXP rexp) {
        return isType(rexp, METRIC_STRING);
    }

    /**
     * Test to see if rexp is metric number
     * 
     * @param rexp
     * @return
     */
    public static final boolean isMetricNumber(REXP rexp) {
        return isType(rexp, METRIC_NUMBER);
    }

    /**
     * Test to see if rexp is a violation.
     * 
     * @param rexp
     * @return
     */
    public static final boolean isViolation(REXP rexp) {
        return isType(rexp, IViolation.RTYPE);
    }

    public static final boolean isRange(REXP rexp) {
        return isType(rexp, NumberRange.RTYPE);
    }

    /**
     * Method to test the type of the object. It takes a rexp object and a
     * string type This uses the rexp inherits function to see if the object has
     * that class type.
     * 
     * @param rexp
     * @param type
     * @return
     */
    private static boolean isType(REXP rexp, String type) {
        if (rexp.inherits(type)) {
            return true;
        }
        return false;
    }

    public static String getPackage(REXP rexp) throws REXPMismatchException {
        String pkg = "";

        if (rexp != null) {
            REXP klass = rexp.getAttribute("class");
            if (klass != null) {
                REXP _pkg = klass.getAttribute("package");
                if (_pkg != null) {
                    pkg = _pkg.asString();
                }
            }
        }
        return pkg;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Metric> Set<T> getMetricsFromCollection(Collection<Metric> metrics, Class<T> type) {
        Set<T> found = new HashSet<T>();

        for (Metric metric : metrics) {
            if (metric.getClass().isAssignableFrom(type)) {
                found.add((T) metric);
            }
        }

        return found;
    }

    /**
     * Happy place for metric utils
     * 
     * @return
     */
    public static Map<String, IMetric> mericListAsMap(List<? extends IMetric> metrics) {
        Map<String, IMetric> map = new HashMap<String, IMetric>();

        for (IMetric metric : metrics) {
            if (metric == null || metric.getName() == null) {
                throw new IllegalArgumentException("metrics contained a null metric value or name");
            }
            
            map.put(metric.getName(), metric);
        }
        
        return map;
    }

}