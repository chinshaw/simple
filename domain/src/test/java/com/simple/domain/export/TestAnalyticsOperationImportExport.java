package com.simple.domain.export;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.simple.domain.export.EntityExporter;
import com.simple.domain.export.ExportModel;
import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.AnalyticsTask;
import com.simple.domain.model.ui.dashboard.Dashboard;

public class TestAnalyticsOperationImportExport {

    // This needs to use the test entity manager
    private static final EntityManagerFactory productionEntityManagerFactory = Persistence.createEntityManagerFactory("openjpa-pu");
    private static final EntityManagerFactory testEntityManagerFactory = Persistence.createEntityManagerFactory("openjpa-test-pu");

    public void testAnalyticsOperationExportImport() throws JsonGenerationException, JsonMappingException, IOException {
        EntityExporter exporter = new EntityExporter(productionEntityManagerFactory);

        String json = exporter.entityToJson(AnalyticsOperation.class, 100);
        FileUtils.writeStringToFile(new File("/tmp/testAnalyticsOperationExportImport-json"), json);

        List<AnalyticsOperation> operations = exporter.readEntitiesFromJson(json);

        String json2 = exporter.exportEntity(operations);
        FileUtils.writeStringToFile(new File("/tmp/testAnalyticsOperationExportImport-json2"), json2);

        Assert.assertTrue("Json strings don't equal after marshalling", json.equals(json2));
    }

    public void testAnalyticsTaskExportImport() throws JsonGenerationException, JsonMappingException, IOException {
        EntityExporter exporter = new EntityExporter(productionEntityManagerFactory);

        String json = exporter.entityToJson(AnalyticsTask.class, 100);
        FileUtils.writeStringToFile(new File("/tmp/testAnalyticsTaskExportImport-json"), json);

        List<AnalyticsTask> operations = exporter.readEntitiesFromJson(json);

        String json2 = exporter.exportEntity(operations);
        FileUtils.writeStringToFile(new File("/tmp/testAnalyticsTaskExportImport-json2"), json2);

        Assert.assertTrue("Json strings don't equal after marshalling", json.equals(json2));
    }

    public void testDashboardExportImport() throws JsonGenerationException, JsonMappingException, IOException {
        EntityExporter exporter = new EntityExporter(productionEntityManagerFactory);

        String json = exporter.entityToJson(Dashboard.class, 100);
        FileUtils.writeStringToFile(new File("/tmp/testDashboardExportImport-json"), json);

        List<Dashboard> operations = exporter.readEntitiesFromJson(json);

        String json2 = exporter.exportEntity(operations);
        FileUtils.writeStringToFile(new File("/tmp/testDashboardExportImport-json2"), json2);

        Assert.assertTrue("Json strings don't equal after marshalling", json.equals(json2));
    }

    public void testExportModel() throws IOException {
        EntityExporter exporter = new EntityExporter(productionEntityManagerFactory);

        File modelFile = new File("/tmp/exported-model-test1.json");
        try {
            exporter.exportModel(modelFile, 677l);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to marshal json to model ");
        }

        File modelFile2 = new File("/tmp/exported-model-test2.json");
        try {

            ExportModel model = exporter.readEntitiesFromJson(modelFile, ExportModel.class);
            exporter.exportEntity(modelFile2, model);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to reserialize object back to json");
        }

        // Assert.assertTrue("Json strings don't equal after marshalling",
        // json.equals(json2));
    }

    public void testUpdateModel() throws IOException {
        EntityExporter exporter = new EntityExporter(testEntityManagerFactory);

        EntityManager em = testEntityManagerFactory.createEntityManager();

        File modelFile2 = new File("/tmp/exported-model-test1.json");
        try {

            ExportModel model = exporter.readEntitiesFromJson(modelFile2, ExportModel.class);

            Collection<Dashboard> dashboards = model.getDashbaords();

            
            for (Dashboard dashboard : dashboards) {

                try {
                    em.getTransaction().begin();
                    em.merge(dashboard);
                    em.getTransaction().commit();
                } catch (ConstraintViolationException e) {
                    for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                        System.out.println("Constraint violation " + violation.getMessage());
                    }
                }
                // em.getTransaction();
                System.out.println("Dashboard name is " + dashboard.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to reserialize object back to json");
        }

    }
}
