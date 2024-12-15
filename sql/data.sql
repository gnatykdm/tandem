CREATE TABLE "User" (
    id SERIAL PRIMARY KEY,
    login VARCHAR(32) NOT NULL,
    username VARCHAR(63) NOT NULL,
    email VARCHAR(64) NOT NULL,
    password VARCHAR(32) NOT NULL,
    user_key CHAR(32) NOT NULL,
    about TEXT,
    profile_image TEXT
);

CREATE TABLE "Message" (
    message_id SERIAL PRIMARY KEY,
    sender INT NOT NULL REFERENCES "User"(id),
    content TEXT NOT NULL,
    send_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Photo (
    photo_id SERIAL PRIMARY KEY,
    photo_url TEXT NOT NULL,
    description TEXT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Video (
    video_id SERIAL PRIMARY KEY,
    video_url TEXT NOT NULL,
    description TEXT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Audio (
    audio_id SERIAL PRIMARY KEY,
    audio_url TEXT NOT NULL,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "Content" (
    content_id SERIAL PRIMARY KEY,
    photo INT REFERENCES Photo(photo_id),
    video INT REFERENCES Video(video_id),
    audio INT REFERENCES Audio(audio_id)
);

CREATE TABLE follows (
    id SERIAL PRIMARY KEY,
    followers BIGINT NOT NULL,
    following BIGINT NOT NULL,
    CONSTRAINT fk_followers FOREIGN KEY (followers) REFERENCES "User"(id) ON DELETE CASCADE,
    CONSTRAINT fk_following FOREIGN KEY (following) REFERENCES "User"(id) ON DELETE CASCADE
);

CREATE TABLE "Group" (
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

CREATE TABLE Optional (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES "User"(id),
    group_id INT NOT NULL REFERENCES "Group"(group_id),
    admin BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE OR REPLACE PROCEDURE add_user(
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
    VALUES (p_login, p_username, p_email, p_password, p_key, p_about, p_profile_image);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE update_user_profile(
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

CREATE OR REPLACE FUNCTION add_content(
    p_photo INT,
    p_video INT,
    p_audio INT
) RETURNS INT AS $$ 
DECLARE
    new_content_id INT;
BEGIN
    INSERT INTO "Content" (photo, video, audio)
    VALUES (p_photo, p_video, p_audio)
    RETURNING content_id INTO new_content_id;

    RETURN new_content_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE add_user_to_group(
    p_user_id INT,
    p_group_id INT,
    p_admin BOOLEAN
) AS $$ 
BEGIN
    INSERT INTO Optional (user_id, group_id, admin)
    VALUES (p_user_id, p_group_id, p_admin);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION list_group_users(p_group_id INT) RETURNS SETOF RECORD AS $$ 
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

CREATE OR REPLACE PROCEDURE remove_user_from_group(
    p_user_id INT,
    p_group_id INT
) AS $$ 
BEGIN
    DELETE FROM Optional
    WHERE group_id = p_group_id AND user_id = p_user_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE invite_to_group(
    p_user_id INT,
    p_group_id INT,
    p_access_code CHAR(10)
) AS $$ 
BEGIN
    UPDATE "Group"
    SET access_code = p_access_code
    WHERE group_id = p_group_id;

    INSERT INTO Optional (user_id, group_id, admin)
    VALUES (p_user_id, p_group_id, FALSE);
END;
$$ LANGUAGE plpgsql;
