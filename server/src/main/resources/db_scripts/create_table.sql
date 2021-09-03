-- we probably need to support multiple chatroom 
create table if not exists chatrooms (
	room_id serial primary key not null,
	room_name varchar(50) not null
);

create table if not exists users (
	uid serial primary key not null,
	uname varchar(50) not null,
	registered_time timestamp with time zone not null default now(),
	updated_time timestamp with time zone not null default now(),
	email varchar(100) not null,
	pwd varchar(100) not null
);

create or replace function update_user_trigger()
returns trigger as
$$
	begin
	new.updated_time = now();
	return new;
	end;
$$ language plpgsql;

drop trigger if exists update_users on users;
create trigger update_users
	before update on users
	for each row 
	execute procedure update_user_trigger();

create table if not exists messages (
	message_id serial primary key not null,
	from_user bigint not null,
	from_room bigint not null,
	message_content varchar(256) not null,
	sent_time timestamp with time zone not null default now(),
	foreign key(from_user) references users(uid),
	foreign key(from_room) references chatrooms(room_id)
);

create table if not exists chatroom_user_mapping (
	registration_id serial primary key not null,
	room_id_fk bigint not null,
	user_id_fk bigint not null,
	foreign key(room_id_fk) references chatrooms(room_id),
	foreign key(user_id_fk) references users(uid)
);

create table if not exists ec_keypairs (
	public_key bytea primary key not null,
	private_key bytea not null
);