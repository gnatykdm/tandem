
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(32) NOT NULL UNIQUE,
    username VARCHAR(63) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR NOT NULL,
    key CHAR(32) NOT NULL UNIQUE,
    about TEXT,
    register_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_image TEXT
);

CREATE TABLE groups (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(32) NOT NULL,
    group_key CHAR(32) NOT NULL UNIQUE,
    group_icon TEXT,
    group_description TEXT,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_groups (
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    group_id INT NOT NULL REFERENCES groups(group_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, group_id)
);

CREATE TABLE followers (
    id SERIAL PRIMARY KEY,
    follower_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    following_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (follower_id, following_id)
);

CREATE TABLE content (
    content_id SERIAL PRIMARY KEY,
    photo_id INT REFERENCES photos(photo_id) ON DELETE SET NULL,
    video_id INT REFERENCES videos(video_id) ON DELETE SET NULL,
    audio_id INT REFERENCES audios(audio_id) ON DELETE SET NULL,
);

CREATE TABLE photos (
    photo_id SERIAL PRIMARY KEY,
    photo_url TEXT NOT NULL,
    description TEXT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE videos (
    video_id SERIAL PRIMARY KEY,
    video_url TEXT NOT NULL,
    description TEXT,
    duration INT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE audios (
    audio_id SERIAL PRIMARY KEY,
    audio_url TEXT NOT NULL,
    description TEXT,
    duration INT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE messages (
    message_id SERIAL PRIMARY KEY,
    sender_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    receiver_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    send_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_login ON users(login);

CREATE INDEX idx_followers_follower ON followers(follower_id);
CREATE INDEX idx_followers_following ON followers(following_id);

CREATE INDEX idx_messages_sender ON messages(sender_id);
CREATE INDEX idx_messages_receiver ON messages(receiver_id);

CREATE INDEX idx_content_photo ON content(photo_id);
CREATE INDEX idx_content_video ON content(video_id);
CREATE INDEX idx_content_audio ON content(audio_id);

CREATE OR REPLACE FUNCTION add_follower(follower_id INT, following_id INT)
RETURNS VOID AS $$
BEGIN
    IF follower_id = following_id THEN
        RAISE EXCEPTION 'User cannot follow themselves.';
    END IF;

    INSERT INTO followers (follower_id, following_id)
    VALUES (follower_id, following_id)
    ON CONFLICT (follower_id, following_id) DO NOTHING;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION remove_follower(follower_id INT, following_id INT)
RETURNS VOID AS $$
BEGIN
    DELETE FROM followers
    WHERE follower_id = follower_id AND following_id = following_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Follower relationship does not exist.';
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION send_message(sender INT, receiver INT, message_content TEXT)
RETURNS VOID AS $$
BEGIN
    IF sender = receiver THEN
        RAISE EXCEPTION 'User cannot send a message to themselves.';
    END IF;

    INSERT INTO messages (sender_id, receiver_id, content)
    VALUES (sender, receiver, message_content);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION like_content(content_id INT)
RETURNS VOID AS $$
BEGIN
    UPDATE content
    SET likes = likes + 1
    WHERE content_id = content_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Content with ID % does not exist.', content_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

INSERT INTO users (login, username, email, password, key, about, profile_image)
VALUES
('user1', 'User One', 'user1@example.com', 'password1', 'key123', 'About User One', 'image1.png'),
('user2', 'User Two', 'user2@example.com', 'password2', 'key456', 'About User Two', 'image2.png');

INSERT INTO groups (group_name, group_key, group_icon, group_description)
VALUES
('Group One', 'key_group1', 'icon1.png', 'Description for Group One'),
('Group Two', 'key_group2', 'icon2.png', 'Description for Group Two');

INSERT INTO user_groups (user_id, group_id)
VALUES
(1, 1),
(2, 2);

INSERT INTO followers (follower_id, following_id)
VALUES
(1, 2),
(2, 1);

INSERT INTO photos (photo_url, description) VALUES ('photo1.png', 'First Photo');
INSERT INTO videos (video_url, description, duration) VALUES ('video1.mp4', 'First Video', 120);
INSERT INTO audios (audio_url, description, duration) VALUES ('audio1.mp3', 'First Audio', 180);

INSERT INTO content (photo_id, likes) VALUES (1, 10);
INSERT INTO messages (sender_id, receiver_id, content) VALUES (1, 2, 'Hello, how are you?');

SELECT add_follower(1, 2);
SELECT remove_follower(1, 2);
SELECT send_message(1, 2, 'Hello again!');
SELECT like_content(1);
