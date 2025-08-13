update transportation_request a set a.planned_pickup_date  = a.expected_pickup_date  where a.planned_pickup_date  is null;
update transportation_request a set a.planned_delivery_date  = a.expected_delivery_date  where a.planned_delivery_date  is null;

update vehicle v set active  = true where active  is null;


update transportation_request a,transportation_job b set a.transporter_id = b.transporter_id where a.transportation_job_id  = b.id and a.transporter_id is null and b.transporter_id is not null;
update transportation_request a,transportation_job b set a.driver_username = b.driver_username where a.transportation_job_id  = b.id and a.driver_username is null and b.driver_username is not null;