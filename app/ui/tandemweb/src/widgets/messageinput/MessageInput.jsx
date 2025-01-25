import { useState } from "react";
import "./MessageInput.css";
import PropTypes from 'prop-types';

const MessageInput = ({ onSend }) => {
    const [message, setMessage] = useState("");

    const handleSend = () => {
        if (message.trim()) {
            onSend(message);
            setMessage("");
        }
    };

    const handleChange = (e) => {
        setMessage(e.target.value);
    };

    return (
        <div className="message-input">
            <input
                type="text"
                className="input-field"
                value={message}
                placeholder="Type your message..."
                onChange={handleChange}
                onKeyDown={(e) => e.key === "Enter" && handleSend()}
            />
            <button 
                className="send-button" 
                onClick={handleSend} 
                disabled={!message.trim()}
            >
                Send
            </button>
        </div>
    );
};

MessageInput.propTypes = {
    onSend: PropTypes.func.isRequired,
};

export default MessageInput;
