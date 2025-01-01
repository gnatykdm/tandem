import './UserCardComponent.css';
import { useState } from 'react';
import profile_logo from '../../assets/images/musk.jpg'; 
import settings_logo from '../../assets/icons/settings.png';
import EditModal from '../editmodal/EditModal';
import PostModal from '../postmodal/PostModal';

const UserCardComponent = () => {
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [isPostModalOpen, setIsPostModalOpen] = useState(false);

    const handleEditModal = () => {
        setIsEditModalOpen(true);
    };

    const handleCloseEditModal = () => {
        setIsEditModalOpen(false);
    };

    const handlePostModal = () => {
        setIsPostModalOpen(true);
    };

    const handleClosePostModal = () => {
        setIsPostModalOpen(false);
    };

    return (
        <div className="user-card">
            <div className="user-card-header">
                <div className="user-profile_img">
                    <img src={profile_logo} alt="Profile" className="profile-image" />
                    <div className="user-primary-info">
                        <h2 className="username">@elonmusk</h2> 
                        <h3 className="full-name">Elon Musk</h3> 
                    </div>
                </div>
                <div className="user-stats">
                    <div className="user__stats">
                        <div className="stat-item">
                            <span className="stat-number">245</span>
                            <span className="stat-label">Followers</span>
                        </div>
                        <div className="stat-item">
                            <span className="stat-number">156</span>
                            <span className="stat-label">Following</span>
                        </div>
                        <div className="stat-item">
                            <span className="stat-number">12</span>
                            <span className="stat-label">Posts</span>
                        </div>
                    </div>
                    <div className="settings-button">
                        <img src={settings_logo} alt="Settings" />
                        <button onClick={handleEditModal}>Edit</button>
                        <button onClick={handlePostModal}>Post</button>
                    </div>
                </div>
            </div>

            <div className="user-card-info">
                <div className="user-header">
                    <p className="description">
                        CEO of Tesla and SpaceX. Founder of The Boring Company and Neuralink. 
                        Passionate about sustainable energy, space exploration, and technology.
                    </p>
                </div>
                
                <div className="user-details">
                    <div className="detail-item">
                        <p className="email">contact@spacex.com</p>
                    </div>
                    <div className="detail-item">
                        <p className="join-date">Joined: 03.01.2002</p>
                    </div>
                </div>
            </div>

            {isEditModalOpen && <EditModal onClose={handleCloseEditModal} />}
            {isPostModalOpen && <PostModal onClose={handleClosePostModal} />}
        </div>
    );
};

export default UserCardComponent;
