create table token(
	u_id varchar(255) not null,
    accessToken varchar(255) not null primary key,
    foreign key(u_id) references user(u_id)
    on delete cascade
    on update cascade
);