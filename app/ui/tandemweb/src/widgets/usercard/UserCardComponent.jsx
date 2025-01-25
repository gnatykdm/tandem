import './UserCardComponent.css';
import axios from 'axios';
import { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import settings_logo from '../../assets/icons/settings.png';
import EditModal from '../editmodal/EditModal';
import FollowButton from '../followbutton/FollowButton';

const UserCardComponent = ({ user }) => {
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [postsCount, setPostsCount] = useState(null); 
    const [followersCount, setFollowersCount] = useState(null); 
    const [followingCount, setFollowingCount] = useState(null); 
    const [currentUserLogin, setCurrentUserLogin] = useState(null);

    const backendUrl = import.meta.env.VITE_BACKEND_URL;

    const getCurrentUserLogin = () => {
        setCurrentUserLogin(localStorage.getItem("userLogin"))
    };

    const handleEditModal = () => {
        setIsEditModalOpen(true);
    };

    const handleCloseEditModal = () => {
        setIsEditModalOpen(false);
    };

    const getFollowers = (userLogin) => {
        axios
            .get(`${backendUrl}/api/v1/follows/get_followers/${userLogin}`) 
            .then(response => {
                setFollowersCount(response.data.length); 
            })
            .catch(error => {
                console.error("Error fetching followers:", error);
            });
    };

    const getFollowings = (userLogin) => {
        axios
            .get(`${backendUrl}/api/v1/follows/get_following/${userLogin}`) 
            .then(response => {
                setFollowingCount(response.data.length); 
            })
            .catch(error => {
                console.error("Error fetching following:", error);
            });
    };

    const getUserPostsCount = (userLogin) => {
        axios
            .get(`${backendUrl}/api/v1/user/get_post_count/${userLogin}`)
            .then((response) => {
                console.log(response.data); 
                setPostsCount(response.data);  
            })
            .catch((error) => {
                console.error("Error fetching post count:", error);
                setPostsCount(0);  
            });
    };

    useEffect(() => {
        getFollowers(user.login);  
        getFollowings(user.login); 
        getUserPostsCount(user.login);  
        getCurrentUserLogin();

    }, [user.login]);  

    return (
        <div className="user-card">
            <div className="user-card-header">
                <div className="user-profile_img">
                    <img 
                        src={user.profileImage} 
                        alt="Profile" 
                        className="profile-image" 
                    />
                    <div className="user-primary-info">
                        <h2 className="username">@{user.login}</h2> 
                        <h3 className="full-name">{user.username}</h3> 
                    </div>
                </div>

                <div className="user-stats">
                    <div className="user__stats">
                        <div className="stat-item">
                            <span className="stat-number">{followersCount || 0}</span>
                            <span className="stat-label">Followers</span>
                        </div>
                        <div className="stat-item">
                            <span className="stat-number">{followingCount || 0}</span>
                            <span className="stat-label">Following</span>
                        </div>
                        <div className="stat-item">
                            <span className="stat-number">{postsCount !== null ? postsCount : 'Loading...'}</span>
                            <span className="stat-label">Posts</span>
                        </div>
                    </div>


                { user.login === currentUserLogin ? (
                    <div className="settings-button">
                        <img src={settings_logo} alt="Settings" />
                        <button onClick={handleEditModal}>Edit</button>
                    </div>
                    ) : (
                        <center>
                            <FollowButton userName={user.login} currentUserName={currentUserLogin} />
                        </center>
                    )
                }

                </div>
            </div>

            <div className="user-card-info">
                <div className="user-header">
                    <p className="description">{user.about || "Here can be info about you"}</p>
                </div>
                
                <div className="user-details">
                    <div className="detail-item">
                        <p className="email">{user.email}</p>
                    </div>
                </div>
            </div>

            {isEditModalOpen && <EditModal onClose={handleCloseEditModal} userLogin={user.login} />}
        </div>
    );
};

UserCardComponent.propTypes = {
    user: PropTypes.shape({
        login: PropTypes.string.isRequired,
        username: PropTypes.string.isRequired,
        profileImage: PropTypes.string,
        followersCount: PropTypes.number,
        followingCount: PropTypes.number,
        postsCount: PropTypes.number,
        about: PropTypes.string,
        email: PropTypes.string,
        joinDate: PropTypes.string
    }).isRequired,
    
    onEdit: PropTypes.func.isRequired
};

export default UserCardComponent;