UPDATE 
	transportation_job tj
set
user8_username = (
	select
		max(tr.user7_username)
	from
		transportation_request tr
	where
		tr.transportation_job_id = tj.id and tr.delivery_date = (select max(tr2.delivery_date) from transportation_request tr2 where tr2.transportation_job_id = tj.id))
where
	status = 'ACKNOWLEDGED' and user8_username is null;