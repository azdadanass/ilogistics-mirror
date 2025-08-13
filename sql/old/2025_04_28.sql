update delivery_request o set o.missing_serial_number  = null where o.`type`  = 'outbound';

update
	stock_row sr ,
	delivery_request dr
set
dr.missing_serial_number  = false
where
	sr.delivery_request_id = dr.id
	and dr.`type` = 'outbound'
	and dr.status  in ('DELIVRED','ACKNOWLEDGED')
	and (
	select
		count(*)
	from
		delivery_request_serial_number drsn,
		stock_row sr2
	where
		drsn.inbound_stock_row_id = sr2.id
		and sr2.delivery_request_detail_id  = sr.inbound_delivery_request_detail_id 
		) >0;


update
	delivery_request_detail a,
	delivery_request b
set
b.missing_serial_number = true
where
	a.delivery_request_id = b.id
	and b.`type` = 'outbound'
	and b.missing_serial_number is false
	and (select count(*) from delivery_request_serial_number c,stock_row d where c.inbound_stock_row_id  = d.id  and d.delivery_request_detail_id  = a.inbound_delivery_request_detail_id) > 0
	and a.quantity * COALESCE((select sum(pd.quantity) from packing_detail pd where pd.parent_id = a.packing_id and pd.has_serialnumber is true),1) / (select p.quantity  from packing p where p.id = a.packing_id)  > (select count(*) from  delivery_request_serial_number x,stock_row y where x.inbound_stock_row_id = y.id and x.outbound_delivery_request_id = b.id and y.delivery_request_detail_id = a.inbound_delivery_request_detail_id) ;



update delivery_request a set a.missing_serial_number  = null where a.type = 'inbound' and (a.is_sn_required is null or a.is_sn_required is false);