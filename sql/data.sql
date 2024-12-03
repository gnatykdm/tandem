-- Tabela User
CREATE TABLE "User" (
    id SERIAL PRIMARY KEY,
    login VARCHAR(32) NOT NULL,
    username VARCHAR(63) NOT NULL,
    email VARCHAR(64) NOT NULL,
    password VARCHAR(32) NOT NULL,
    key CHAR(32) NOT NULL,
    about TEXT,
    profile_image TEXT
);

-- Tabela follows (relacje użytkowników)
CREATE TABLE follows (
    follower_id SERIAL REFERENCES "User"(id),
    following_id SERIAL REFERENCES "User"(id),
    PRIMARY KEY (follower_id, following_id)
);

-- Tabela Group (grupy użytkowników)
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

-- Tabela Admins (użytkownicy w grupach)
CREATE TABLE Admins (
    id SERIAL REFERENCES "User"(id),
    group_id INT NOT NULL REFERENCES "Group"(group_id),
    admin BOOLEAN NOT NULL DEFAULT FALSE
);

-- Tabela Message (wiadomości)
CREATE TABLE Message (
    message_id SERIAL PRIMARY KEY,
    sender INT NOT NULL REFERENCES "User"(id),
    content TEXT NOT NULL,
    send_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela Content (treści użytkownika)
CREATE TABLE Content (
    content_id SERIAL PRIMARY KEY,
    photo INT REFERENCES Photo(photo_id),
    video INT REFERENCES Video(video_id),
    audio INT REFERENCES Audio(audio_id)
);

-- Tabela Photo (zdjęcia)
CREATE TABLE Photo (
    photo_id SERIAL PRIMARY KEY,
    photo_url TEXT NOT NULL,
    description TEXT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela Video (filmy wideo)
CREATE TABLE Video (
    video_id SERIAL PRIMARY KEY,
    video_url TEXT NOT NULL,
    description TEXT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela Audio (pliki audio)
CREATE TABLE Audio (
    audio_id SERIAL PRIMARY KEY,
    audio_url TEXT NOT NULL,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela Optional (użytkownicy w grupach)
CREATE TABLE Optional (
    user_id SERIAL REFERENCES "User"(id),
    group_id SERIAL REFERENCES "Group"(group_id),
    PRIMARY KEY (user_id, group_id)
);

-- Funkcje i procedury PL/pgSQL

-- Dodawanie użytkownika
CREATE OR REPLACE PROCEDURE add_user(
    p_login VARCHAR(32),
    p_username VARCHAR(63),
    p_email VARCHAR(64),
    p_password VARCHAR(32),
    p_key CHAR(32),
    p_about TEXT,
    p_profile_image TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO "User" (login, username, email, password, key, about, profile_image)
    VALUES (p_login, p_username, p_email, p_password, p_key, p_about, p_profile_image);
END;
$$;

-- Aktualizacja profilu użytkownika
CREATE OR REPLACE PROCEDURE update_user_profile(
    p_id INT,
    p_about TEXT,
    p_profile_image TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE "User"
    SET about = p_about,
        profile_image = p_profile_image
    WHERE id = p_id;
END;
$$;

-- Dodawanie treści
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

-- Obsługa polubień zdjęć (trigger na Photo)
CREATE OR REPLACE FUNCTION increment_photo_likes()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OPNAME = 'INSERT' THEN
        UPDATE Photo
        SET likes = likes + 1
        WHERE photo_id = NEW.photo;
    ELSE IF TG_OPNAME = 'DELETE' THEN
        UPDATE Photo
        SET likes = likes - 1
        WHERE photo_id = OLD.photo;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Dodanie użytkownika do grupy
CREATE OR REPLACE PROCEDURE add_user_to_group(
    p_user_id INT,
    p_group_id INT,
    p_admin BOOLEAN
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO Optional (user_id, group_id, admin)
    VALUES (p_user_id, p_group_id, p_admin);
END;
$$;

-- Listowanie użytkowników grupy
CREATE OR REPLACE FUNCTION list_group_users(p_group_id INT)
RETURNS TABLE(id INT, username VARCHAR, admin BOOLEAN) AS $$
BEGIN
    RETURN QUERY
    SELECT u.id, u.username, o.admin
    FROM "User" u
    INNER JOIN Optional o ON u.id = o.user_id
    WHERE o.group_id = p_group_id;
END;
$$ LANGUAGE plpgsql;

-- Usunięcie użytkownika z grupy
CREATE OR REPLACE PROCEDURE remove_user_from_group(
    p_user_id INT,
    p_group_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM Optional
    WHERE group_id = p_group_id AND user_id = p_user_id;
END;
$$;

-- Zaproszenie użytkownika do grupy
CREATE OR REPLACE PROCEDURE invite_to_group(
    p_user_id INT,
    p_group_id INT,
    p_access_code CHAR(10)
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE "Group"
    SET access_code = p_access_code
    WHERE group_id = p_group_id;

    INSERT INTO Optional (user_id, group_id, admin)
    VALUES (p_user_id, p_group_id, FALSE);
END;
$$;
