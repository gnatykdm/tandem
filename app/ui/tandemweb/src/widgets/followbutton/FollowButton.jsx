import './FollowButton.css';
import axios from 'axios';
import { useState, useEffect } from 'react';
import PropTypes from 'prop-types';

const FollowButton = ({ userName, currentUserName }) => {
    const [isUserFollowing, setUserFollowing] = useState(null); 

    const checkFollowingStatus = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/v1/follows/is_user_follow/${currentUserName}`, {
                params: { follow: userName } 
            });
            setUserFollowing(response.data); 
        } catch (error) {
            console.error('Error checking follow status:', error);
        }
    };

    const handleFollow = async () => {
        try {
            await axios.post(`http://localhost:8080/api/v1/follows/follow/${currentUserName}`, null, {
                params: { follow: userName } 
            });
            setUserFollowing(true); 
        } catch (error) {
            console.error('Error following user:', error);
        }
    };

    const handleUnfollow = async () => {
        try {
            await axios.delete(`http://localhost:8080/api/v1/follows/unfollow/${currentUserName}`, {
                params: { follow: userName } 
            });
            setUserFollowing(false); 
        } catch (error) {
            console.error('Error unfollowing user:', error);
        }
    };

    useEffect(() => {
        checkFollowingStatus();
    }, [userName, currentUserName]);

    return (
        <div className="follow-button">
            {isUserFollowing === null ? (
                <p>Loading...</p> 
            ) : isUserFollowing ? (
                <button onClick={handleUnfollow} className="unfollow-button">
                    Unfollow
                </button>
            ) : (
                <button onClick={handleFollow} className="follow-button">
                    Follow
                </button>
            )}
        </div>
    );
};

FollowButton.propTypes = {
    userName: PropTypes.string.isRequired,
    currentUserName: PropTypes.string.isRequired 
};

export default FollowButton;
