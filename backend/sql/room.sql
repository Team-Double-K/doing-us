use hackerthon_11_11;



create table room(
	r_id int not null auto_increment primary key,
	r_owner_id varchar(255),
	r_title varchar(255),
	r_comments varchar(255),
    r_longitude double not null,
    r_latitude double not null,
	r_meet_date datetime,
	foreign key(r_owner_id) references user(u_id)	
	on delete cascade
	on update cascade

);