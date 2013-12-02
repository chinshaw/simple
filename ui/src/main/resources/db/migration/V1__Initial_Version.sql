--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- Name: migrate_linkables(); Type: FUNCTION; Schema: public; Owner: chinshaw
--

CREATE FUNCTION migrate_linkables() RETURNS integer
    LANGUAGE plpgsql
    AS $$

DECLARE r RECORD;
BEGIN
	FOR r IN SELECT * from staticplotwidget_analyticstask LOOP
		INSERT INTO linkabletask (id, analyticstask_id) values ((select sequence_value from openjpa_sequence_table), r.linkabletasks_id);
		INSERT INTO staticplotwidget_linkabletask(staticplotwidget_id, linkabletasks_id) values (r.staticplotwidget_id, (select sequence_value from openjpa_sequence_table));
		UPDATE openjpa_sequence_table set sequence_value = sequence_value + 1 where id = 0;
	END LOOP;
RETURN 1;
END;
$$;


ALTER FUNCTION public.migrate_linkables() OWNER TO chinshaw;

--
-- Name: migrate_rcode(); Type: FUNCTION; Schema: public; Owner: chinshaw
--

CREATE FUNCTION migrate_rcode() RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE 
ROP RECORD;
BEGIN                                                                                                                                                                   
FOR ROP IN SELECT * FROM ranalyticsoperation 
LOOP
RAISE NOTICE '%', rop.id;
update ranalyticsoperation set code = (select rcode.code from rcode, ranalyticsoperation where ranalyticsoperation.rcode_id = rcode.id and ranalyticsoperation.id= rop.id) where id = rop.id;
END LOOP; 
RETURN 0;
END; 
$$;


ALTER FUNCTION public.migrate_rcode() OWNER TO chinshaw;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: alertdefinition; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE alertdefinition (
    id bigint NOT NULL,
    version integer,
    alertstatus boolean,
    description character varying(255),
    ispublic boolean,
    name character varying(255),
    analyticstask_id bigint,
    metric bigint,
    owner_id bigint,
    enablequix boolean
);


ALTER TABLE public.alertdefinition OWNER TO chinshaw;

--
-- Name: alertdefinition_person; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE alertdefinition_person (
    alertdefinition_id bigint,
    subscribers_id bigint
);


ALTER TABLE public.alertdefinition_person OWNER TO chinshaw;

--
-- Name: analyticsoperation_output; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticsoperation_output (
    id bigint NOT NULL,
    version integer,
    description character varying(255),
    name character varying(255),
    outputtype smallint,
    required boolean,
    operation_id bigint,
    outputs_order integer
);


ALTER TABLE public.analyticsoperation_output OWNER TO chinshaw;

--
-- Name: analyticsoperationdataprovider; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticsoperationdataprovider (
    id bigint NOT NULL,
    version integer,
    description character varying(8000),
    variablename character varying(255)
);


ALTER TABLE public.analyticsoperationdataprovider OWNER TO chinshaw;

--
-- Name: analyticstask; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstask (
    id bigint NOT NULL,
    version integer,
    description character varying(255),
    ispublic boolean,
    name character varying(255),
    dtype character varying(255),
    owner_id bigint,
    createddate timestamp without time zone,
    fingerprint character varying(255),
    modifieddate timestamp without time zone,
    lastmodifiedby_id bigint,
    schedule_cron_expression character varying(255),
    schedule_description character varying(255),
    schedule_enabled boolean,
    schedule_name character varying(255),
    template_id bigint
);


ALTER TABLE public.analyticstask OWNER TO chinshaw;

--
-- Name: analyticstask_analyticsoperations; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstask_analyticsoperations (
    analyticstask_id bigint,
    element bigint,
    analyticsoperations_order integer
);


ALTER TABLE public.analyticstask_analyticsoperations OWNER TO chinshaw;

--
-- Name: analyticstask_changelog; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstask_changelog (
    analyticstask_id bigint,
    changelogs_id bigint,
    changelogs_order integer
);


ALTER TABLE public.analyticstask_changelog OWNER TO chinshaw;

--
-- Name: analyticstask_dataproviders; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstask_dataproviders (
    analyticstask_id bigint,
    element bigint,
    dataproviders_order integer
);


ALTER TABLE public.analyticstask_dataproviders OWNER TO chinshaw;

--
-- Name: analyticstask_inputs; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstask_inputs (
    analyticstask_id bigint,
    element bigint,
    inputs_order integer
);


ALTER TABLE public.analyticstask_inputs OWNER TO chinshaw;

--
-- Name: analyticstask_person; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstask_person (
    reporttask_id bigint,
    subscribers_id bigint
);


ALTER TABLE public.analyticstask_person OWNER TO chinshaw;

--
-- Name: analyticstask_taskinputs; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstask_taskinputs (
    analyticstask_id bigint,
    element bigint,
    taskinputs_order integer
);


ALTER TABLE public.analyticstask_taskinputs OWNER TO chinshaw;

--
-- Name: analyticstaskcomplexinput; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstaskcomplexinput (
    id bigint NOT NULL,
    version integer,
    operationinput bigint
);


ALTER TABLE public.analyticstaskcomplexinput OWNER TO chinshaw;

--
-- Name: analyticstaskcomplexinput_value; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstaskcomplexinput_value (
    analyticstaskcomplexinput_id bigint,
    element bigint
);


ALTER TABLE public.analyticstaskcomplexinput_value OWNER TO chinshaw;

