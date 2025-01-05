-- Create schema for group management
CREATE SCHEMA IF NOT EXISTS group_management;

-- Create the Group table
CREATE TABLE group_management."Group" (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(32) NOT NULL,
    group_key CHAR(32) NOT NULL,
    group_icon TEXT NOT NULL,
    group_description TEXT,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    messages INT REFERENCES message_management."Message"(message_id), 
    access_code CHAR(10),
    type BOOLEAN NOT NULL
);

CREATE TABLE group_management.group_messages (
    group_id INT REFERENCES group_management."Group"(group_id),
    message_id INT REFERENCES message_management."Message"(message_id),
    PRIMARY KEY (group_id, message_id)
);

-- Create Optional table to track group memberships and admin status
CREATE TABLE IF NOT EXISTS group_management.Optional (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES user_management."User"(id) ON DELETE CASCADE,
    group_id INT NOT NULL REFERENCES group_management."Group"(group_id) ON DELETE CASCADE,
    admin BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE OR REPLACE PROCEDURE group_management.add_user_to_group(
    p_user_id INT,
    p_group_id INT,
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

-- Function to list users in a group with admin status
CREATE OR REPLACE FUNCTION group_management.list_group_users(p_group_id INT)
RETURNS TABLE(id INT, username VARCHAR, admin BOOLEAN) AS $$
BEGIN
    RETURN QUERY
    SELECT u.id, u.username, o.admin
    FROM "User" u
    INNER JOIN group_management.Optional o ON u.id = o.user_id
    WHERE o.group_id = p_group_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE group_management.remove_user_from_group(
    p_user_id INT,
    p_group_id INT
) AS $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM group_management.Optional WHERE user_id = p_user_id AND group_id = p_group_id) THEN
        RAISE EXCEPTION 'User % is not a member of group %', p_user_id, p_group_id;
    END IF;

    DELETE FROM group_management.Optional
    WHERE group_id = p_group_id AND user_id = p_user_id;
END;
$$ LANGUAGE plpgsql;

-- Procedure to delete content (ensure the Content table exists in content_management schema)
CREATE OR REPLACE PROCEDURE content_management.delete_content(
    p_content_id INT
) AS $$ 
BEGIN
    DELETE FROM "Content" WHERE content_id = p_content_id;
END;
$$ LANGUAGE plpgsql;

-- Function to check if a user is an admin of a group
CREATE OR REPLACE FUNCTION group_management.check_admin_rights(
    p_user_id INT,
    p_group_id INT
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
