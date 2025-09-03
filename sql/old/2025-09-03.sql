update transportation_job set cost = real_cost where cost is null;
update transportation_job set itinerary_cost  = real_cost where itinerary_cost is null;
update transportation_job set start_cost  = 0 where start_cost is null;
update transportation_job set handling_cost  = 0 where handling_cost  is null;
update transportation_job set estimated_itinerary_cost  = estimated_cost  where estimated_itinerary_cost is null;
update transportation_job set estimated_start_cost  = 0 where estimated_start_cost is null;

update transportation_request set itinerary_cost  = cost where itinerary_cost is null;
update transportation_request set start_cost  = 0 where start_cost is null;
update transportation_request set handling_cost  = 0 where handling_cost is null;
update transportation_request set estimated_itinerary_cost  = estimated_cost  where estimated_itinerary_cost is null;
update transportation_request set estimated_start_cost  = 0 where estimated_start_cost is null;