--
-- Name: analyticstaskdateinput; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstaskdateinput (
    id bigint NOT NULL,
    version integer,
    value timestamp without time zone,
    operationinput bigint
);


ALTER TABLE public.analyticstaskdateinput OWNER TO chinshaw;

--
-- Name: analyticstaskexecution; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstaskexecution (
    id bigint NOT NULL,
    version integer,
    completionstatus smallint,
    endtime timestamp without time zone,
    starttime timestamp without time zone,
    analyticstask_id bigint,
    executionlogfilename character varying(255)
);


ALTER TABLE public.analyticstaskexecution OWNER TO chinshaw;

--
-- Name: analyticstaskexecution_analyticstaskinputs; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstaskexecution_analyticstaskinputs (
    analyticstaskexecution_id bigint,
    element bigint
);


ALTER TABLE public.analyticstaskexecution_analyticstaskinputs OWNER TO chinshaw;

--
-- Name: analyticstaskexecution_executionmetrics; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstaskexecution_executionmetrics (
    analyticstaskexecution_id bigint,
    element bigint
);


ALTER TABLE public.analyticstaskexecution_executionmetrics OWNER TO chinshaw;

--
-- Name: analyticstaskmonitor; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstaskmonitor (
    id bigint NOT NULL,
    version integer,
    alertstatus boolean,
    description character varying(255),
    ispublic boolean,
    name character varying(255),
    quixenabled boolean,
    analyticstask_id bigint,
    metric bigint,
    owner_id bigint
);


ALTER TABLE public.analyticstaskmonitor OWNER TO chinshaw;

--
-- Name: analyticstaskmonitor_factoryalert; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstaskmonitor_factoryalert (
    analyticstaskmonitor_id bigint,
    factoryalert_id bigint
);


ALTER TABLE public.analyticstaskmonitor_factoryalert OWNER TO chinshaw;

--
-- Name: analyticstaskmonitor_person; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstaskmonitor_person (
    analyticstaskmonitor_id bigint,
    subscribers_id bigint
);


ALTER TABLE public.analyticstaskmonitor_person OWNER TO chinshaw;

--
-- Name: analyticstaskschedule; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstaskschedule (
    id bigint NOT NULL,
    cronexpression character varying(255),
    description character varying(255),
    enabled boolean,
    name character varying(255)
);


ALTER TABLE public.analyticstaskschedule OWNER TO chinshaw;

--
-- Name: analyticstaskstringinput; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstaskstringinput (
    id bigint NOT NULL,
    version integer,
    value character varying(255),
    operationinput bigint
);


ALTER TABLE public.analyticstaskstringinput OWNER TO chinshaw;

--
-- Name: analyticstasktemplate; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstasktemplate (
    id bigint NOT NULL,
    version integer,
    createddate timestamp without time zone,
    description character varying(255),
    fingerprint character varying(255),
    ispublic boolean,
    modifieddate timestamp without time zone,
    name character varying(255),
    lastmodifiedby_id bigint,
    owner_id bigint,
    schedule_cron_expression character varying(255),
    schedule_description character varying(255),
    schedule_enabled boolean,
    schedule_name character varying(255)
);


ALTER TABLE public.analyticstasktemplate OWNER TO chinshaw;

--
-- Name: analyticstasktemplate_analyticsoperations; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstasktemplate_analyticsoperations (
    analyticstasktemplate_id bigint,
    element bigint,
    analyticsoperations_order integer
);


ALTER TABLE public.analyticstasktemplate_analyticsoperations OWNER TO chinshaw;

--
-- Name: analyticstasktemplate_dataproviders; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstasktemplate_dataproviders (
    analyticstask_id bigint,
    element bigint,
    dataproviders_order integer,
    analyticstasktemplate_id bigint
);


ALTER TABLE public.analyticstasktemplate_dataproviders OWNER TO chinshaw;

--
-- Name: analyticstasktemplate_person; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstasktemplate_person (
    reporttask_id bigint,
    subscribers_id bigint
);


ALTER TABLE public.analyticstasktemplate_person OWNER TO chinshaw;

--
-- Name: analyticstasktemplate_taskinputs; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE analyticstasktemplate_taskinputs (
    analyticstasktemplate_id bigint,
    element bigint
);


ALTER TABLE public.analyticstasktemplate_taskinputs OWNER TO chinshaw;

--
-- Name: changelog; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE changelog (
    id bigint NOT NULL,
    changedate timestamp without time zone,
    changemessage character varying(255),
    person_id bigint
);


ALTER TABLE public.changelog OWNER TO chinshaw;

--
-- Name: complexinput; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE complexinput (
    id bigint NOT NULL,
    version integer,
    displayname character varying(255),
    inputname character varying(255),
    required boolean,
    fingerprint character varying(255),
    parent bigint
);


ALTER TABLE public.complexinput OWNER TO chinshaw;

--
-- Name: complexinput_inputs; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE complexinput_inputs (
    complexinput_id bigint,
    element bigint
);


ALTER TABLE public.complexinput_inputs OWNER TO chinshaw;

--
-- Name: dashboard_dashboardwidget; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_dashboardwidget (
    id bigint NOT NULL,
    version integer,
    title character varying(255),
    description character varying(255),
    name character varying(255),
    template_id bigint,
    backgroundcolor character varying(255),
    analyticstask_id bigint
);


ALTER TABLE public.dashboard_dashboardwidget OWNER TO chinshaw;

