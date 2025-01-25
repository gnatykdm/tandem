CREATE SCHEMA IF NOT EXISTS user_management;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE user_management."User" (
    id SERIAL PRIMARY KEY,
    login VARCHAR(32) NOT NULL UNIQUE,
    username VARCHAR(63) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,  
    about TEXT,
    profile_image TEXT
);

CREATE TABLE IF NOT EXISTS user_management.follows (
    id SERIAL PRIMARY KEY,
    followers INT NOT NULL REFERENCES user_management."User"(id) ON DELETE CASCADE,
    following INT NOT NULL REFERENCES user_management."User"(id) ON DELETE CASCADE,
    UNIQUE (followers, following)
);

CREATE OR REPLACE PROCEDURE user_management.add_user(
    p_login VARCHAR(32),
    p_username VARCHAR(63),
    p_email VARCHAR(64),
    p_password VARCHAR(32),
    p_about TEXT,
    p_profile_image TEXT
) AS $$
BEGIN
    INSERT INTO user_management."User" (login, username, email, password, about, profile_image)
    VALUES (p_login, p_username, p_email, crypt(p_password, gen_salt('bf', 8)), p_about, p_profile_image);
END;
$$ LANGUAGE plpgsql;

-- Procedure to Update User Profile Information (about and profile_image)
CREATE OR REPLACE PROCEDURE user_management.update_user_profile(
    p_id INT,
    p_about TEXT,
    p_profile_image TEXT
) AS $$
BEGIN
    UPDATE user_management."User"
    SET about = p_about,
        profile_image = p_profile_image
    WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;

-- Function to Get a User by Login
CREATE OR REPLACE FUNCTION user_management.get_user_by_login(p_login VARCHAR)
RETURNS TABLE(id INT, login VARCHAR(32), username VARCHAR(63), email VARCHAR(64), password VARCHAR(255), about TEXT, profile_image TEXT) AS $$
BEGIN
    RETURN QUERY
    SELECT u.id, u.login, u.username, u.email, u.password, u.about, u.profile_image
    FROM user_management."User" u
    WHERE u.login = p_login;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION user_management.get_user_by_email(p_email VARCHAR)
RETURNS TABLE(
    id INT,
    login VARCHAR(32),
    username VARCHAR(63),
    email VARCHAR(64),
    password VARCHAR(255),
    about TEXT,
    profile_image TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT u.id, u.login, u.username, u.email, u.password, u.about, u.profile_image
    FROM user_management."User" u
    WHERE u.email = p_email;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION user_management.get_all_users_cursor()
RETURNS REFCURSOR AS $$
DECLARE
    user_cursor REFCURSOR;
BEGIN

    OPEN user_cursor FOR
    SELECT u.id, u.login, u.username, u.email, u.password, u.about, u.profile_image
    FROM user_management."User" u;

    RETURN user_cursor;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION user_management.get_user_by_id(
    p_id BIGINT
)
    RETURNS TABLE(
                     id INT,
                     login VARCHAR(32),
                     username VARCHAR(63),
                     email VARCHAR(64),
                     password VARCHAR(255),
                     about TEXT,
                     profile_image TEXT
                 ) AS $$
BEGIN
    RETURN QUERY
        SELECT
            u.id,
            u.login,
            u.username,
            u.email,
            u.password,
            u.about,
            u.profile_image
        FROM
            user_management."User" u
        WHERE
            u.id = p_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION user_management.does_user_exist(
    p_id BIGINT
) RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (
        SELECT 1
        FROM user_management."User"
        WHERE id = p_id
    );
END;
$$ LANGUAGE plpgsql;


-- Function to Check Authorization (Login and Password)
CREATE OR REPLACE FUNCTION user_management.check_authorization(
    p_login VARCHAR(32),
    p_password VARCHAR(32)
) RETURNS BOOLEAN AS $$
DECLARE
    stored_password TEXT;
BEGIN
    SELECT password INTO stored_password
    FROM user_management."User"
    WHERE login = p_login;

    IF NOT FOUND OR crypt(p_password, stored_password) <> stored_password THEN
        RETURN FALSE;
    END IF;

    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

-- Procedure to Follow Another User
CREATE OR REPLACE PROCEDURE user_management.follow_user(
    p_follower_id BIGINT,
    p_following_id BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO user_management.follows (followers, following)
    VALUES (p_follower_id, p_following_id)
    ON CONFLICT (followers, following) DO NOTHING;
END;
$$;

-- Procedure to Unfollow Another User
CREATE OR REPLACE PROCEDURE user_management.unfollow_user(
    p_follower_id BIGINT,
    p_following_id BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM user_management.follows
    WHERE followers = p_follower_id AND following = p_following_id;
END;
$$;

-- Function to Check if One User is Following Another
CREATE OR REPLACE FUNCTION user_management.is_user_following(
    p_follower_id BIGINT,
    p_following_id BIGINT
)
RETURNS BOOLEAN
LANGUAGE plpgsql
AS $$
DECLARE
    is_following BOOLEAN;
BEGIN
    SELECT EXISTS(
        SELECT 1 FROM user_management.follows
        WHERE followers = p_follower_id AND following = p_following_id
    ) INTO is_following;
    
    RETURN is_following;
END;
$$;

CREATE OR REPLACE FUNCTION user_management.get_follower_count(
    p_user_id BIGINT
) RETURNS INT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM user_management.follows
        WHERE following = p_user_id
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION user_management.get_following_count(
    p_user_id BIGINT
) RETURNS INT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM user_management.follows
        WHERE followers = p_user_id
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION user_management.get_following(p_user_id BIGINT)
RETURNS REFCURSOR AS $$
DECLARE
    following_cursor REFCURSOR;
BEGIN
    OPEN following_cursor FOR
    SELECT u.id, u.login, u.username, u.email, u.about, u.profile_image
    FROM user_management.follows f
    JOIN user_management."User" u ON f.following = u.id
    WHERE f.followers = p_user_id;

    RETURN following_cursor;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE PROCEDURE user_management.delete_user(
    p_id BIGINT
) AS $$
BEGIN
    DELETE FROM message_management.group_messages
    WHERE message_id IN (
        SELECT id FROM message_management."Message" WHERE sender = p_id
    );
        DELETE FROM message_management."Message" WHERE sender = p_id;

    DELETE FROM content_management.photo WHERE user_id = p_id;
    DELETE FROM content_management.video WHERE user_id = p_id;
    DELETE FROM content_management.audio WHERE user_id = p_id;

    DELETE FROM user_management."User" WHERE id = p_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'User with ID % not found', p_id;
    END IF;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE PROCEDURE user_management.update_user_profile(
    p_id BIGINT,
    p_about TEXT DEFAULT NULL,
    p_profile_image TEXT DEFAULT NULL,
    p_username VARCHAR(63) DEFAULT NULL
) AS $$
BEGIN
    UPDATE user_management."User"
    SET 
        about = COALESCE(p_about, about),
        profile_image = COALESCE(p_profile_image, profile_image),
        username = COALESCE(p_username, username)
    WHERE id = p_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'User with ID % not found', p_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

