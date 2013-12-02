package com.simple.domain.export;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.simple.domain.AnalyticsOperation;
import com.simple.domain.AnalyticsOperationInput;
import com.simple.domain.AnalyticsTask;
import com.simple.domain.DataProvider;
import com.simple.domain.DateInput;
import com.simple.domain.Person;
import com.simple.domain.RAnalyticsOperation;
import com.simple.domain.RequestFactoryEntity;
import com.simple.domain.StringInput;
import com.simple.domain.dashboard.Dashboard;
import com.simple.domain.dashboard.GaugeModelRange;
import com.simple.domain.dashboard.Widget;
import com.simple.domain.metric.Metric;
import com.simple.domain.metric.MetricCollection;
import com.simple.domain.metric.MetricDouble;
import com.simple.domain.metric.MetricNumber;
import com.simple.domain.metric.MetricPlot;
import com.simple.domain.metric.Violation;

public class EntityExporter {

    private static ObjectMapper jsonMapper = new ObjectMapper();

    private EntityManagerFactory entityManagerFactory;

    public EntityExporter(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;

        //SimpleModule simpleModule = new SimpleModule("EntityModule", new Version(1, 0, 0, null));

        //simpleModule.addSerializer(new AnalyticsOperationSerializer());
        //simpleModule.addSerializer(new AnalyticsTaskSerializer());
        //simpleModule.addSerializer(new DashboardSerializer());

        //simpleModule.addDeserializer(AnalyticsTask.class, new AnalyticsTaskDeserializer(jsonMapper));
        //simpleModule.addDeserializer(AnalyticsOperation.class, new AnalyticsOperationDeserializer(jsonMapper));
        //simpleModule.addDeserializer(AnalyticsOperationInput.class, new AnalyticsOperationInputDeserializer(jsonMapper));

        //jsonMapper.registerModule(simpleModule);
        
        jsonMapper.addMixInAnnotations(RequestFactoryEntity.class, RequestFactoryEntityMixin.class);
        jsonMapper.addMixInAnnotations(Dashboard.class, DashboardMixin.class);
        //jsonMapper.addMixInAnnotations(AnalyticsTask.class, AnalyticsTaskMixin.class);
        jsonMapper.addMixInAnnotations(AnalyticsTask.class, AnalyticsTaskMixin.class);
        jsonMapper.addMixInAnnotations(AnalyticsOperation.class, AnalyticsOperationMixin.class);
        jsonMapper.addMixInAnnotations(AnalyticsOperationInput.class, AnalyticsOperationInputMixin.class);
        jsonMapper.addMixInAnnotations(RAnalyticsOperation.class, RAnalyticsOperationMixin.class);
        jsonMapper.addMixInAnnotations(Widget.class, WidgetMixin.class);
        jsonMapper.addMixInAnnotations(Metric.class, MetricMixin.class);
        jsonMapper.addMixInAnnotations(MetricNumber.class, MetricNumberMixin.class);
        jsonMapper.addMixInAnnotations(DataProvider.class, DataProviderMixin.class);
        jsonMapper.addMixInAnnotations(Violation.class, ViolationMixin.class);
        jsonMapper.addMixInAnnotations(MetricPlot.class, MetricPlotMixin.class);
        jsonMapper.addMixInAnnotations(Person.class, PersonMixin.class);
        jsonMapper.addMixInAnnotations(DateInput.class, DateInputMixin.class);
        jsonMapper.addMixInAnnotations(StringInput.class, StringInputMixin.class);
        jsonMapper.addMixInAnnotations(MetricCollection.class, MetricCollectionMixin.class);
        jsonMapper.addMixInAnnotations(GaugeModelRange.class, GaugeRangeMixin.class);
        jsonMapper.addMixInAnnotations(MetricDouble.class, MetricDoubleMixin.class);

       jsonMapper.setVisibilityChecker(jsonMapper.getSerializationConfig().getDefaultVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY)
               .withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY).withSetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY).withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    }

    /**
     * This is used to dump all the entities of a type to a json string and
     * return the results for storing.
     * 
     * @param clazz
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public <T> String entityToJson(Class<T> clazz) throws JsonGenerationException, JsonMappingException, IOException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> q = cb.createQuery(clazz);
        Root<T> c = q.from(clazz);
        q.select(c);

        TypedQuery<T> query = entityManager.createQuery(q);

        List<T> entities = query.getResultList();

        String jsonEntities = null;

        if (entities != null && entities.size() > 0) {
            jsonEntities = exportEntity(entities);
        }

        entityManager.close();

        return jsonEntities;
    }

    /**
     * This is used to dump all the entities of a type to a json string and
     * return the results for storing.
     * 
     * @param clazz
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public <T> String entityToJson(Class<T> clazz, Integer count) throws JsonGenerationException, JsonMappingException, IOException {

        String jsonEntities = null;
        EntityManager entityManager = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            Collection<T> entities = fetchEntities(entityManager, clazz, count);

            if (entities != null && entities.size() > 0) {
                jsonEntities = exportEntity(entities);
            }
        } finally {
            entityManager.close();
        }

        return jsonEntities;
    }

    private <T> Collection<T> fetchEntities(EntityManager entityManager, Class<T> clazz, Integer count) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> q = cb.createQuery(clazz);
        Root<T> c = q.from(clazz);
        q.select(c);

        TypedQuery<T> query = entityManager.createQuery(q);
        
        if (count != null) {
            query.setMaxResults(count);
        }
        
        Collection<T> entities = query.getResultList();

        return entities;

    }

    /**
     * Write the entities out as a json string.
     * 
     * @param value
     * @return
     * @throws JsonProcessingException
     */
    public String exportEntity(Object value) throws JsonProcessingException {
        ObjectWriter writer = jsonMapper.writer().withDefaultPrettyPrinter();
        return writer.writeValueAsString(value);
    }
    
    
    /**
     * Write the entities out as a json string.
     * 
     * @param value
     * @return
     * @throws IOException 
     */
    public void exportEntity(File file, Object value) throws IOException {
        ObjectWriter writer = jsonMapper.writer().withDefaultPrettyPrinter();
        writer.writeValue(file, value);
    }

    public <T> T readEntitiesFromJson(String jsonString) throws JsonGenerationException, JsonMappingException, IOException {
        JsonFactory jfactory = new JsonFactory();
        JsonParser jparser = jfactory.createJsonParser(jsonString);

        T object = jsonMapper.readValue(jparser, new TypeReference<T>() {
        });

        //T object = jsonMapper.readValue(jparser, Type);
        
        return object;
    }
    
    public <T> T readEntitiesFromJson(String jsonString, Class<T> clazz) throws JsonGenerationException, JsonMappingException, IOException {
        JsonFactory jfactory = new JsonFactory();
        JsonParser jparser = jfactory.createJsonParser(jsonString);

        T object = jsonMapper.readValue(jparser, clazz);
        
        return object;
    }

    public <T> T readEntitiesFromJson(File file, Class<T> clazz) throws JsonGenerationException, JsonMappingException, IOException {
        JsonFactory jfactory = new JsonFactory();
        JsonParser jparser = jfactory.createJsonParser(file);

        T object = jsonMapper.readValue(jparser, clazz);
        
        return object;
    }

    
    /**
     * This is mostly used for testing purposes but could be used to serialize
     * an entity.
     * 
     * @return
     */
    public ObjectMapper getObjectMapper() {
        return jsonMapper;
    }

    public void exportModel(File modelFile, Long version) throws IOException {
        // Needs to come from a constants somewhere.
        ExportModel model = new ExportModel(version);
        
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            
            model.setOperations(fetchEntities(em, AnalyticsOperation.class, null));
            model.setTasks(fetchEntities(em, AnalyticsTask.class, null));
            model.setDashbaords(fetchEntities(em, Dashboard.class, null));
            model.setPersons(fetchEntities(em, Person.class, null));
            
            exportEntity(modelFile, model);
        } finally {
            em.close();
        }
    }
}