--
-- Name: dashboard_dashboardwidget_widgets; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_dashboardwidget_widgets (
    dashboardwidget_id bigint,
    element bigint,
    widgets_order integer
);


ALTER TABLE public.dashboard_dashboardwidget_widgets OWNER TO chinshaw;

--
-- Name: dashboard_gaugewidget; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_gaugewidget (
    id bigint NOT NULL,
    version integer,
    title character varying(255),
    linkabletask_id bigint,
    metric bigint,
    metric_id bigint,
    backgroundcolor character varying(255),
    description character varying(255)
);


ALTER TABLE public.dashboard_gaugewidget OWNER TO chinshaw;

--
-- Name: dashboard_gaugewidget_dashboard_linkabletask; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_gaugewidget_dashboard_linkabletask (
    gaugewidget_id bigint,
    linkabletasks_id bigint
);


ALTER TABLE public.dashboard_gaugewidget_dashboard_linkabletask OWNER TO chinshaw;

--
-- Name: dashboard_gaugewidget_ranges; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_gaugewidget_ranges (
    gauge_model_id bigint,
    color character varying(255),
    max double precision,
    min double precision,
    rangename character varying(255),
    range_id bigint
);


ALTER TABLE public.dashboard_gaugewidget_ranges OWNER TO chinshaw;

--
-- Name: dashboard_linkabletask; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_linkabletask (
    id bigint,
    version integer,
    context character varying(255),
    analyticstask_id bigint
);


ALTER TABLE public.dashboard_linkabletask OWNER TO chinshaw;

--
-- Name: dashboard_metricplotwidget; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_metricplotwidget (
    id bigint NOT NULL,
    version integer,
    title character varying(255),
    linkabletask_id bigint,
    metric_id bigint,
    backgroundcolor character varying(255),
    description character varying(255)
);


ALTER TABLE public.dashboard_metricplotwidget OWNER TO chinshaw;

--
-- Name: dashboard_metricplotwidget_dashboard_linkabletask; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_metricplotwidget_dashboard_linkabletask (
    metricplotwidget_id bigint,
    linkabletasks_id bigint
);


ALTER TABLE public.dashboard_metricplotwidget_dashboard_linkabletask OWNER TO chinshaw;

--
-- Name: dashboard_metricplotwidget_linkabletask; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_metricplotwidget_linkabletask (
    metricplotwidget_id bigint,
    linkabletasks_id bigint
);


ALTER TABLE public.dashboard_metricplotwidget_linkabletask OWNER TO chinshaw;

--
-- Name: dashboard_metrictablewidget; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_metrictablewidget (
    id bigint NOT NULL,
    version integer,
    backgroundcolor character varying(255),
    description character varying(255),
    title character varying(255),
    metric_id bigint
);


ALTER TABLE public.dashboard_metrictablewidget OWNER TO chinshaw;

--
-- Name: dashboard_metrictablewidget_dashboard_linkabletask; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_metrictablewidget_dashboard_linkabletask (
    metrictablewidget_id bigint,
    linkabletasks_id bigint
);


ALTER TABLE public.dashboard_metrictablewidget_dashboard_linkabletask OWNER TO chinshaw;

--
-- Name: dashboard_metrictablewidget_headers; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_metrictablewidget_headers (
    header_id bigint,
    element character varying(255),
    headers_order integer
);


ALTER TABLE public.dashboard_metrictablewidget_headers OWNER TO chinshaw;

--
-- Name: dashboard_panelwidget; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_panelwidget (
    id bigint NOT NULL,
    version integer,
    title character varying(255),
    backgroundcolor character varying(255),
    description character varying(255)
);


ALTER TABLE public.dashboard_panelwidget OWNER TO chinshaw;

--
-- Name: dashboard_panelwidget_widgets; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dashboard_panelwidget_widgets (
    panelwidget_id bigint,
    element bigint,
    widgets_order integer
);


ALTER TABLE public.dashboard_panelwidget_widgets OWNER TO chinshaw;

--
-- Name: dateinput; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE dateinput (
    id bigint NOT NULL,
    version integer,
    displayname character varying(255),
    inputname character varying(255),
    required boolean,
    value timestamp without time zone,
    fingerprint character varying(255),
    parent bigint
);


ALTER TABLE public.dateinput OWNER TO chinshaw;

--
-- Name: factory; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE factory (
    id bigint NOT NULL,
    location character varying(255),
    name character varying(255)
);


ALTER TABLE public.factory OWNER TO chinshaw;

--
-- Name: factoryalert; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE factoryalert (
    id bigint NOT NULL,
    alertstate smallint,
    alerttype character varying(255),
    closeddate timestamp without time zone,
    creationdate timestamp without time zone,
    description character varying(255),
    quixid character varying(255),
    resolution character varying(255),
    status character varying(255),
    factory_id bigint,
    monitor_id bigint,
    taskexecution_id bigint
);


ALTER TABLE public.factoryalert OWNER TO chinshaw;

--
-- Name: javaanalyticsoperation; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE javaanalyticsoperation (
    id bigint NOT NULL,
    version integer,
    description character varying(255),
    ispublic boolean,
    name character varying(255),
    executablejar text,
    owner_id bigint,
    modifieddate timestamp without time zone,
    lastmodifiedby bytea,
    fingerprint character varying(255)
);


ALTER TABLE public.javaanalyticsoperation OWNER TO chinshaw;

