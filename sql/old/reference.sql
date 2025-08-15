update delivery_request set reference = concat('DN',CAST(`type`+1 as CHAR),CAST(LPAD(reference_number,5,"0") as CHAR));

update transportation_request a set reference = concat('TR',CAST((select b.type from delivery_request b where b.id = a.delivery_request_id)+1 as CHAR),
CAST(LPAD((select b.reference_number from delivery_request b where b.id = a.delivery_request_id),5,"0") as CHAR));