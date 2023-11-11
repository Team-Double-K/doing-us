use hackerthon_11_11;

create table user(
    u_id varchar(255) primary key not null,
    u_pw varchar(255) not null,
    u_name varchar(255) not null,
    u_birth date not null
);