--
-- Name: javaanalyticsoperation_analyticsoperationdataprovider; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE javaanalyticsoperation_analyticsoperationdataprovider (
    javaanalyticsoperation_id bigint,
    dataproviders_id bigint,
    dataproviders_order integer
);


ALTER TABLE public.javaanalyticsoperation_analyticsoperationdataprovider OWNER TO chinshaw;

--
-- Name: javaanalyticsoperation_changelog; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE javaanalyticsoperation_changelog (
    javaanalyticsoperation_id bigint,
    changelogs_id bigint,
    changelogs_order integer
);


ALTER TABLE public.javaanalyticsoperation_changelog OWNER TO chinshaw;

--
-- Name: javaanalyticsoperation_inputs; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE javaanalyticsoperation_inputs (
    javaanalyticsoperation_id bigint,
    element bigint,
    inputs_order integer
);


ALTER TABLE public.javaanalyticsoperation_inputs OWNER TO chinshaw;

--
-- Name: javaanalyticsoperation_outputs; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE javaanalyticsoperation_outputs (
    javaanalyticsoperation_id bigint,
    element bigint,
    outputs_order integer
);


ALTER TABLE public.javaanalyticsoperation_outputs OWNER TO chinshaw;

--
-- Name: metric_metricdouble; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metric_metricdouble (
    id bigint NOT NULL,
    version integer,
    context character varying(255),
    name character varying(255),
    value double precision,
    parent bigint,
    highrange_id bigint,
    lowrange_id bigint,
    midrange_id bigint,
    totalrange_id bigint,
    chartpath character varying(255)
);


ALTER TABLE public.metric_metricdouble OWNER TO chinshaw;

--
-- Name: metric_metricdouble_violation; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metric_metricdouble_violation (
    metricnumber_id bigint,
    violations_id bigint,
    metricdouble_id bigint
);


ALTER TABLE public.metric_metricdouble_violation OWNER TO chinshaw;

--
-- Name: metric_metricinteger; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metric_metricinteger (
    id bigint NOT NULL,
    version integer,
    context character varying(255),
    name character varying(255),
    value integer
);


ALTER TABLE public.metric_metricinteger OWNER TO chinshaw;

--
-- Name: metric_metricinteger_violation; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metric_metricinteger_violation (
    metricinteger_id bigint,
    violations_id bigint
);


ALTER TABLE public.metric_metricinteger_violation OWNER TO chinshaw;

--
-- Name: metric_metricmatrix; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metric_metricmatrix (
    id bigint NOT NULL,
    version integer,
    context character varying(255),
    name character varying(255),
    serializedtablefile character varying(255)
);


ALTER TABLE public.metric_metricmatrix OWNER TO chinshaw;

--
-- Name: metric_metricmatrix_violation; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metric_metricmatrix_violation (
    metricmatrix_id bigint,
    violations_id bigint
);


ALTER TABLE public.metric_metricmatrix_violation OWNER TO chinshaw;

--
-- Name: metric_metricnumber_ranges; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metric_metricnumber_ranges (
    metricnumber_id bigint,
    ranges_id bigint,
    metricdouble_id bigint,
    metricinteger_id bigint
);


ALTER TABLE public.metric_metricnumber_ranges OWNER TO chinshaw;

--
-- Name: metric_metricplot; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metric_metricplot (
    id bigint NOT NULL,
    version integer,
    context character varying(255),
    name character varying(255),
    ploturl character varying(255),
    parent bigint,
    imageformat character varying(255)
);


ALTER TABLE public.metric_metricplot OWNER TO chinshaw;

--
-- Name: metric_metricplot_violation; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metric_metricplot_violation (
    metricstaticchart_id bigint,
    violations_id bigint,
    metricplot_id bigint
);


ALTER TABLE public.metric_metricplot_violation OWNER TO chinshaw;

--
-- Name: metric_metricstring; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metric_metricstring (
    id bigint NOT NULL,
    version integer,
    context character varying(255),
    name character varying(255),
    value character varying(255),
    parent bigint,
    chartpath character varying(255)
);


ALTER TABLE public.metric_metricstring OWNER TO chinshaw;

--
-- Name: metric_metricstring_violation; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metric_metricstring_violation (
    metricstring_id bigint,
    violations_id bigint
);


ALTER TABLE public.metric_metricstring_violation OWNER TO chinshaw;

--
-- Name: metric_numberrange; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metric_numberrange (
    id bigint NOT NULL,
    max double precision,
    min double precision,
    criticality smallint,
    version integer
);


ALTER TABLE public.metric_numberrange OWNER TO chinshaw;

--
-- Name: metriccollection; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metriccollection (
    id bigint NOT NULL,
    version integer,
    context character varying(255),
    name character varying(255),
    parent bigint,
    chartpath character varying(255)
);


ALTER TABLE public.metriccollection OWNER TO chinshaw;

--
-- Name: metriccollection_metrics; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metriccollection_metrics (
    metriccollection_id bigint,
    element bigint
);


ALTER TABLE public.metriccollection_metrics OWNER TO chinshaw;

--
-- Name: metriccollection_violation; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metriccollection_violation (
    metriccollection_id bigint,
    violations_id bigint
);


ALTER TABLE public.metriccollection_violation OWNER TO chinshaw;

--
-- Name: metricdataframe; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metricdataframe (
    id bigint NOT NULL,
    version integer,
    context character varying(255),
    name character varying(255),
    parent bigint,
    chartpath character varying(255)
);


