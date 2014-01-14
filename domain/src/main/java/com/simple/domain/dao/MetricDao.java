package com.simple.domain.dao;

import javax.persistence.EntityManager;

import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.metric.MetricCollection;
import com.simple.domain.model.metric.MetricMatrix;
import com.simple.original.api.exceptions.AnalyticsTaskException;

public class MetricDao extends DaoBase<Metric> {

	
	public MetricDao() {
		super(Metric.class);
	}
	
    public MetricMatrix getMetricMatrix(Long matrixId) {
        MetricMatrix matrix = getEntityManager().find(MetricMatrix.class, matrixId);
        matrix.getColumns().size();
        return matrix;
    }

    public Integer getMetricMatrixRowCount(Long matrixId) {
        MetricMatrix matrix = getMetricMatrix(matrixId);
        try {
            return matrix.getColumns().get(0).getCells().size();
        } catch (NullPointerException e) {
            return 0;
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

   

    public MetricCollection getMetricCollection(Long collectionId) throws AnalyticsTaskException {
        return getEntityManager().find(MetricCollection.class, collectionId);
    }

    /**
     * This function is used to page in a Metric Collection to pull just a few
     * results. If the MetricColleciton contains multiple metric collecitons
     * this will work as a snapshot of just those rows. The good thing is that
     * this object will be cached by jpa so the next query should be pretty
     * quick.
     * 
     * @param metricCollectionId
     *            The id of the collection to pull results from.
     * @param start
     *            The starting metric row to get first.
     * @param max
     *            The number of rows to get from start.
     * @return A metric colleciton containing the offset of rows.
     * @throws AnalyticsTaskException
     */
    public MetricCollection getMetricCollection(Long metricCollectionId, int start, int max) throws AnalyticsTaskException {
        MetricCollection collection = new MetricCollection();

        EntityManager em = getEntityManager();

        Metric entity = em.find(Metric.class, metricCollectionId);

        if (entity == null) {
            throw new AnalyticsTaskException("Unable to locate the metric collection");
        }

        MetricCollection table = (MetricCollection) entity;

        for (Metric value : table.getValue()) {
            // If the next value is not a Metric collection then there is a
            // format
            // problem we were expecting a metric collection.
            if (!(value instanceof MetricCollection)) {
                throw new AnalyticsTaskException("Formatting constraint error on MetricCollecion, was expecting to find a metric colleciotn");
            }

            MetricCollection columnValue = (MetricCollection) value;
            collection.add(columnValue.getRange(start, (start + max)));
        }

        return collection;
    }
}