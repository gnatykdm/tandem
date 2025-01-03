CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS "User" (
    id SERIAL PRIMARY KEY,
    login VARCHAR(32) NOT NULL UNIQUE,
    username VARCHAR(63) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    user_key CHAR(32) NOT NULL,
    about TEXT,
    profile_image TEXT
);

CREATE TABLE IF NOT EXISTS "Message" (
    message_id SERIAL PRIMARY KEY,
    sender INT NOT NULL REFERENCES "User"(id),
    content TEXT NOT NULL,
    send_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Photo (
    photo_id SERIAL PRIMARY KEY,
    photo_url TEXT NOT NULL,
    description TEXT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Video (
    video_id SERIAL PRIMARY KEY,
    video_url TEXT NOT NULL,
    description TEXT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Audio (
    audio_id SERIAL PRIMARY KEY,
    audio_url TEXT NOT NULL,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Text (
    text_id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "Content" (
    content_id SERIAL PRIMARY KEY,
    photo INT REFERENCES Photo(photo_id),
    video INT REFERENCES Video(video_id),
    audio INT REFERENCES Audio(audio_id),
    text INT REFERENCES Text(text_id)
);

CREATE TABLE IF NOT EXISTS "Group" (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(32) NOT NULL,
    group_key CHAR(32) NOT NULL,
    group_icon TEXT NOT NULL,
    group_description TEXT,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    messages INT REFERENCES "Message"(message_id),
    access_code CHAR(10),
    type BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS Optional (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES "User"(id),
    group_id INT NOT NULL REFERENCES "Group"(group_id),
    admin BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS follows (
    id SERIAL PRIMARY KEY,
    followers INT NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
    following INT NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
    UNIQUE (followers, following)
);

CREATE SCHEMA IF NOT EXISTS user_management;
CREATE SCHEMA IF NOT EXISTS group_management;
CREATE SCHEMA IF NOT EXISTS content_management;
CREATE SCHEMA IF NOT EXISTS message_management;

CREATE INDEX IF NOT EXISTS idx_user_login ON "User" (login);
CREATE INDEX IF NOT EXISTS idx_user_email ON "User" (email);
CREATE INDEX IF NOT EXISTS idx_group_name ON "Group" (group_name);
CREATE INDEX IF NOT EXISTS idx_content_type ON "Content" (photo, video, audio, text);
CREATE INDEX IF NOT EXISTS idx_group_id ON Optional (group_id);
CREATE INDEX IF NOT EXISTS idx_user_id ON Optional (user_id);

CREATE OR REPLACE PROCEDURE user_management.add_user(
    p_login VARCHAR(32),
    p_username VARCHAR(63),
    p_email VARCHAR(64),
    p_password VARCHAR(32),
    p_key CHAR(32),
    p_about TEXT,
    p_profile_image TEXT
) AS $$
BEGIN
    INSERT INTO "User" (login, username, email, password, user_key, about, profile_image)
    VALUES (p_login, p_username, p_email, crypt(p_password, gen_salt('bf', 8)), p_key, p_about, p_profile_image);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE user_management.update_user_profile(
    p_id INT,
    p_about TEXT,
    p_profile_image TEXT
) AS $$
BEGIN
    UPDATE "User"
    SET about = p_about,
        profile_image = p_profile_image
    WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION user_management.get_user_by_id(
    p_id INT
) RETURNS SETOF "User" AS $$
BEGIN
    RETURN QUERY
    SELECT id, login, username, email, about, profile_image
    FROM "User"
    WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION user_management.check_authorization(
    p_login VARCHAR(32),
    p_password VARCHAR(32)
) RETURNS BOOLEAN AS $$
DECLARE
    stored_password TEXT;
BEGIN
    SELECT password INTO stored_password
    FROM "User"
    WHERE login = p_login;

    IF NOT FOUND OR crypt(p_password, stored_password) <> stored_password THEN
        RETURN FALSE;
    END IF;

    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE content_management.add_content(
    p_photo INT,
    p_video INT,
    p_audio INT,
    p_text INT
) AS $$
BEGIN
    INSERT INTO "Content" (photo, video, audio, text)
    VALUES (p_photo, p_video, p_audio, p_text);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE content_management.delete_content(
    p_content_id INT
) AS $$
BEGIN
    DELETE FROM "Content" WHERE content_id = p_content_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE group_management.add_user_to_group(
    p_user_id INT,
    p_group_id INT,
    p_admin BOOLEAN
) AS $$
BEGIN
    INSERT INTO Optional (user_id, group_id, admin)
    VALUES (p_user_id, p_group_id, p_admin);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION group_management.list_group_users(p_group_id INT) RETURNS SETOF RECORD AS $$
DECLARE
    group_users RECORD;
    user_cursor CURSOR FOR
        SELECT u.id, u.username, o.admin
        FROM "User" u
        INNER JOIN Optional o ON u.id = o.user_id
        WHERE o.group_id = p_group_id;
BEGIN
    OPEN user_cursor;
    LOOP
        FETCH user_cursor INTO group_users;
        EXIT WHEN NOT FOUND;
        RETURN NEXT group_users;
    END LOOP;
    CLOSE user_cursor;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE group_management.remove_user_from_group(
    p_user_id INT,
    p_group_id INT
) AS $$ 
BEGIN
    DELETE FROM Optional
    WHERE group_id = p_group_id AND user_id = p_user_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE content_management.delete_content(
    p_content_id INT
) AS $$ 
BEGIN
    DELETE FROM "Content" WHERE content_id = p_content_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION group_management.check_admin_rights(
    p_user_id INT,
    p_group_id INT
) RETURNS BOOLEAN AS $$
DECLARE
    is_admin BOOLEAN;
BEGIN
    SELECT admin INTO is_admin
    FROM Optional
    WHERE user_id = p_user_id AND group_id = p_group_id;

    RETURN is_admin;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_user_changes() 
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO audit_log (action_type, table_name, record_id, action_details, performed_by)
    VALUES (TG_OP, 'User', NEW.id, row_to_json(NEW)::text, CURRENT_USER::INT);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION content_management.check_content_deletable(p_content_id INT) RETURNS BOOLEAN AS $$
DECLARE
    is_deletable BOOLEAN;
BEGIN
    SELECT CASE 
        WHEN EXISTS (SELECT 1 FROM "Content" WHERE content_id = p_content_id AND (photo IS NOT NULL OR video IS NOT NULL OR audio IS NOT NULL))
        THEN FALSE
        ELSE TRUE
    END INTO is_deletable;

    RETURN is_deletable;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE content_management.delete_content(
    p_content_id INT
) AS $$
BEGIN
    IF content_management.check_content_deletable(p_content_id) THEN
        DELETE FROM "Content" WHERE content_id = p_content_id;
    ELSE
        RAISE EXCEPTION 'Content cannot be deleted due to existing dependencies';
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION group_management.list_groups() RETURNS SETOF "Group" AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM "Group";
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_group_last_message_time() 
RETURNS TRIGGER AS $$
BEGIN
    UPDATE "Group"
    SET messages = NEW.message_id
    WHERE group_id = NEW.group_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE message_management.add_message(
    p_sender INT,
    p_content TEXT,
    p_send_at TIMESTAMP
) AS $$
BEGIN
    INSERT INTO "Message" (sender, content, send_at)
    VALUES (p_sender, p_content, p_send_at);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE message_management.update_message(
    p_message_id INT,
    p_content TEXT,
    p_send_at TIMESTAMP
) AS $$
BEGIN
    UPDATE "Message"
    SET content = p_content, send_at = p_send_at
    WHERE message_id = p_message_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION message_management.get_message_by_id(
    p_message_id INT
) RETURNS TABLE(message_id INT, sender INT, content TEXT, send_at TIMESTAMP) AS $$
BEGIN
    RETURN QUERY
    SELECT message_id, sender, content, send_at
    FROM "Message"
    WHERE message_id = p_message_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE message_management.delete_message(
    p_message_id INT
) AS $$
BEGIN
    DELETE FROM "Message" WHERE message_id = p_message_id;
END;
$$ LANGUAGE plpgsql;
