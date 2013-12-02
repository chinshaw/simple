/*
 Migrate the metric double classes

 Possible metric types from the operation
 public enum AnalyticsOperationOutputType {

    AUTO, 0
    GRAPHIC, 1
    TEXT, 2
    NUMERIC, 3
    TWO_DIMENSIONAL, 4
    BINARY 5
}
*/

/*
 Migrate the ranalyticsoperation_outputs
*/

-- Add our funciton for getting the next entity id from the jpa table
CREATE OR REPLACE FUNCTION next_entity_id() RETURNS integer
    LANGUAGE plpgsql
    AS $$
	DECLARE next_entity_id integer;
    	BEGIN
		SELECT INTO next_entity_id sequence_value from openjpa_sequence_table;
		UPDATE openjpa_sequence_table SET sequence_value = sequence_value + 1 where id = 0;
	RETURN next_entity_id;
END;
$$;



-- First clean up old garbage tables
DROP TABLE IF EXISTS "MetricMatrix$DoubleColumn";
DROP TABLE IF EXISTS "MetricMatrix$DoubleColumn_rows";
DROP TABLE IF EXISTS "MetricMatrix$IntegerColumn";
DROP TABLE IF EXISTS "MetricMatrix$IntegerColumn_rows";
DROP TABLE IF EXISTS "MetricMatrix$StringColumn";
DROP TABLE IF EXISTS "MetricMatrix$StringColumn_rows";
DROP TABLE IF EXISTS alertdefinition;
DROP TABLE IF EXISTS alertdefinition_person;
DROP TABLE IF EXISTS analyticsoperationdataprovider;
DROP TABLE IF EXISTS appconfigurations;
DROP TABLE IF EXISTS builtinanalyticstask;
DROP TABLE IF EXISTS metricdouble_violation;
DROP TABLE IF EXISTS gaugemodel_ranges;
DROP TABLE IF EXISTS dashboard_metricplotwidget_linkabletask;
DROP TABLE IF EXISTS javaanalyticsoperation_analyticsoperationdataprovider;
DROP TABLE IF EXISTS javaanalyticsoperation_inputs;
DROP TABLE IF EXISTS javaanalyticsoperation_outputs;
DROP TABLE IF EXISTS metric_metricmatrix_columns;
DROP TABLE IF EXISTS metrictable;
DROP TABLE IF EXISTS metrictable_linkabletask;
DROP TABLE IF EXISTS preferences;
DROP TABLE IF EXISTS sqldataproviderdriver;
DROP TABLE IF EXISTS ranalyticsoperation_analyticsoperationdataprovider;




-- AlertMonitor table changes
DROP TABLE IF EXISTS analyticstaskmonitor_factoryalert;
DROP TABLE IF EXISTS analyticstaskmonitor_subscribers;
ALTER TABLE analyticstaskmonitor_person RENAME TO analyticstaskmonitor_subscribers;



-- Remove the analyticstask table if it exists and rename the analtics task template table.
DROP TABLE IF EXISTS analyticstask;
ALTER TABLE analyticstasktemplate RENAME TO analyticstask;

-- Rename the templtates operations table
DROP TABLE IF EXISTS analyticstask_analyticsoperation; -- Old left over table with no data
DROP TABLE IF EXISTS analyticstask_analyticsoperations;
ALTER TABLE analyticstasktemplate_analyticsoperations RENAME TO analyticstask_analyticsoperations;
ALTER TABLE analyticstask_analyticsoperations rename column analyticstasktemplate_id to fk_analyticstask_id;
ALTER TABLE analyticstask_analyticsoperations rename column element to fk_analyticsoperations_id;

-- Rename the templtes person table
DROP TABLE IF EXISTS  analyticstask_person;
ALTER TABLE analyticstasktemplate_person RENAME TO  analyticstask_person;

-- Rename the templates inputs table
DROP TABLE IF EXISTS analyticstask_taskinputs;
DROP TABLE IF EXISTS analyticstask_inputs; 
ALTER TABLE analyticstasktemplate_taskinputs RENAME TO analyticstask_taskinputs; 

-- Rename the dataproviders template table
DROP TABLE IF EXISTS analyticstask_dataproviders;
ALTER TABLE analyticstasktemplate_dataproviders RENAME TO analyticstask_dataproviders;


-- Rename the outputs for the analytics operation table.
ALTER TABLE dashboard_gaugewidget RENAME COLUMN metric_id to analyticsoperationoutput_id;
ALTER TABLE dashboard_gaugewidget DROP COLUMN IF EXISTS metric;


-- Rename the outputs for the analytics operation table.
ALTER TABLE dashboard_metrictablewidget RENAME COLUMN metric_id to analyticsoperationoutput_id;
ALTER TABLE dashboard_metrictablewidget DROP COLUMN IF EXISTS metric;


-- Rename the outputs for the analytics operation table.
ALTER TABLE dashboard_metricplotwidget RENAME COLUMN metric_id to analyticsoperationoutput_id;
ALTER TABLE dashboard_metricplotwidget DROP COLUMN IF EXISTS metric;