ALTER TABLE public.metricdataframe OWNER TO chinshaw;

--
-- Name: metricdataframe_metriccollection; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metricdataframe_metriccollection (
    metricdataframe_id bigint,
    value_id bigint
);


ALTER TABLE public.metricdataframe_metriccollection OWNER TO chinshaw;

--
-- Name: metricdataframe_violation; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metricdataframe_violation (
    metricdataframe_id bigint,
    violations_id bigint
);


ALTER TABLE public.metricdataframe_violation OWNER TO chinshaw;

--
-- Name: metricstring_violation; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE metricstring_violation (
    metricstring_id bigint,
    violations_id bigint
);


ALTER TABLE public.metricstring_violation OWNER TO chinshaw;

--
-- Name: openjpa_sequence_table; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE openjpa_sequence_table (
    id smallint NOT NULL,
    sequence_value bigint
);


ALTER TABLE public.openjpa_sequence_table OWNER TO chinshaw;

--
-- Name: person; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE person (
    id bigint NOT NULL,
    version integer,
    email character varying(255),
    name character varying(255),
    tasklimitvalue integer,
    preferences_id bigint,
    cellphoneprovider character varying(255),
    debugenabled boolean,
    defaultplace character varying(255),
    emailflag boolean,
    smsflag boolean,
    subscribercellnumber character varying(255),
    subscribermailid character varying(255)
);


ALTER TABLE public.person OWNER TO chinshaw;

--
-- Name: preferences; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE preferences (
    id bigint NOT NULL,
    version integer,
    cellphoneprovider character varying(255),
    debugenabled boolean,
    defaultplace character varying(255),
    emailflag boolean,
    smsflag boolean,
    subscribercellnumber character varying(255),
    subscribermailid character varying(255)
);


ALTER TABLE public.preferences OWNER TO chinshaw;

--
-- Name: ranalyticsoperation; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE ranalyticsoperation (
    id bigint NOT NULL,
    version integer,
    description character varying(255),
    ispublic boolean,
    name character varying(255),
    code text,
    owner_id bigint,
    modifieddate timestamp without time zone,
    lastmodifiedby bytea,
    fingerprint character varying(255)
);


ALTER TABLE public.ranalyticsoperation OWNER TO chinshaw;

--
-- Name: ranalyticsoperation_analyticsoperationdataprovider; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE ranalyticsoperation_analyticsoperationdataprovider (
    ranalyticsoperation_id bigint,
    dataproviders_id bigint,
    dataproviders_order integer
);


ALTER TABLE public.ranalyticsoperation_analyticsoperationdataprovider OWNER TO chinshaw;

--
-- Name: ranalyticsoperation_changelog; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE ranalyticsoperation_changelog (
    ranalyticsoperation_id bigint,
    changelogs_id bigint,
    changelogs_order integer
);


ALTER TABLE public.ranalyticsoperation_changelog OWNER TO chinshaw;

--
-- Name: ranalyticsoperation_inputs; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE ranalyticsoperation_inputs (
    ranalyticsoperation_id bigint,
    element bigint,
    inputs_order integer
);


ALTER TABLE public.ranalyticsoperation_inputs OWNER TO chinshaw;

--
-- Name: ranalyticsoperation_outputs; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE ranalyticsoperation_outputs (
    ranalyticsoperation_id bigint,
    element bigint,
    outputs_order integer
);


ALTER TABLE public.ranalyticsoperation_outputs OWNER TO chinshaw;

--
-- Name: rdataprovider; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE rdataprovider (
    id bigint NOT NULL,
    version integer,
    variablename character varying(255),
    rcommand character varying(255),
    description character varying(255),
    command character varying(255)
);


ALTER TABLE public.rdataprovider OWNER TO chinshaw;

--
-- Name: sqlconnection; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE sqlconnection (
    id bigint NOT NULL,
    version integer,
    description character varying(255),
    drivername character varying(255),
    host character varying(255),
    name character varying(255),
    password character varying(255),
    username character varying(255)
);


ALTER TABLE public.sqlconnection OWNER TO chinshaw;

--
-- Name: sqldataprovider; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE sqldataprovider (
    id bigint NOT NULL,
    version integer,
    variablename character varying(255),
    sqlstatement text,
    description character varying(255)
);


ALTER TABLE public.sqldataprovider OWNER TO chinshaw;

--
-- Name: sqldataproviderdriver; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE sqldataproviderdriver (
    id bigint NOT NULL,
    version integer,
    drivername character varying(255),
    host character varying(255),
    name character varying(255),
    password character varying(255),
    username character varying(255)
);


ALTER TABLE public.sqldataproviderdriver OWNER TO chinshaw;

--
-- Name: stringinput; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE stringinput (
    id bigint NOT NULL,
    version integer,
    displayname character varying(255),
    inputname character varying(255),
    required boolean,
    value text,
    fingerprint character varying(255),
    parent bigint
);


ALTER TABLE public.stringinput OWNER TO chinshaw;

--
-- Name: stringinput_definedinputs; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE stringinput_definedinputs (
    stringinput_id bigint,
    element character varying(255)
);


ALTER TABLE public.stringinput_definedinputs OWNER TO chinshaw;

--
-- Name: violation; Type: TABLE; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE TABLE violation (
    id bigint NOT NULL,
    version integer,
    detail character varying(255),
    rulename character varying(255),
    severity integer,
    subgroup integer
);


ALTER TABLE public.violation OWNER TO chinshaw;

