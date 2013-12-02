

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