-- Rename the dashboard_dashboardwidget template_id table to be analyticstask_id after removing
-- the template object
ALTER TABLE dashboard_dashboardwidget DROP COLUMN IF EXISTS analyticstask_id;
ALTER TABLE dashboard_dashboardwidget rename column template_id to analyticstask_id;

ALTER TABLE dashboard_dashboardwidget_widgets DROP COLUMN IF EXISTS  dashboard_id ;
ALTER TABLE dashboard_dashboardwidget_widgets RENAME COLUMN dashboardwidget_id TO dashboard_id;


ALTER TABLE metriccollection RENAME TO metric_metriccollection;
ALTER TABLE metriccollection_metrics RENAME TO metric_metriccollection_metrics;
ALTER TABLE metriccollection_violation RENAME TO metric_metriccollection_violation;



-- AnalyticsOperation Inputs table
ALTER TABLE ranalyticsoperation_inputs RENAME TO analyticsoperation_inputs;
ALTER TABLE analyticsoperation_inputs RENAME COLUMN ranalyticsoperation_id TO fk_analyticsoperation_id;
ALTER TABLE analyticsoperation_inputs RENAME COLUMN element TO fk_analyticsoperation_input_id;

-- Create the analyticsoperationoutput table first

DROP TABLE IF EXISTS analyticsoperationoutput;
CREATE TABLE analyticsoperationoutput (
    id bigint NOT NULL,
    version integer,
    description character varying(255),
    name character varying(255),
    outputtype smallint,
    required boolean,
    operation_id bigint,
    outputs_order integer
);

--
-- Name: analyticsoperation_outputs; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace:
--

CREATE TABLE analyticsoperation_outputs (
    fk_analyticsoperation_id bigint,
    fk_analyticsoperation_output_id bigint,
    outputs_order integer
);


--
-- Name: changelog; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace:
--
DROP TABLE IF EXISTS changelog;
CREATE TABLE changelog (
    id bigint NOT NULL,
    changedate timestamp without time zone,
    changemessage character varying(255),
    person_id bigint
);

CREATE TABLE analyticstask_changelogs (
    fk_analyticstask_id bigint,
    fk_changelog_id bigint,
    changelogs_order integer
);

--
-- Name: analyticsoperation_changelogs; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace:
--

CREATE TABLE analyticsoperation_changelogs (
    fk_analyticsoperation_id bigint,
    fk_changelog_id bigint,
    changelogs_order integer
);



--
-- Name: Person_Preferences$Bookmark; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace:
--

CREATE TABLE "Person_Preferences$Bookmark" (
    preferences_id bigint,
    bookmarks_id bigint
);


--
-- Name: Preferences$Bookmark; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace:
--

CREATE TABLE "Preferences$Bookmark" (
    id bigint NOT NULL,
    name character varying(255),
    placetoken character varying(255)
);


/*
Convert the MetricPlot class


    INSERT INTO analyticsoperationoutput (id, version, description, name, outputtype, required, operation_id, outputs_order)  (select next_entity_id(), 0, '', md.name, 1, false, op.id, out.outputs_order FROM ranalyticsoperation as op, metric_metricplot as md, ranalyticsoperation_outputs as out where op.id = out.ranalyticsoperation_id and out.element = md.id);
*/

CREATE OR REPLACE FUNCTION migrate_metric() RETURNS void AS $$
DECLARE
        next_entity_id BIGINT;
        metric record;

BEGIN
        FOR metric IN
                SELECT md.id as id, md.name as name, op.id as opid, out.outputs_order as order FROM ranalyticsoperation as op, metric_metricplot as md, ranalyticsoperation_outputs as out where op.id = out.ranalyticsoperation_id and out.element = md.id
        LOOP
                next_entity_id := next_entity_id();
                INSERT INTO analyticsoperationoutput (id, version, description, name, outputtype, required, operation_id, outputs_order) values (next_entity_id, 0, '', metric.name, 1, false, metric.opid, metric.order);
                INSERT INTO analyticsoperation_outputs (fk_analyticsoperation_id, fk_analyticsoperation_output_id, outputs_order) values (metric.opid, next_entity_id, metric.order); 
                UPDATE dashboard_metricplotwidget SET analyticsoperationoutput_id = next_entity_id where analyticsoperationoutput_id = metric.id;

                IF FOUND THEN
                        RAISE NOTICE 'Updated analyticsoperationoutput_id for metric  % to output id %', metric.id, next_entity_id;
                END IF;
        END LOOP;

--              RAISE EXCEPTION 'No records updated';  

END;
$$ LANGUAGE plpgsql;

SELECT migrate_metric();



-- Convert the MetricString class
	INSERT INTO analyticsoperationoutput (id, version, description, name, outputtype, required, operation_id, outputs_order)  (select next_entity_id(), 0, '', md.name, 2, false, op.id, out.outputs_order FROM ranalyticsoperation as op, metric_metricstring as md, ranalyticsoperation_outputs as out where op.id = out.ranalyticsoperation_id and out.element = md.id);


