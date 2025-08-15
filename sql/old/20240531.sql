update
	stock_row a,
	delivery_request b
set
	a.company_id = b.company_idcompany,
	a.customer_id = b.customer_idcustomer,
	a.supplier_id = b.supplier_idsupplier
where
	a.delivery_request_id = b.id;
	
update
	stock_row a,
	delivery_request b
set
	a.inbound_company_id  = b.company_idcompany,
	a.inbound_customer_id  = b.customer_idcustomer,
	a.inbound_supplier_id  = b.supplier_idsupplier
where
	a.inbound_delivery_request_id  = b.id;