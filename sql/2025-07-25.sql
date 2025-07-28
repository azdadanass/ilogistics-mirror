ALTER TABLE transportation_job MODIFY COLUMN vehicle_id int NULL;
ALTER TABLE transportation_job MODIFY COLUMN transporter_id int NULL;


update transporter set private_first_name  = first_name where private_first_name is null and first_name is not null;
update transporter set private_last_name  = last_name where private_last_name is null and last_name is not null;
update transporter set private_phone  = phone where private_phone is null and phone is not null;
ALTER TABLE transporter DROP COLUMN last_name;
ALTER TABLE transporter DROP COLUMN first_name;
ALTER TABLE transporter DROP COLUMN phone;




update transportation_job a set user1_username  = (select user_username from transportation_job_history tjh where tjh.parent_id = a.id and tjh.status = 'Created') ;
update transportation_job a set date1   = (select date from transportation_job_history tjh where tjh.parent_id = a.id and tjh.status = 'Created') ;
update transportation_job a set user2_username  = user1_username;
update transportation_job a set date2   = date1;
update transportation_job a set user3_username  = user1_username;
update transportation_job a set date3   = date1;
update transportation_job a set user4_username  = driver_username;
update transportation_job a set date4   = date1 ;
update transportation_job a set user5_username  = driver_username;
update transportation_job a set date5   = date1;

