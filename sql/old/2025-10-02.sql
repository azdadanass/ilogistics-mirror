UPDATE packing_detail a
JOIN packing b ON a.parent_id = b.id
JOIN part_number c ON b.part_number_id = c.id
JOIN (
    SELECT parent_id
    FROM packing_detail
    GROUP BY parent_id
    HAVING COUNT(*) = 1
) d ON d.parent_id = b.id
SET a.name = c.name where a.name is null;