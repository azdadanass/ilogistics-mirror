update
	stock_row a,
	delivery_request b
set
	a.delivery_request_detail_id = (
	select
		c.id
	from
		delivery_request_detail c
	where
		c.part_number_id = a.part_number_id
		and c.delivery_request_id = a.delivery_request_id
		)
where
	a.delivery_request_id = b.id
	and b.`type` = 'INBOUND'
	;
	
	
update
	stock_row a,
	delivery_request b
set
	a.delivery_request_detail_id = (
	select
		c.id
	from
		delivery_request_detail c
	where
		c.part_number_id = a.part_number_id
		and c.delivery_request_id = a.delivery_request_id
		and c.inbound_delivery_request_id  = a.inbound_delivery_request_id 
		and c.status  = a.status 
		)
where
	a.delivery_request_id = b.id
	and b.`type` = 'OUTBOUND'
	;
	
	
	
update
	stock_row a,
	delivery_request b
set
	a.delivery_request_detail_id = (
	select
		min(c.id)
	from
		delivery_request_detail c
	where
		c.part_number_id = a.part_number_id
		and c.delivery_request_id = a.delivery_request_id
		and (c.quantity = a.quantity
			or c.quantity = -a.quantity)
		and a.packing_id = c.packing_id
		and ((a.unit_cost is null and c.unit_cost is null) or a.unit_cost = c.unit_cost )
		)
where
	a.delivery_request_id = b.id
	and b.`type` = 'XBOUND'
	;
