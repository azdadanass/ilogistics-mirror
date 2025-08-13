update transportation_job tj set status = 'ACKNOWLEDGED' where status  = 'CLOSED';

update transportation_request a set a.planned_pickup_date  = a.expected_pickup_date  where a.planned_pickup_date  is null;
update transportation_request a set a.planned_delivery_date  = a.expected_delivery_date  where a.planned_delivery_date  is null;

update vehicle v set active  = true where active  is null;


update transportation_request a,transportation_job b set a.transporter_id = b.transporter_id where a.transportation_job_id  = b.id and a.transporter_id is null and b.transporter_id is not null;
update transportation_request a,transportation_job b set a.driver_username = b.driver_username where a.transportation_job_id  = b.id and a.driver_username is null and b.driver_username is not null;



update transportation_request_history set status  = 'Delivered' where status  = 'Delivred';
update transportation_request_history a set description = (select concat('<span class="green">Picked up Date : </span><b>',b.pickup_date,'</b>')   from transportation_request b where b.id = a.parent_id)  where status  = 'Picked up' ;
update transportation_request_history a set description = (select concat('<span class="green">Delivery Date : </span><b>',b.delivery_date,'</b>')   from transportation_request b where b.id = a.parent_id)  where status  = 'Delivered' ;