/*

Convert MetricDouble class


	INSERT INTO analyticsoperationoutput (id, version, description, name, outputtype, required, operation_id, outputs_order)  (select next_entity_id(), 0, '', md.name, 3, false, op.id, out.outputs_order FROM ranalyticsoperation as op, metric_metricdouble as md, ranalyticsoperation_outputs as out where op.id = out.ranalyticsoperation_id and out.element = md.id);
*/


CREATE OR REPLACE FUNCTION migrate_metric() RETURNS void AS $$
DECLARE
        next_entity_id BIGINT;
        metric record;

BEGIN
        FOR metric IN
                 SELECT md.id as id, md.name as name, op.id as opid, out.outputs_order as order FROM ranalyticsoperation as op, metric_metricdouble as md, ranalyticsoperation_outputs as out where op.id = out.ranalyticsoperation_id and out.element = md.id
        LOOP
                next_entity_id := next_entity_id();
                INSERT INTO analyticsoperationoutput (id, version, description, name, outputtype, required, operation_id, outputs_order) values (next_entity_id, 0, '', metric.name, 3, false, metric.opid, metric.order);
                INSERT INTO analyticsoperation_outputs (fk_analyticsoperation_id, fk_analyticsoperation_output_id, outputs_order) values (metric.opid, next_entity_id, metric.order); 
                UPDATE dashboard_gaugewidget SET analyticsoperationoutput_id = next_entity_id where analyticsoperationoutput_id = metric.id;

                IF FOUND THEN
                        RAISE NOTICE 'Updated analyticsoperationoutput_id for metric  % to output id %', metric.id, next_entity_id;
                END IF;
        END LOOP;

--              RAISE EXCEPTION 'No records updated';  

END;
$$ LANGUAGE plpgsql;

SELECT migrate_metric();


/* 
    Convert MetricMatrix class


        INSERT INTO analyticsoperationoutput (id, version, description, name, outputtype, required, operation_id, outputs_order)  (select next_entity_id(), 0, '', md.name, 4, false, op.id, out.outputs_order FROM ranalyticsoperation as op, metric_metricmatrix as md, ranalyticsoperation_outputs as out where op.id = out.ranalyticsoperation_id and out.element = md.id);
*/

CREATE OR REPLACE FUNCTION migrate_metric() RETURNS void AS $$
DECLARE
        next_entity_id BIGINT;
        metric record;

BEGIN
        FOR metric IN
                 SELECT md.id as id, md.name as name, op.id as opid, out.outputs_order as order FROM ranalyticsoperation as op, metric_metricmatrix as md, ranalyticsoperation_outputs as out where op.id = out.ranalyticsoperation_id and out.element = md.id
        LOOP
                next_entity_id := next_entity_id();
                INSERT INTO analyticsoperationoutput (id, version, description, name, outputtype, required, operation_id, outputs_order) values (next_entity_id, 0, '', metric.name, 4, false, metric.opid, metric.order);
                INSERT INTO analyticsoperation_outputs (fk_analyticsoperation_id, fk_analyticsoperation_output_id, outputs_order) values (metric.opid, next_entity_id, metric.order); 
                UPDATE dashboard_metrictablewidget SET analyticsoperationoutput_id = next_entity_id where analyticsoperationoutput_id = metric.id;

                IF FOUND THEN
                        RAISE NOTICE 'Updated  analyticsoperationoutput_id for metric  % to output id %', metric.id, next_entity_id;
                END IF;
        END LOOP;

--              RAISE EXCEPTION 'No records updated';  

END;
$$ LANGUAGE plpgsql;

SELECT migrate_metric();



/*
These are not really necessary because we are going to clean out the old history.
*/
-- Delete MetricPlot from outputs
--DELETE FROM metric_metricplot as md USING ranalyticsoperation, ranalyticsoperation_outputs where ranalyticsoperation.id = ranalyticsoperation_outputs.ranalyticsoperation_id and ranalyticsoperation_outputs.element = md.id;

-- Delete MetricString from outputs
--DELETE FROM metric_metricstring as md USING ranalyticsoperation, ranalyticsoperation_outputs where ranalyticsoperation.id = ranalyticsoperation_outputs.ranalyticsoperation_id and ranalyticsoperation_outputs.element = md.id;

-- Delete the MetricDouble outputs
--DELETE FROM metric_metricdouble as md USING ranalyticsoperation, ranalyticsoperation_outputs where ranalyticsoperation.id = ranalyticsoperation_outputs.ranalyticsoperation_id and ranalyticsoperation_outputs.element = md.id;

-- Delete the MetricMatrix outputs
--DELETE FROM metric_metricmatrix as md USING ranalyticsoperation, ranalyticsoperation_outputs where ranalyticsoperation.id = ranalyticsoperation_outputs.ranalyticsoperation_id and ranalyticsoperation_outputs.element = md.id;

-- Delete the outputs table for the RAnalyticsOperation and JavaOperations table
-- DROP TABLE IF EXISTS ranalyticsoperation_outputs;

