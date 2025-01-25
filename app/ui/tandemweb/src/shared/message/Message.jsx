import "./Message.css";
import PropTypes from 'prop-types';
import { Link } from "react-router-dom";

const Message = ({ isMyMessage, userIcon, userLogin, text, dateTime }) => {
    return (
        <div className={`message ${isMyMessage ? "my-message" : "other-message"}`}>
            <div className="message-content">
                {/* User Image and Login */}
                <Link to={`/profile?login=${userLogin}`}>
                    <div className="user-info">
                        <img src={userIcon} alt="User Icon" className="user-icon" />
                        <span className="user-login">{userLogin}</span>
                    </div>
                </Link>

                {/* Message Text with Background Color */}
                <div className="message-text-container">
                    <p className="message-text">{text}</p>
                </div>

                {/* Date Time */}
                <span className="message-datetime">{dateTime}</span>
            </div>
        </div>
    );
};

Message.propTypes = {
    isMyMessage: PropTypes.bool.isRequired,
    userIcon: PropTypes.string.isRequired,
    userLogin: PropTypes.string.isRequired,
    text: PropTypes.string.isRequired,
    dateTime: PropTypes.string.isRequired,
};

export default Message;