--
-- Name: alertdefinition_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY alertdefinition
    ADD CONSTRAINT alertdefinition_pkey PRIMARY KEY (id);


--
-- Name: analyticsoperation_output_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY analyticsoperation_output
    ADD CONSTRAINT analyticsoperation_output_pkey PRIMARY KEY (id);


--
-- Name: analyticsoperationdataprovider_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY analyticsoperationdataprovider
    ADD CONSTRAINT analyticsoperationdataprovider_pkey PRIMARY KEY (id);


--
-- Name: analyticstask_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY analyticstask
    ADD CONSTRAINT analyticstask_pkey PRIMARY KEY (id);


--
-- Name: analyticstaskcomplexinput_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY analyticstaskcomplexinput
    ADD CONSTRAINT analyticstaskcomplexinput_pkey PRIMARY KEY (id);


--
-- Name: analyticstaskdateinput_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY analyticstaskdateinput
    ADD CONSTRAINT analyticstaskdateinput_pkey PRIMARY KEY (id);


--
-- Name: analyticstaskexecution_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY analyticstaskexecution
    ADD CONSTRAINT analyticstaskexecution_pkey PRIMARY KEY (id);


--
-- Name: analyticstaskmonitor_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY analyticstaskmonitor
    ADD CONSTRAINT analyticstaskmonitor_pkey PRIMARY KEY (id);


--
-- Name: analyticstaskschedule_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY analyticstaskschedule
    ADD CONSTRAINT analyticstaskschedule_pkey PRIMARY KEY (id);


--
-- Name: analyticstaskstringinput_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY analyticstaskstringinput
    ADD CONSTRAINT analyticstaskstringinput_pkey PRIMARY KEY (id);


--
-- Name: analyticstasktemplate_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY analyticstasktemplate
    ADD CONSTRAINT analyticstasktemplate_pkey PRIMARY KEY (id);


--
-- Name: changelog_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY changelog
    ADD CONSTRAINT changelog_pkey PRIMARY KEY (id);


--
-- Name: dashboard_metrictablewidget_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY dashboard_metrictablewidget
    ADD CONSTRAINT dashboard_metrictablewidget_pkey PRIMARY KEY (id);


--
-- Name: dashboard_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY dashboard_dashboardwidget
    ADD CONSTRAINT dashboard_pkey PRIMARY KEY (id);


--
-- Name: factory_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY factory
    ADD CONSTRAINT factory_pkey PRIMARY KEY (id);


--
-- Name: factoryalert_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY factoryalert
    ADD CONSTRAINT factoryalert_pkey PRIMARY KEY (id);


--
-- Name: gaugewidget_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY dashboard_gaugewidget
    ADD CONSTRAINT gaugewidget_pkey PRIMARY KEY (id);


--
-- Name: javaanalyticsoperation_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY javaanalyticsoperation
    ADD CONSTRAINT javaanalyticsoperation_pkey PRIMARY KEY (id);


--
-- Name: metric_metricinteger_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY metric_metricinteger
    ADD CONSTRAINT metric_metricinteger_pkey PRIMARY KEY (id);


--
-- Name: metric_metricmatrix_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY metric_metricmatrix
    ADD CONSTRAINT metric_metricmatrix_pkey PRIMARY KEY (id);


--
-- Name: metriccollection_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY metriccollection
    ADD CONSTRAINT metriccollection_pkey PRIMARY KEY (id);


--
-- Name: metricdataframe_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY metricdataframe
    ADD CONSTRAINT metricdataframe_pkey PRIMARY KEY (id);


--
-- Name: metricnumber_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY metric_metricdouble
    ADD CONSTRAINT metricnumber_pkey PRIMARY KEY (id);


--
-- Name: metricstaticchart_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY metric_metricplot
    ADD CONSTRAINT metricstaticchart_pkey PRIMARY KEY (id);


--
-- Name: metricstring_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY metric_metricstring
    ADD CONSTRAINT metricstring_pkey PRIMARY KEY (id);


--
-- Name: numberrange_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY metric_numberrange
    ADD CONSTRAINT numberrange_pkey PRIMARY KEY (id);


--
-- Name: openjpa_sequence_table_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY openjpa_sequence_table
    ADD CONSTRAINT openjpa_sequence_table_pkey PRIMARY KEY (id);


--
-- Name: panel_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY dashboard_panelwidget
    ADD CONSTRAINT panel_pkey PRIMARY KEY (id);


--
-- Name: person_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- Name: preferences_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY preferences
    ADD CONSTRAINT preferences_pkey PRIMARY KEY (id);


--
-- Name: ranalyticsoperation_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY ranalyticsoperation
    ADD CONSTRAINT ranalyticsoperation_pkey PRIMARY KEY (id);


--
-- Name: rdataprovider_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY rdataprovider
    ADD CONSTRAINT rdataprovider_pkey PRIMARY KEY (id);


--
-- Name: sqlconnection_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY sqlconnection
    ADD CONSTRAINT sqlconnection_pkey PRIMARY KEY (id);


--
-- Name: sqldataprovider_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY sqldataprovider
    ADD CONSTRAINT sqldataprovider_pkey PRIMARY KEY (id);


--
-- Name: sqldataproviderdriver_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY sqldataproviderdriver
    ADD CONSTRAINT sqldataproviderdriver_pkey PRIMARY KEY (id);


