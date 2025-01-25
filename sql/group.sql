-- Create schema for group management
CREATE SCHEMA IF NOT EXISTS group_management;

-- Create the Group table
CREATE TABLE group_management."Group" (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(32) NOT NULL,
    group_icon TEXT NOT NULL,
    group_description TEXT,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    messages INT REFERENCES message_management."Message"(message_id)
);

CREATE TABLE IF NOT EXISTS content_management.Content (
    group_content_id SERIAL PRIMARY KEY,
    group_id BIGINT REFERENCES group_management."Group"(group_id) ON DELETE CASCADE,
    content_id BIGINT NOT NULL,  -- Assuming you want to link content directly here
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE group_management.Messages (
    group_id INT REFERENCES group_management."Group"(group_id),
    message_id INT REFERENCES message_management."Message"(message_id),
    PRIMARY KEY (group_id, message_id)
);

-- Create Optional table to track group memberships and admin status
CREATE TABLE IF NOT EXISTS group_management.Optional (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES user_management."User"(id) ON DELETE CASCADE,
    group_id INT NOT NULL REFERENCES group_management."Group"(group_id) ON DELETE CASCADE,
    admin BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT unique_user_group UNIQUE (user_id, group_id)
);

CREATE OR REPLACE PROCEDURE group_management.add_user_to_group(
    p_user_id BIGINT,
    p_group_id BIGINT,
    p_admin BOOLEAN
) AS $$  
BEGIN
    IF NOT EXISTS (SELECT 1 FROM group_management."Group" WHERE group_id = p_group_id) THEN
        RAISE EXCEPTION 'Group with ID % does not exist', p_group_id;
    END IF;

    INSERT INTO group_management.Optional (user_id, group_id, admin)
    VALUES (p_user_id, p_group_id, p_admin)
    ON CONFLICT (user_id, group_id) DO NOTHING;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION group_management.list_group_users(p_group_id BIGINT)
    RETURNS TABLE(id INT, username VARCHAR, admin BOOLEAN) AS $$  
BEGIN
    RETURN QUERY
    SELECT u.id, u.username, o.admin
    FROM user_management."User" u
    INNER JOIN group_management.Optional o ON u.id = o.user_id
    WHERE o.group_id = p_group_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE PROCEDURE group_management.remove_user_from_group(
    p_user_id BIGINT,
    p_group_id BIGINT
) AS $$  
BEGIN
    IF NOT EXISTS (SELECT 1 FROM group_management.Optional WHERE user_id = p_user_id AND group_id = p_group_id) THEN
        RAISE EXCEPTION 'User % is not a member of group %', p_user_id, p_group_id;
    END IF;

    DELETE FROM group_management.Optional
    WHERE group_id = p_group_id AND user_id = p_user_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE content_management.delete_content(
    p_content_id BIGINT
) AS $$  
BEGIN
    DELETE FROM content_management.Content WHERE content_id = p_content_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION group_management.check_admin_rights(
    p_user_id BIGINT,
    p_group_id BIGINT
) RETURNS BOOLEAN AS $$  
DECLARE
    is_admin BOOLEAN;
BEGIN
    SELECT admin INTO is_admin
    FROM group_management.Optional
    WHERE user_id = p_user_id AND group_id = p_group_id;

    RETURN COALESCE(is_admin, FALSE);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE content_management.add_content_to_group(
    p_group_id BIGINT,
    p_content_id BIGINT
) AS $$  
BEGIN
    IF NOT EXISTS (SELECT 1 FROM group_management."Group" WHERE group_id = p_group_id) THEN
        RAISE EXCEPTION 'Group with ID % does not exist', p_group_id;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM content_management.Content WHERE content_id = p_content_id) THEN
        RAISE EXCEPTION 'Content with ID % does not exist', p_content_id;
    END IF;

    -- Insert into Content table
    INSERT INTO content_management.Content (group_id, content_id)
    VALUES (p_group_id, p_content_id);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION content_management.get_content_by_group(
    p_group_id BIGINT
) RETURNS TABLE(
    content_type TEXT,
    content_id INT,
    media_url TEXT,
    description TEXT,
    post_at TIMESTAMP
) AS $$  
BEGIN
    RETURN QUERY
    SELECT 'photo' AS content_type, p.photo_id, p.photo_url, p.description, p.post_at
    FROM content_management.Photo p
    JOIN content_management.Content c ON c.content_id = p.photo_id
    JOIN content_management.GroupContent gc ON gc.content_id = c.content_id
    WHERE gc.group_id = p_group_id

    UNION ALL

    SELECT 'video' AS content_type, v.video_id, v.video_url, v.description, v.post_at
    FROM content_management.Video v
    JOIN content_management.Content c ON c.content_id = v.video_id
    JOIN content_management.GroupContent gc ON gc.content_id = c.content_id
    WHERE gc.group_id = p_group_id

    UNION ALL

    SELECT 'audio' AS content_type, a.audio_id, a.audio_url, NULL AS description, a.post_at
    FROM content_management.Audio a
    JOIN content_management.Content c ON c.content_id = a.audio_id
    JOIN content_management.GroupContent gc ON gc.content_id = c.content_id
    WHERE gc.group_id = p_group_id;
END;
$$ LANGUAGE plpgsql;

-- Procedure to create a new group
CREATE OR REPLACE PROCEDURE group_management.create_group(
    p_group_name VARCHAR(32),
    p_group_icon TEXT,
    p_group_description TEXT
) AS $$  
BEGIN
    INSERT INTO group_management."Group" (group_name, group_icon, group_description)
    VALUES (p_group_name, p_group_icon, p_group_description);
END;
$$ LANGUAGE plpgsql;

-- Procedure to delete a group
CREATE OR REPLACE PROCEDURE group_management.delete_group(
    p_group_id BIGINT
) AS $$  
BEGIN
    DELETE FROM group_management.Optional WHERE group_id = p_group_id;

    DELETE FROM content_management.Content WHERE group_id = p_group_id;

    DELETE FROM group_management.Messages WHERE group_id = p_group_id;

    DELETE FROM group_management."Group" WHERE group_id = p_group_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Group with ID % does not exist', p_group_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE group_management.update_group(
    p_group_id BIGINT,
    p_group_icon TEXT,
    p_group_description TEXT
) AS $$  
BEGIN
    IF NOT EXISTS (SELECT 1 FROM group_management."Group" WHERE group_id = p_group_id) THEN
        RAISE EXCEPTION 'Group with ID % does not exist', p_group_id;
    END IF;

    UPDATE group_management."Group"
    SET group_icon = p_group_icon,
        group_description = p_group_description
    WHERE group_id = p_group_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION group_management.get_messages_by_group(p_group_id BIGINT)
RETURNS TABLE(message_id BIGINT, sender BIGINT, content TEXT, send_at TIMESTAMP) AS $$  
BEGIN
    RETURN QUERY
    SELECT m.message_id::BIGINT, m.sender::BIGINT, m.content, m.send_at
    FROM message_management."Message" m
    INNER JOIN group_management.Messages gm 
        ON m.message_id = gm.message_id
    WHERE gm.group_id = p_group_id;
END;
$$ LANGUAGE plpgsql;
