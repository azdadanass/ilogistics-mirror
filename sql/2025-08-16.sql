update vehicle_type set image = 'files/no-image.png' where image is null;
update vehicle_type set gross_weight = 0 where gross_weight is null;
update vehicle_type set max_volume = 0 where max_volume is null;