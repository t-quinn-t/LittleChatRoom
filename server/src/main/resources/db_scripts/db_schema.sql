-- @author: Quinn Tao
-- @last updated on Dec 31

-- ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝　Users Table　＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝－－
CREATE TABLE IF NOT EXISTS users (
  user_id SERIAL PRIMARY KEY NOT NULL,
  user_name VARCHAR(50) NOT NULL,
  email VARCHAR(50) NOT NULL,
  registered_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  password VARCHAR(100) NOT NULL,
  private_key BYTEA
);

CREATE OR REPLACE FUNCTION update_user_trigger()
RETURNS TRIGGER AS
$$
    BEGIN
    new.updated_time = now();
    RETURN new;
    END;
$$ LANGUAGE plpgsql;

DROP TRIGGER if EXISTS update_user ON users;
CREATE TRIGGER update_user BEFORE UPDATE ON users FOR EACH ROW
    EXECUTE PROCEDURE update_user_trigger();

-- ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝　Chatrooms Table　＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝－－
CREATE TABLE IF NOT EXISTS chatrooms (
    room_id SERIAL PRIMARY KEY NOT NULL,
    room_name VARCHAR(50) NOT NULL,
    host_id BIGINT NOT NULL,
    FOREIGN KEY (host_id) REFERENCES users ON DELETE NO ACTION
);

-- ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝　Messages Table　＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝－－
CREATE TABLE IF NOT EXISTS messages (
	message_id SERIAL PRIMARY KEY NOT NULL,
	from_user BIGINT NOT NULL,
	from_room BIGINT NOT NULL,
	message_content VARCHAR(256) NOT NULL,
	sent_time TIMESTAMP WITH TIME ZONE DEFAULT now(),
	FOREIGN KEY (from_user) REFERENCES users(user_id) ON DELETE CASCADE,
	FOREIGN KEY (from_room) REFERENCES chatrooms(room_id) ON DELETE CASCADE
);

-- ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝　Chatroom_User_Mappings Table　＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝－－
CREATE TABLE IF NOT EXISTS chatroom_user_mappings (
	registration_id serial PRIMARY KEY NOT NULL,
	room_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
    user_nickname VARCHAR(50),
    as_role BIGINT NOT NULL,
	FOREIGN KEY (room_id) REFERENCES chatrooms(room_id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝　User_Preferences Table　＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝－－
CREATE TABLE IF NOT EXISTS user_preferences
(
    preference_id SERIAL UNIQUE NOT NULL,
    user_id BIGINT UNIQUE NOT NULL,
    ui_preference_data JSONB NOT NULL,
    notification_preference_data JSONB NOT NULL,
    PRIMARY KEY (preference_id, user_id),
	FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝　Role_Permissions Table　＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝－－
CREATE TABLE IF NOT EXISTS role_specifications 
(
    role_spec_id SERIAL UNIQUE NOT NULL,
    role_nickname VARCHAR(50) NOT NULL, 
    role_permission_data JSONB,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE 
)

