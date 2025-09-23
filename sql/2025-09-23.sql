update
	transportation_job a
set
	priority = (
	select
		max(case when (dr.priority is null or dr.priority = 'MINOR') then 0 else (case when dr.priority='MEDIUM' then 1 else (case when dr.priority='MEDIUM' then 2 else 3 end) end) end )
	from
		transportation_request tr,delivery_request dr
	where
		dr.id = tr.delivery_request_id and
		tr.transportation_job_id = a.id) 
where
	priority is null
	and status in ('STARTED', 'IN_PROGRESS', 'COMPLETED', 'ACKNOWLEDGED');
	
update transportation_job tj  set tj.priority = "MINOR" where tj.priority = "0";
update transportation_job tj  set tj.priority = "MEDIUM" where tj.priority = "1";
update transportation_job tj  set tj.priority = "HIGH" where tj.priority = "2";
update transportation_job tj  set tj.priority = "CRITICAL" where tj.priority = "3";
	


update transportation_job  set planned_start_date  = start_date  where  planned_start_date is null and status in ('STARTED','IN_PROGRESS','COMPLETED','ACKNOWLEDGED');
update transportation_job  set planned_end_date  = end_date  where  planned_end_date is null and status in ('STARTED','IN_PROGRESS','COMPLETED','ACKNOWLEDGED');

update transportation_job a  set ref = concat((select left(u.firstname,1) from users u where u.username = a.driver_username),(select left(u.lastname,1) from users u where u.username = a.driver_username),'_', DATE_FORMAT(start_date , '%y%m%d'))  where  `ref`  is null and status in ('STARTED','IN_PROGRESS','COMPLETED','ACKNOWLEDGED');*

