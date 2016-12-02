------------
-- SCHEMA --
------------
create table if not exists Alert (
	id          	bigserial primary key,
	message_type	varchar(255),	
	message_ts   	timestamp,
	message_value 	float,
	message_qlty    float,
	building_id		float,
	floor_id		float,
	room_id 		float,
	equipment_id	float,
	date_created    timestamp
);
