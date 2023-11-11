use hackerthon_11_11;

create table user_join_room(
	u_id varchar(255),
	r_id int,
	j_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	foreign key(u_id) references user(u_id)
	on delete cascade
	on update cascade,
	foreign key(r_id) references room(r_id)
	on delete cascade
	on update cascade
);
