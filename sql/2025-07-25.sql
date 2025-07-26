ALTER TABLE transportation_job MODIFY COLUMN vehicle_id int NULL;
ALTER TABLE transportation_job MODIFY COLUMN transporter_id int NULL;


update transporter set private_first_name  = first_name where private_first_name is null and first_name is not null;
update transporter set private_last_name  = last_name where private_last_name is null and last_name is not null;
update transporter set private_phone  = phone where private_phone is null and phone is not null;
ALTER TABLE transporter DROP COLUMN last_name;
ALTER TABLE transporter DROP COLUMN first_name;
ALTER TABLE transporter DROP COLUMN phone;
