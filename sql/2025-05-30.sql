update delivery_request set outbound_type  = 'TRANSFER' where `type`  = 'OUTBOUND' and is_for_transfer is true;
update delivery_request set outbound_type  = 'PLANNED_RETURN' where `type`  = 'OUTBOUND' and is_for_return is true;
update delivery_request set outbound_type  = 'NORMAL' where `type`  = 'OUTBOUND' and outbound_type is null;