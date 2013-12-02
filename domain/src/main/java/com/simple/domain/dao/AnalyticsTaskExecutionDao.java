package com.simple.domain.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.simple.domain.AlertViolationData;
import com.simple.domain.AnalyticsTaskExecution;
import com.simple.domain.metric.Metric;
import com.simple.domain.metric.MetricDouble;
import com.simple.original.api.analytics.IAnalyticsTaskExecution;
import com.simple.original.api.analytics.IViolation;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.api.domain.SortOrder;

public class AnalyticsTaskExecutionDao extends DaoBase<AnalyticsTaskExecution> implements IDaoRequest<AnalyticsTaskExecution> {

	private static final Logger logger = Logger.getLogger(AnalyticsTaskExecutionDao.class.getName());
	
	/**
	 * Default constructor that is injected with the
	 * entity manager.
	 * @param em
	 */
	public AnalyticsTaskExecutionDao() {
		super(AnalyticsTaskExecution.class);
	}
	
    public List<AnalyticsTaskExecution> findRange(Long analyticsTaskId, int start, int end) {
        EntityManager em = getEntityManager();
    	
        TypedQuery<AnalyticsTaskExecution> query = em.createQuery(
                "SELECT execution FROM " + AnalyticsTaskExecution.class.getName() + " as execution where execution.analyticsTask.id = (:analyticsTaskId) order by execution.endTime ASC",
                AnalyticsTaskExecution.class);
        
        query.setParameter("analyticsTaskId", analyticsTaskId);
        
        // If it is less than zero we will start from the end.
        if (start < 0) {
        	// Get the count of the number or task executions for this task.
        	Query countQuery = em.createNamedQuery("AnalyticsTaskExecution.countByTaskId");
        	countQuery.setParameter("taskId", analyticsTaskId);
            Long taskExecCount = (Long) countQuery.getSingleResult();
            
        	// Have to set it to the absolute value so that we can subtrace it from the total records.
        	int absStart = Math.abs(start);
        	// If the number of records is greater than the start then 
        	// we will set it. Otherwise just leave it alone
        	if (taskExecCount >= absStart) {
        		query.setFirstResult((int) (taskExecCount - absStart));
        	}
        } else {
        	query.setFirstResult(start);
        }
        
        // This is just here as a check from client.
        if (end > 0) {
        	query.setMaxResults(end);
        }

        return query.getResultList();
    }

    public List<AnalyticsTaskExecution> find(int start, int max) {
        EntityManager em = (EntityManager) getEntityManager();

        TypedQuery<AnalyticsTaskExecution> query = em.createQuery("SELECT e FROM AnalyticsTaskExecution e ORDER BY e.id DESC", AnalyticsTaskExecution.class);
        
        if (start >= 0) {
        	query.setFirstResult(start);
        }
        
        if (max >= 0) {
        	query.setMaxResults(max);
        }
        List<AnalyticsTaskExecution> resultList = (List<AnalyticsTaskExecution>) query.getResultList();
        return resultList;
    }
    
    /**
     * This method finds all the entities based on the input parameters and range passed. 
     * 
     * @param start			Starting record
     * @param max			Max number of records to fetch and return
     * @param recordType	RecordFecthType
     * @param searchText	Search text
     * @param searchColumn	Search column
     * @param sortColumn	Sort column
     * @param sortOrder		SortOrder
     * @return		List of entities
     */
    @SuppressWarnings("unchecked")
	public List<AnalyticsTaskExecution> find(int start, int max, RecordFecthType recordType, 
				String searchText, String searchColumn, String sortColumn, 
														SortOrder sortOrder) {
    	
    	Long currPersonId = getSession().getCurrentPerson().getId();

    	String jpql = "SELECT e FROM " + clazz.getName() + " e";
    	
    	
    	if (recordType == null) {
    		recordType = RecordFecthType.ALL_RECORDS;
    	}
        
    	if((searchText != null && searchText.length() > 0 ) && (searchColumn != null && searchText.length() > 0)) {
   	     	

    		if (recordType.equals(RecordFecthType.ALL_RECORDS)) {
    			jpql += " WHERE UPPER(e."+searchColumn+") LIKE '%"+ searchText.toUpperCase() +"%' ESCAPE '\\' ";
    			
    		} else {
    			jpql += " WHERE UPPER(e."+searchColumn+") LIKE '%"+ searchText.toUpperCase() +"%' ESCAPE '\\' AND ";
    		}
        } else if (!recordType.equals(RecordFecthType.ALL_RECORDS)){
        	jpql += " WHERE ";
        }
    	

    	if (recordType.equals(RecordFecthType.PUBLIC_RECORDS)) {
        	jpql += " e.analyticsTask.isPublic=true "; 
        	
        } else if (recordType.equals(RecordFecthType.USER_RECORDS)) {
        	jpql += " e.analyticsTask.isPublic=false ";
        	
        } else if (recordType.equals(RecordFecthType.MY_RECORDS)){
        	jpql += " e.analyticsTask.isPublic=false and e.analyticsTask.owner.id= "+currPersonId;
        	
        }
    	
        
        if (sortColumn != null && sortOrder != null) {
        	jpql += " ORDER BY e." + sortColumn + " " + sortOrder.asJPQL();
        }
        
		
        Query query = getEntityManager().createQuery(jpql);
        query.setFirstResult(start);
        
        if(max > 0) {
        	query.setMaxResults(max);
        }
        
        return (List<AnalyticsTaskExecution>) query.getResultList();
    }

