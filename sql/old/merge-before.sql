alter table il_delegation_detail rename assignment_detail;
ALTER TABLE assignment_detail change delegation_id assignment_id int(11); 
alter table il_delegation rename assignment;
ALTER TABLE assignment change delegate_username user_username varchar(255);
ALTER TABLE assignment change delegator_username assignator_username varchar(255);