--
-- Name: staticplotwidget_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY dashboard_metricplotwidget
    ADD CONSTRAINT staticplotwidget_pkey PRIMARY KEY (id);


--
-- Name: uicomplexinputmodel_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY complexinput
    ADD CONSTRAINT uicomplexinputmodel_pkey PRIMARY KEY (id);


--
-- Name: uidateinputmodel_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY dateinput
    ADD CONSTRAINT uidateinputmodel_pkey PRIMARY KEY (id);


--
-- Name: uiuserinputmodel_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY stringinput
    ADD CONSTRAINT uiuserinputmodel_pkey PRIMARY KEY (id);


--
-- Name: violation_pkey; Type: CONSTRAINT; Schema: public; Owner: chinshaw; Tablespace: 
--

ALTER TABLE ONLY violation
    ADD CONSTRAINT violation_pkey PRIMARY KEY (id);


--
-- Name: i_chnglog_person; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_chnglog_person ON changelog USING btree (person_id);


--
-- Name: i_cmplpts_uicomplexinputmodel_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_cmplpts_uicomplexinputmodel_id ON complexinput_inputs USING btree (complexinput_id);


--
-- Name: i_dshbgts_dashboard_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_dshbgts_dashboard_id ON dashboard_dashboardwidget_widgets USING btree (dashboardwidget_id);


--
-- Name: i_dshbord_analyticstask; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_dshbord_analyticstask ON dashboard_dashboardwidget USING btree (template_id);


--
-- Name: i_ggmdngs_gauge_model_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_ggmdngs_gauge_model_id ON dashboard_gaugewidget_ranges USING btree (gauge_model_id);


--
-- Name: i_gugwdgt_linkabletask; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_gugwdgt_linkabletask ON dashboard_gaugewidget USING btree (linkabletask_id);


--
-- Name: i_gugwdgt_metric; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_gugwdgt_metric ON dashboard_gaugewidget USING btree (metric_id);


--
-- Name: i_jvnlglg_element; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_jvnlglg_element ON javaanalyticsoperation_changelog USING btree (changelogs_id);


--
-- Name: i_jvnlglg_javaanalyticsoperation_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_jvnlglg_javaanalyticsoperation_id ON javaanalyticsoperation_changelog USING btree (javaanalyticsoperation_id);


--
-- Name: i_jvnlpts_javaanalyticsoperation_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_jvnlpts_javaanalyticsoperation_id ON javaanalyticsoperation_inputs USING btree (javaanalyticsoperation_id);


--
-- Name: i_jvnlpts_javaanalyticsoperation_id1; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_jvnlpts_javaanalyticsoperation_id1 ON javaanalyticsoperation_outputs USING btree (javaanalyticsoperation_id);


--
-- Name: i_jvnlrtn_owner; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_jvnlrtn_owner ON javaanalyticsoperation USING btree (owner_id);


--
-- Name: i_jvnlvdr_element; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_jvnlvdr_element ON javaanalyticsoperation_analyticsoperationdataprovider USING btree (dataproviders_id);


--
-- Name: i_jvnlvdr_javaanalyticsoperation_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_jvnlvdr_javaanalyticsoperation_id ON javaanalyticsoperation_analyticsoperationdataprovider USING btree (javaanalyticsoperation_id);


--
-- Name: i_lrtdntn_analyticstask; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_lrtdntn_analyticstask ON alertdefinition USING btree (analyticstask_id);


--
-- Name: i_lrtdntn_owner; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_lrtdntn_owner ON alertdefinition USING btree (owner_id);


--
-- Name: i_lrtdrsn_alertdefinition_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_lrtdrsn_alertdefinition_id ON alertdefinition_person USING btree (alertdefinition_id);


--
-- Name: i_lrtdrsn_element; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_lrtdrsn_element ON alertdefinition_person USING btree (subscribers_id);


--
-- Name: i_mtrcctn_element; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcctn_element ON metricdataframe_metriccollection USING btree (value_id);


--
-- Name: i_mtrcctn_metricdataframe_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcctn_metricdataframe_id ON metricdataframe_metriccollection USING btree (metricdataframe_id);


--
-- Name: i_mtrcltn_element; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcltn_element ON metric_metricplot_violation USING btree (violations_id);


--
-- Name: i_mtrcltn_element1; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcltn_element1 ON metriccollection_violation USING btree (violations_id);


--
-- Name: i_mtrcltn_element2; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcltn_element2 ON metricstring_violation USING btree (violations_id);


--
-- Name: i_mtrcltn_element3; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcltn_element3 ON metricdataframe_violation USING btree (violations_id);


--
-- Name: i_mtrcltn_element4; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcltn_element4 ON metric_metricdouble_violation USING btree (violations_id);


--
-- Name: i_mtrcltn_metriccollection_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcltn_metriccollection_id ON metriccollection_violation USING btree (metriccollection_id);


--
-- Name: i_mtrcltn_metricdataframe_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcltn_metricdataframe_id ON metricdataframe_violation USING btree (metricdataframe_id);


--
-- Name: i_mtrcltn_metricnumber_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcltn_metricnumber_id ON metric_metricdouble_violation USING btree (metricnumber_id);


--
-- Name: i_mtrcltn_metricstaticchart_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcltn_metricstaticchart_id ON metric_metricplot_violation USING btree (metricstaticchart_id);


