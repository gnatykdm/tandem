-- Create schema for message management
CREATE SCHEMA IF NOT EXISTS message_management;

CREATE TABLE message_management."Message" (
    message_id SERIAL PRIMARY KEY,
    sender INT NOT NULL REFERENCES user_management."User"(id) ON DELETE CASCADE,  
    content TEXT NOT NULL,
    send_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE group_management.Messages (
    group_id INT REFERENCES group_management."Group"(group_id),
    message_id INT REFERENCES message_management."Message"(message_id),
    PRIMARY KEY (group_id, message_id)
);


CREATE OR REPLACE PROCEDURE message_management.add_message(
    p_sender INT,  
    p_content TEXT,
    p_group_id INT,  
    p_send_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) AS $$
DECLARE
    v_message_id INT;
BEGIN
    INSERT INTO message_management."Message" (sender, content, send_at)
    VALUES (p_sender, p_content, p_send_at)
    RETURNING message_id INTO v_message_id;

    INSERT INTO group_management.Messages (group_id, message_id)
    VALUES (p_group_id, v_message_id);
END;
$$ LANGUAGE plpgsql;

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

CREATE OR REPLACE PROCEDURE message_management.delete_message(
    p_message_id INT
) AS $$
BEGIN
    DELETE FROM message_management."Message" WHERE message_id = p_message_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Message with ID % does not exist', p_message_id;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION message_management.get_messages_by_user(p_user_id INT)
RETURNS TABLE(message_id INT, sender INT, content TEXT, send_at TIMESTAMP) AS $$
BEGIN
    RETURN QUERY
    SELECT m.message_id, m.sender, m.content, m.send_at
    FROM message_management."Message" m
    WHERE m.sender = p_user_id;
END;
$$ LANGUAGE plpgsql;
