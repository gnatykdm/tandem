-- Create schema for message management
CREATE SCHEMA IF NOT EXISTS message_management;

-- Create Message table to store messages
CREATE TABLE message_management."Message" (
    message_id SERIAL PRIMARY KEY,
    sender INT NOT NULL REFERENCES user_management."User"(id) ON DELETE CASCADE,  -- Ensure user is deleted with message
    content TEXT NOT NULL,
    send_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Procedure to add a new message
CREATE OR REPLACE PROCEDURE message_management.add_message(
    p_sender INT,
    p_content TEXT,
    p_send_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Default send_at to CURRENT_TIMESTAMP if not specified
) AS $$
BEGIN
    INSERT INTO message_management."Message" (sender, content, send_at)
    VALUES (p_sender, p_content, p_send_at);
END;
$$ LANGUAGE plpgsql;

-- Procedure to update an existing message
CREATE OR REPLACE PROCEDURE message_management.update_message(
    p_message_id INT,
    p_content TEXT,
    p_new_send_at TIMESTAMP
) AS $$
BEGIN
    UPDATE message_management."Message"
    SET content = p_content, send_at = p_new_send_at
    WHERE message_id = p_message_id;

    -- Optionally add an error handling in case message_id doesn't exist
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Message with ID % does not exist', p_message_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Function to get a message by its ID
CREATE OR REPLACE FUNCTION message_management.get_message_by_id(
    p_message_id INT
) RETURNS TABLE(message_id INT, sender INT, content TEXT, send_at TIMESTAMP) AS $$
BEGIN
    RETURN QUERY
    SELECT message_id, sender, content, send_at
    FROM message_management."Message"
    WHERE message_id = p_message_id;
END;
$$ LANGUAGE plpgsql;

-- Procedure to delete a message by its ID
CREATE OR REPLACE PROCEDURE message_management.delete_message(
    p_message_id INT
) AS $$
BEGIN
    DELETE FROM message_management."Message" WHERE message_id = p_message_id;

    -- Optionally add error handling for non-existent message
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Message with ID % does not exist', p_message_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Function to get all messages sent by a specific user
CREATE OR REPLACE FUNCTION message_management.get_messages_by_user(p_user_id INT)
RETURNS TABLE(message_id INT, sender INT, content TEXT, send_at TIMESTAMP) AS $$
BEGIN
    RETURN QUERY
    SELECT m.message_id, m.sender, m.content, m.send_at
    FROM message_management."Message" m
    WHERE m.sender = p_user_id;
END;
$$ LANGUAGE plpgsql;
