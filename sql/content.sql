-- Create the content management schema
CREATE SCHEMA IF NOT EXISTS content_management;

CREATE TABLE IF NOT EXISTS content_management.Photo (
    photo_id SERIAL PRIMARY KEY,
    photo_url TEXT NOT NULL,
    description TEXT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INT REFERENCES user_management."User"(id)
);

CREATE TABLE IF NOT EXISTS content_management.Video (
    video_id SERIAL PRIMARY KEY,
    video_url TEXT NOT NULL,
    description TEXT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INT REFERENCES user_management."User"(id)
);

CREATE TABLE IF NOT EXISTS content_management.Audio (
    audio_id SERIAL PRIMARY KEY,
    audio_url TEXT NOT NULL,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INT REFERENCES user_management."User"(id)
);

CREATE TABLE IF NOT EXISTS content_management.Content (
    content_id SERIAL PRIMARY KEY,
    photo_id INT REFERENCES content_management.Photo(photo_id) ON DELETE CASCADE,
    video_id INT REFERENCES content_management.Video(video_id) ON DELETE CASCADE,
    audio_id INT REFERENCES content_management.Audio(audio_id) ON DELETE CASCADE,
    text_id INT REFERENCES content_management.Text(text_id) ON DELETE CASCADE,
    user_id INT REFERENCES user_management."User"(id) 
);

CREATE OR REPLACE PROCEDURE content_management.add_content(
    p_user_id INT, 
    p_photo INT,
    p_video INT,
    p_audio INT,
    p_text INT
) AS $$
BEGIN
    IF p_photo IS NULL AND p_video IS NULL AND p_audio IS NULL AND p_text IS NULL THEN
        RAISE EXCEPTION 'At least one content type (photo, video, audio, text) must be provided.';
    END IF;

    INSERT INTO content_management.Content (photo_id, video_id, audio_id, text_id, user_id)
    VALUES (p_photo, p_video, p_audio, p_text, p_user_id); 
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE content_management.delete_content(
    p_content_id INT
) AS $$
BEGIN
    DELETE FROM content_management.Content WHERE content_id = p_content_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION content_management.get_content_by_user(p_user_id INT)
RETURNS TABLE(content_type TEXT, content_id INT, media_url TEXT, description TEXT, post_at TIMESTAMP) AS $$
BEGIN
    RETURN QUERY
    SELECT 'photo' AS content_type, p.photo_id, p.photo_url AS media_url, p.description, p.post_at
    FROM content_management.Photo p
    JOIN content_management.Content c ON p.photo_id = c.photo_id
    WHERE c.user_id = p_user_id
    
    UNION ALL

    SELECT 'video' AS content_type, v.video_id, v.video_url AS media_url, v.description, v.post_at
    FROM content_management.Video v
    JOIN content_management.Content c ON v.video_id = c.video_id
    WHERE c.user_id = p_user_id
END;
$$ LANGUAGE plpgsql;

-- CRUD for Photo -- 
CREATE OR REPLACE PROCEDURE content_management.create_photo(
    p_photo_url TEXT,
    p_description TEXT,
    p_user_id BIGINT
) AS $$
BEGIN
    INSERT INTO content_management.Photo (photo_url, description, user_id)
    VALUES (p_photo_url, p_description, p_user_id);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION content_management.get_photo_by_id(
    p_photo_id BIGINT
) RETURNS TABLE (
    photo_id BIGINT,          
    photo_url TEXT,
    description TEXT,
    post_at TIMESTAMP,
    user_id INT
) AS $$
BEGIN
    RETURN QUERY 
    SELECT 
        p.photo_id::BIGINT,  
        p.photo_url, 
        p.description, 
        p.post_at, 
        p.user_id
    FROM content_management.Photo p
    WHERE p.photo_id = p_photo_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE content_management.delete_photo(
    p_photo_id BIGINT
) AS $$
BEGIN
    DELETE FROM content_management.Photo WHERE photo_id = p_photo_id;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Photo with ID % not found', p_photo_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION content_management.get_all_photos()
RETURNS REFCURSOR AS $$
DECLARE
    photos_cursor REFCURSOR;
BEGIN
    OPEN photos_cursor FOR 
    SELECT photo_id, photo_url, description, post_at, user_id
    FROM content_management.Photo
    ORDER BY post_at DESC;

    RETURN photos_cursor;
END;
$$ LANGUAGE plpgsql;

-- CRUD for Video -- 
CREATE OR REPLACE PROCEDURE content_management.create_video(
    p_video_url TEXT,
    p_description TEXT DEFAULT NULL,
    p_user_id BIGINT DEFAULT NULL
) AS $$
BEGIN
    INSERT INTO content_management.Video (video_url, description, user_id)
    VALUES (p_video_url, p_description, p_user_id);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION content_management.get_all_videos()
RETURNS REFCURSOR AS $$
DECLARE
    videos_cursor REFCURSOR;
BEGIN
    OPEN videos_cursor FOR 
    SELECT video_id, video_url, description, post_at, user_id
    FROM content_management.Video
    ORDER BY post_at DESC;

    RETURN videos_cursor;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE content_management.delete_video(
    p_video_id BIGINT
) AS $$
BEGIN
    DELETE FROM content_management.Video WHERE video_id = p_video_id;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Video with ID % not found', p_video_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- CRUD for Audio -- 
CREATE OR REPLACE PROCEDURE content_management.create_audio(
    p_audio_url TEXT,
    p_user_id BIGINT DEFAULT NULL
) AS $$
BEGIN
    INSERT INTO content_management.Audio (audio_url, user_id)
    VALUES (p_audio_url, p_user_id);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE content_management.delete_audio(
    p_audio_id BIGINT
) AS $$
BEGIN
    DELETE FROM content_management.Audio WHERE audio_id = p_audio_id;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Audio with ID % not found', p_audio_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

