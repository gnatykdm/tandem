import { useState, useEffect } from "react";
import Message from "../../shared/message/Message";
import MessageInput from "../messageinput/MessageInput";
import "./Messages.css";
import PropTypes from "prop-types";

const Messages = ({ currentUserLogin, currentGroupName }) => {
    const [messages, setMessages] = useState([]);
    const [userImages, setUserImages] = useState({}); // Cache the user images
    const [userLogins, setUserLogins] = useState({}); // Cache the user logins
    const groupName = currentGroupName; // Replace with the actual group name
    const userLogin = currentUserLogin; // Replace with the current user's login
    const AWS_S3_BASE_URL = "https://your-bucket-name.s3.amazonaws.com"; // Replace with your AWS S3 base URL

    useEffect(() => {
        fetchMessages();
    }, []); // Runs once on component mount

    const fetchMessages = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/group/get_all_messages_from_group/${groupName}`);
            const data = await response.json();
    
            // Extract unique user IDs
            const userIds = [...new Set(data.map(msg => msg[1]))]; // Unique senderIds
            const images = { ...userImages }; // Spread current state of images to prevent overwriting existing data
            const logins = { ...userLogins }; // Spread current state of logins to prevent overwriting existing data
    
            // Fetch user images and logins if not already cached
            for (const userId of userIds) {
                if (!images[userId] || !logins[userId]) {
                    const userResponse = await fetch(`http://localhost:8080/api/v1/user/find_by_id/${userId}`);
                    const userData = await userResponse.json();
    
                    // Check if profileImage exists
                    console.log("User Data: ", userData); // Log userData to see if profileImage is defined
    
                    const profileImageUrl = userData.profileImage
                        ? userData.profileImage.startsWith("http") 
                            ? userData.profileImage 
                            : `${AWS_S3_BASE_URL}${userData.profileImage}`
                        : "/path/to/default/profile/image.png"; // Use default if not defined
    
                    images[userId] = profileImageUrl; // Use the profileImage URL
                    logins[userId] = userData.login; // Store the user login
                }
            }
    
            // Update the user images and logins state
            setUserImages(images);
            setUserLogins(logins);
    
            // Map through the fetched messages and set the correct user image and login
            const fetchedMessages = data.map((msg) => ({
                id: msg[0], // Message ID
                text: msg[2], // Message content
                dateTime: new Date(msg[3]).toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }), // Format time
                isMyMessage: msg[1] === 39, // Check if the current user is the sender
                userIcon: images[msg[1]] || "/path/to/default/profile/image.png", // Get user image, fallback if not found
                userLogin: logins[msg[1]] || "Unknown User", // Get user login, fallback if not found
            }));
    
            setMessages(fetchedMessages);
        } catch (error) {
            console.error("Error fetching messages:", error);
        }
    };
    
    const handleSend = async (text) => {
        // Construct the URL with query parameters as per the backend's expected format
        const url = new URL(`http://localhost:8080/api/v1/group/add_message/${groupName}`);
        const params = new URLSearchParams();
        params.append('userLogin', userLogin);   // Add the user login
        params.append('messageContent', text);   // Add the message content
    
        try {
            // Send the POST request with the query parameters
            const response = await fetch(`${url}?${params.toString()}`, {
                method: "POST",
            });
    
            if (response.ok) {
                // Directly add the new message to the state without re-fetching all messages
                const newMsg = {
                    id: Date.now(), // Use the timestamp as a unique ID for the message
                    text: text,
                    dateTime: new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }),
                    isMyMessage: true,
                    userIcon: userImages[39] || "/path/to/default/profile/image.png", // Use a default image if none is found
                    userLogin: userLogin, // Use the current user's login
                };
    
                setMessages((prevMessages) => [...prevMessages, newMsg]);
            } else {
                console.error("Error sending message:", await response.text());
            }
        } catch (error) {
            console.error("Error sending message:", error);
        }
    };
    
    return (
        <div className="messages-container">
            <div className="messages-list">
                {messages.map((msg) => (
                    <Message
                        key={msg.id}
                        isMyMessage={msg.isMyMessage}
                        userIcon={msg.userIcon}  // Correctly using the profile image
                        userLogin={msg.userLogin} // Passing user login
                        text={msg.text}
                        dateTime={msg.dateTime}
                    />
                ))}
            </div>
            <MessageInput onSend={handleSend} />
        </div>
    );
};

Messages.propTypes = {
    currentUserLogin: PropTypes.string.isRequired,
    currentGroupName: PropTypes.string.isRequired
}

export default Messages;
