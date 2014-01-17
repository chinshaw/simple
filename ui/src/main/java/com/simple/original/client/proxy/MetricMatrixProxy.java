package com.simple.original.client.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ExtraTypes;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.SkipInterfaceValidation;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.domain.model.metric.MetricMatrix;
import com.simple.domain.model.metric.MetricMatrix.Column;
import com.simple.domain.model.metric.MetricMatrix.DoubleColumn;
import com.simple.domain.model.metric.MetricMatrix.IntegerColumn;
import com.simple.domain.model.metric.MetricMatrix.StringColumn;
import com.simple.original.server.service.locators.IMetricLocator;

/**
 * A metric data frame is
 * 
 * @author chinshaw
 * 
 */
@ExtraTypes({ MetricMatrixProxy.ColumnProxy.class, MetricMatrixProxy.StringColumnProxy.class, MetricMatrixProxy.IntegerColumnProxy.class, 
        MetricMatrixProxy.DoubleColumnProxy.class })
@ProxyFor(value = MetricMatrix.class, locator = IMetricLocator.class)
// Need to remove this when a reasonable solution comes around.
// The problem with this is that getValue/ getColumns uses generics and
// validator is
// complaining.
@SkipInterfaceValidation
public interface MetricMatrixProxy extends MetricProxy {

    /**
     * Name of this object in the ui. This will commonly be extended by
     * subclasses.
     */
    public static String NAME = "Metric Matrix";
    
    /**
     * The proxy representation for a metric row. This will contain a list of
     * MetricCellProxy objects.
     * 
     * @author chinshaw
     */
    //@ProxyFor(value = com.simple.original.server.domain.dashboard.MetricTableWidget.MetricRow.class)
    //public static interface MetricRowProxy extends ValueProxy {
    //    public List<MetricCellProxy> getCells();
    //}

    @ProxyFor(value = Column.class)
    public interface ColumnProxy extends ValueProxy {
        public String getHeader();
    }

    @ProxyFor(value = StringColumn.class)
    public interface StringColumnProxy extends ColumnProxy {

        public List<MetricStringProxy> getValue();
    }

    @ProxyFor(value = IntegerColumn.class)
    public interface IntegerColumnProxy extends ColumnProxy {
        public List<MetricIntegerProxy> getValue();
    }

    @ProxyFor(value = DoubleColumn.class)
    public interface DoubleColumnProxy extends ColumnProxy {
        public List<MetricDoubleProxy> getValue();
    }
    
    public String getJsonUrl();
    
    //public List<ColumnProxy> getValue();

    //public List<String> getHeaders();
    
   // public List<MetricRowProxy> getRows();
}