	/**
	 * This method returns the count of entities based on the input parameters passed. 
	 * 
     * @param recordType	RecordFecthType
     * @param searchText	Search text
     * @param searchColumn	Search column
	 * @return	Count of records
	 */
    public Long findRecordCount(RecordFecthType recordType,	
    								String searchText, String searchColumn) {
		
		Long currPersonId = getSession().getCurrentPerson().getId();

		
		if (recordType == null) {
			recordType = RecordFecthType.ALL_RECORDS;
		}
		
	
	    String jpql = "SELECT COUNT(e) FROM " + clazz.getName() + " e";
	        	
		if((searchText != null && searchText.length() > 0 ) && (searchColumn != null && searchText.length() > 0)) {
		     	
			if (recordType.equals(RecordFecthType.ALL_RECORDS)) {
				jpql += " WHERE UPPER(e."+searchColumn+") LIKE '%"+ searchText.toUpperCase() +"%' ESCAPE '\\' ";
				
			} else {
				jpql += " WHERE UPPER(e."+searchColumn+") LIKE '%"+ searchText.toUpperCase() +"%' ESCAPE '\\' AND ";
			}
	    } else if (!recordType.equals(RecordFecthType.ALL_RECORDS)){
	    	jpql += " WHERE ";
	    }
		
	
		if (recordType.equals(RecordFecthType.PUBLIC_RECORDS)) {
	    	jpql += " e.analyticsTask.isPublic=true "; 
	    	
	    } else if (recordType.equals(RecordFecthType.USER_RECORDS)) {
	    	jpql += " e.analyticsTask.isPublic=false ";
	    	
	    } else if (recordType.equals(RecordFecthType.MY_RECORDS)){
	    	jpql += " e.analyticsTask.isPublic=false and e.analyticsTask.owner.id= "+currPersonId;
	    	
	    }       
	
	    Query query = getEntityManager().createQuery(jpql);
	    
	    return (Long)query.getSingleResult();
	}

	/**
	 * populates violations data in AlertViolationData - datamodel
	 * 
	 * @param taskExecution
	 * @return
	 */
	public List<AlertViolationData> getAlertViolationsData(IAnalyticsTaskExecution taskExecution){

		List<Metric> listOfMetrics = (List<Metric>) taskExecution.getExecutionMetrics();
		List<AlertViolationData> alertViolationDataList = new ArrayList<AlertViolationData>();
		
		if(listOfMetrics != null && listOfMetrics.size() > 0){
			for(int var1 = 0; var1 < listOfMetrics.size(); var1++){
				if(listOfMetrics.get(var1) instanceof MetricDouble){
					MetricDouble metricNumber = (MetricDouble)listOfMetrics.get(var1);
					for(IViolation violation : listOfMetrics.get(var1).getViolations()){
						
						String startDate = taskExecution.getStartTime().toString();

						//String lowRange = String.valueOf(metricNumber.getLowRange());
						//String midRange = String.valueOf((metricNumber.getMidRange().getMinimum()+metricNumber.getMidRange().getMaximum())/2);
						//String highRange = String.valueOf(metricNumber.getHighRange());

						AlertViolationData violationData = new AlertViolationData();

						violationData.setRuleName(violation.getRuleName());
						violationData.setChartStatistics(String.valueOf(metricNumber.getValue()));
						violationData.setStartDate(startDate);
						//violationData.setUCL(highRange);
						//violationData.setLCL(lowRange);
						//violationData.setPCenter(midRange);

						logger.info("violationData -> "+violationData.getRuleName());
						alertViolationDataList.add(violationData);
					}
				}
			}
		}
		return alertViolationDataList;
	}
	
	
	/**
	 * Loops over the analtyics task executions and deletes the executions cascading to their metrics.
	 */
	public void clearAllHistory() {
	    System.out.println("Calling cleanup on history");
	    List<AnalyticsTaskExecution> executions = listAll();
	    
	    for (AnalyticsTaskExecution execution : executions) {
	        getEntityManager().remove(execution);
	    }
	}

	/**
	 * 
	 * returns charts path linked to FactoryAlert Object
	 * @param factoryAlertObj
	 * @return

	public List<String> getChartsPath(AnalyticsTaskExecutionEntity taskCompletion){
		List<String> imagePaths = new ArrayList<String>();
		MetricCollectionEntity metricCollection = (MetricCollectionEntity) taskCompletion.getExecutionMetrics();	
		for(MetricStaticChart chart : metricCollection.getStaticCharts()) {
			imagePaths.add(chart.getChartPath());
		}
		return imagePaths;
	}
	    */
}
