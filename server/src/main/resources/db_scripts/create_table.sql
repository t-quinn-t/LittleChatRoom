create table if not exists usr (
	uid serial primary key not null,
	uname varchar(50) not null,
	registered_time timestamp with time zone not null default now(),
	updated_time timestamp with time zone not null default now(),
	email varchar(100) not null,
	pwd varchar(100) not null
);

create or replace function update_user_trigger()
return trigger as 
$$
	begin;
	new.updated_time = now();
	return new;
	end;
$$ language plpgsql;

create or replace trigger update_user
	before update on usr
	for each row 
	execute procedure update_user_trigger();

create table if not exists mgs (
	mid serial primary key not null,
	from_user bigint not null,
	msg_content varchar(256) not null,
	sent_time timestamp with time zone not null default now(),
	foreign key(from_user) references usr(uid) 
)