--
-- Name: i_mtrcltn_metricstring_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcltn_metricstring_id ON metricstring_violation USING btree (metricstring_id);


--
-- Name: i_mtrcmbr_highrange; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcmbr_highrange ON metric_metricdouble USING btree (highrange_id);


--
-- Name: i_mtrcmbr_lowrange; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcmbr_lowrange ON metric_metricdouble USING btree (lowrange_id);


--
-- Name: i_mtrcmbr_midrange; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcmbr_midrange ON metric_metricdouble USING btree (midrange_id);


--
-- Name: i_mtrcmbr_totalrange; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcmbr_totalrange ON metric_metricdouble USING btree (totalrange_id);


--
-- Name: i_mtrcrcs_metriccollection_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcrcs_metriccollection_id ON metriccollection_metrics USING btree (metriccollection_id);


--
-- Name: i_mtrcrng_element; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcrng_element ON metric_metricnumber_ranges USING btree (ranges_id);


--
-- Name: i_mtrcrng_metricnumber_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_mtrcrng_metricnumber_id ON metric_metricnumber_ranges USING btree (metricnumber_id);


--
-- Name: i_nlyt_vl_analyticstaskcomplexinput_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlyt_vl_analyticstaskcomplexinput_id ON analyticstaskcomplexinput_value USING btree (analyticstaskcomplexinput_id);


--
-- Name: i_nlytdrs_analyticstask_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlytdrs_analyticstask_id ON analyticstasktemplate_dataproviders USING btree (analyticstask_id);


--
-- Name: i_nlytglg_analyticstask_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlytglg_analyticstask_id ON analyticstask_changelog USING btree (analyticstask_id);


--
-- Name: i_nlytglg_element; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlytglg_element ON analyticstask_changelog USING btree (changelogs_id);


--
-- Name: i_nlytplt_lastmodifiedby; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlytplt_lastmodifiedby ON analyticstasktemplate USING btree (lastmodifiedby_id);


--
-- Name: i_nlytplt_owner; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlytplt_owner ON analyticstasktemplate USING btree (owner_id);


--
-- Name: i_nlytpts_analyticstaskexecution_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlytpts_analyticstaskexecution_id ON analyticstaskexecution_analyticstaskinputs USING btree (analyticstaskexecution_id);


--
-- Name: i_nlytpts_analyticstasktemplate_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlytpts_analyticstasktemplate_id ON analyticstasktemplate_taskinputs USING btree (analyticstasktemplate_id);


--
-- Name: i_nlytrsn_element; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlytrsn_element ON analyticstasktemplate_person USING btree (subscribers_id);


--
-- Name: i_nlytrsn_reporttask_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlytrsn_reporttask_id ON analyticstasktemplate_person USING btree (reporttask_id);


--
-- Name: i_nlyttns_analyticstask_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlyttns_analyticstask_id ON analyticstasktemplate_analyticsoperations USING btree (analyticstasktemplate_id);


--
-- Name: i_nlyttsk_dtype; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlyttsk_dtype ON analyticstask USING btree (dtype);


--
-- Name: i_nlyttsk_owner; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_nlyttsk_owner ON analyticstask USING btree (owner_id);


--
-- Name: i_person_email; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_person_email ON person USING btree (email);


--
-- Name: i_person_preferences; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_person_preferences ON person USING btree (preferences_id);


--
-- Name: i_pnl_gts_panel_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_pnl_gts_panel_id ON dashboard_panelwidget_widgets USING btree (panelwidget_id);


--
-- Name: i_rnlyglg_element; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_rnlyglg_element ON ranalyticsoperation_changelog USING btree (changelogs_id);


--
-- Name: i_rnlyglg_ranalyticsoperation_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_rnlyglg_ranalyticsoperation_id ON ranalyticsoperation_changelog USING btree (ranalyticsoperation_id);


--
-- Name: i_rnlypts_ranalyticsoperation_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_rnlypts_ranalyticsoperation_id ON ranalyticsoperation_inputs USING btree (ranalyticsoperation_id);


--
-- Name: i_rnlypts_ranalyticsoperation_id1; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_rnlypts_ranalyticsoperation_id1 ON ranalyticsoperation_outputs USING btree (ranalyticsoperation_id);


--
-- Name: i_rnlyrtn_owner; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_rnlyrtn_owner ON ranalyticsoperation USING btree (owner_id);


--
-- Name: i_rnlyvdr_element; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_rnlyvdr_element ON ranalyticsoperation_analyticsoperationdataprovider USING btree (dataproviders_id);


--
-- Name: i_rnlyvdr_ranalyticsoperation_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_rnlyvdr_ranalyticsoperation_id ON ranalyticsoperation_analyticsoperationdataprovider USING btree (ranalyticsoperation_id);


--
-- Name: i_srnppts_uiuserinputmodel_id; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_srnppts_uiuserinputmodel_id ON stringinput_definedinputs USING btree (stringinput_id);


--
-- Name: i_sttcdgt_linkabletask; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_sttcdgt_linkabletask ON dashboard_metricplotwidget USING btree (linkabletask_id);


--
-- Name: i_sttcdgt_metric; Type: INDEX; Schema: public; Owner: chinshaw; Tablespace: 
--

CREATE INDEX i_sttcdgt_metric ON dashboard_metricplotwidget USING btree (metric_id);


--
-- Name: public; Type: ACL; Schema: -; Owner: chinshaw
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM chinshaw;
GRANT ALL ON SCHEMA public TO chinshaw;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

