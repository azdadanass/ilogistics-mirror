update delivery_request a set to_user_username = to_external_resource_id where a.to_external_resource_id is not null and a.to_user_username  is null;
update to_notify a  set internal_resource_username = external_resource_id where internal_resource_username is null and external_resource_id is not null;
update job_request a set external_assignor_username = external_assignor_id where a.external_assignor_id is not null and a.external_assignor_username  is null;
update job_request a set external_requester_username = a.external_requester_id where a.external_requester_id is not null and a.external_requester_username  is null;
update site a set contact_username = a.contact_id where a.contact_id is not null and a.contact_username  is null;
update team a set user_username = a.external_resource_id where a.external_resource_id is not null and a.user_username  is null;
update team_detail set user_username =external_resource_id where external_resource_id is not null and user_username is null;
update transportation_job set driver_username =driver_id where driver_id is not null and driver_username is null;
update transportation_request set er1_username =er1_id where er1_id is not null and er1_username is null;
update transportation_request set er2_username =er1_id where er2_id is not null and er2_username is null;
update transportation_request set driver_username =driver_id where driver_id is not null and driver_username is null;
update issue  set internal_ownership_resource_username = external_ownership_resource_id where external_ownership_resource_id is not null and internal_ownership_resource_username is null;
update issue set user1_username =external_resource1_id where external_resource1_id is not null and user1_username is null;
update job_request_acceptance set external_assignor_username = external_assignor_id where external_assignor_id is not null and external_assignor_username is null;
update job_request_acceptance_task_history set user_username =external_resource_id where external_resource_id is not null and user_username is null;
update job_request_acceptance_history  set user_username =external_resource_id where external_resource_id is not null and user_username is null;
update job_request_history  set user_username =external_resource_id where external_resource_id is not null and user_username is null;
update issue_comment  set user_username =external_resource_id where external_resource_id is not null and user_username is null;












