-- Create the content management schema
CREATE SCHEMA IF NOT EXISTS content_management;

-- Create table for photos
CREATE TABLE IF NOT EXISTS content_management.Photo (
    photo_id SERIAL PRIMARY KEY,
    photo_url TEXT NOT NULL,
    description TEXT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INT REFERENCES user_management."User"(id)
);

-- Create table for videos
CREATE TABLE IF NOT EXISTS content_management.Video (
    video_id SERIAL PRIMARY KEY,
    video_url TEXT NOT NULL,
    description TEXT,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INT REFERENCES user_management."User"(id)
);

-- Create table for audios
CREATE TABLE IF NOT EXISTS content_management.Audio (
    audio_id SERIAL PRIMARY KEY,
    audio_url TEXT NOT NULL,
    post_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INT REFERENCES user_management."User"(id)
);

-- Create table for text content
CREATE TABLE IF NOT EXISTS content_management.Text (
    text_id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
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

-- Procedure to add content, storing IDs of each content type
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

-- Procedure to delete content by its ID
CREATE OR REPLACE PROCEDURE content_management.delete_content(
    p_content_id INT
) AS $$
BEGIN
    DELETE FROM content_management.Content WHERE content_id = p_content_id;
END;
$$ LANGUAGE plpgsql;

-- Function to get all content (photo, video, text) for a specific user
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
    
    UNION ALL
    
    SELECT 'text' AS content_type, t.text_id, t.content AS media_url, NULL AS description, t.post_at
    FROM content_management.Text t
    JOIN content_management.Content c ON t.text_id = c.text_id
    WHERE c.user_id = p_user_id;
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
    photo_id INT,
    photo_url TEXT,
    description TEXT,
    post_at TIMESTAMP,
    user_id INT
) AS $$
BEGIN
    RETURN QUERY SELECT * FROM content_management.Photo WHERE photo_id = p_photo_id;
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
RETURNS TABLE (
    photo_id INT,
    photo_url TEXT,
    description TEXT,
    post_at TIMESTAMP,
    user_id INT
) AS $$
BEGIN
    RETURN QUERY SELECT * FROM content_management.Photo ORDER BY post_at DESC;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION content_management.get_photos_by_user(
    p_user_id BIGINT
) RETURNS TABLE (
    photo_id INT,
    photo_url TEXT,
    description TEXT,
    post_at TIMESTAMP,
    user_id INT
) AS $$
BEGIN
    RETURN QUERY 
    SELECT 
        p.photo_id,
        p.photo_url,
        p.description,
        p.post_at,
        p.user_id 
    FROM content_management.Photo p
    WHERE p.user_id = p_user_id 
    ORDER BY p.post_at DESC;
END;
$$ LANGUAGE plpgsql;


-- CRUD for Video -- 
CREATE OR REPLACE PROCEDURE content_management.create_video(
    p_video_url TEXT,
    p_description TEXT DEFAULT NULL,
    p_user_id INT DEFAULT NULL
) AS $$
BEGIN
    INSERT INTO content_management.Video (video_url, description, user_id)
    VALUES (p_video_url, p_description, p_user_id);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION content_management.get_video_by_id(
    p_video_id BIGINT
) RETURNS TABLE (
    video_id INT,
    video_url TEXT,
    description TEXT,
    post_at TIMESTAMP,
    user_id INT
) AS $$
BEGIN
    RETURN QUERY SELECT * FROM content_management.Video WHERE video_id = p_video_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION content_management.get_all_videos()
RETURNS TABLE (
    video_id INT,
    video_url TEXT,
    description TEXT,
    post_at TIMESTAMP,
    user_id INT
) AS $$
BEGIN
    RETURN QUERY SELECT * FROM content_management.Video ORDER BY post_at DESC;
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

CREATE OR REPLACE FUNCTION content_management.get_videos_by_user(
    p_user_id BIGINT
) RETURNS TABLE (
    video_id INT,
    video_url TEXT,
    description TEXT,
    post_at TIMESTAMP,
    user_id INT
) AS $$
BEGIN
    RETURN QUERY 
    SELECT 
        v.video_id,
        v.video_url,
        v.description,
        v.post_at,
        v.user_id 
    FROM content_management.Video v
    WHERE v.user_id = p_user_id 
    ORDER BY v.post_at DESC;
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


CREATE OR REPLACE FUNCTION content_management.get_audio_by_id(
    p_audio_id BIGINT
) RETURNS TABLE (
    audio_id INT,
    audio_url TEXT,
    post_at TIMESTAMP,
    user_id INT
) AS $$
BEGIN
    RETURN QUERY SELECT * FROM content_management.Audio WHERE audio_id = p_audio_id;
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

CREATE OR REPLACE FUNCTION content_management.get_audios_by_user(
    p_user_id BIGINT
) RETURNS TABLE (
    audio_id INT,
    audio_url TEXT,
    post_at TIMESTAMP,
    user_id INT
) AS $$
BEGIN
    RETURN QUERY 
    SELECT 
        a.audio_id, 
        a.audio_url, 
        a.post_at, 
        a.user_id 
    FROM content_management.Audio a
    WHERE a.user_id = p_user_id 
    ORDER BY a.post_at DESC;
END;
$$ LANGUAGE plpgsql;


-- CRUD for Text -- 
CREATE OR REPLACE PROCEDURE content_management.create_text_content(
    p_content TEXT,
    p_user_id BIGINT DEFAULT NULL
) AS $$
BEGIN
    INSERT INTO content_management.Text (content, user_id)
    VALUES (p_content, p_user_id);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION content_management.get_text_by_id(
    p_text_id BIGINT
) RETURNS TABLE (
    text_id INT,
    content TEXT,
    post_at TIMESTAMP,
    user_id INT
) AS $$
BEGIN
    RETURN QUERY SELECT * FROM content_management.Text WHERE text_id = p_text_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE content_management.delete_text_content(
    p_text_id INT
) AS $$
BEGIN
    DELETE FROM content_management.Text WHERE text_id = p_text_id;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Text content with ID % not found', p_text_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION content_management.get_all_texts()
RETURNS TABLE (
    text_id INT,
    content TEXT,
    post_at TIMESTAMP,
    user_id INT
) AS $$
BEGIN
    RETURN QUERY SELECT * FROM content_management.Text ORDER BY post_at DESC;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE content_management.delete_text_content(
    p_text_id BIGINT
) AS $$
BEGIN
    DELETE FROM content_management.Text WHERE text_id = p_text_id;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Text content with ID % not found', p_text_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION content_management.get_texts_by_user(
    p_user_id BIGINT
) RETURNS TABLE (
    text_id INT,
    content TEXT,
    post_at TIMESTAMP,
    user_id INT
) AS $$
BEGIN
    RETURN QUERY 
    SELECT 
        t.text_id,
        t.content,
        t.post_at,
        t.user_id
    FROM content_management.Text t
    WHERE t.user_id = p_user_id 
    ORDER BY t.post_at DESC;
END;
$$ LANGUAGE plpgsql;

