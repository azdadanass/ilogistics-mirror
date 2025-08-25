update vehicle_type set image = 'files/no-image.png' where image is null;
update vehicle_type set gross_weight = 0 where gross_weight is null;
update vehicle_type set max_volume = 0 where max_volume is null;

update transportation_job a set first_latitude  = (select COALESCE ((select b.latitude from site b where b.id = s.site_id),(select b.latitude from il_warehouse b where b.id = s.warehouse_id))  from stop s where s.transportation_job_id  = a.id and s.id = (select min(id) from stop s2 where s2.transportation_job_id = a.id));
update transportation_job a set first_longitude= (select COALESCE ((select b.longitude from site b where b.id = s.site_id),(select b.longitude from il_warehouse b where b.id = s.warehouse_id))  from stop s where s.transportation_job_id  = a.id and s.id = (select min(id) from stop s2 where s2.transportation_job_id = a.id));