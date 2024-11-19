import './UserCardComponent.css';
import profile_logo from '../../shared/assets/icons/profile.png';
import settings_logo from '../../shared/assets/icons/settings.png';

const UserCardComponent = () => {
    return (
        <div className="user-card">
            <div className="user-card-header">
                <img src={profile_logo} alt="Profile" className="profile-image" />
                <div className="user-stats">
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
                    <button className="settings-button">
                        <img src={settings_logo} alt="Settings" />
                    </button>
                </div>
            </div>

            <div className="user-card-info">
            <div className="user-header">
                    <div className="user-primary-info">
                        <h2 className="username">@gnatykdm</h2>
                        <h3 className="full-name">Dmytro Gnatyk</h3>
                    </div>
                    <p className="description">
                        Lorem ipsum dolor sit amet consectetur adipisicing elit.
                        Quisquam, quos. Lorem ipsum dolor sit amet consectetur adipisicing elit.
                        Quisquam, quos. Lorem ipsum dolor sit amet consectetur adipisicing elit.
                        Quisquam, quos. Lorem ipsum dolor sit amet consectetur adipisicing elit.
                        Quisquam, quos.
                    </p>
                </div>
                
                <div className="user-details">
                    <div className="detail-item">
                        <p className="email">gnatykwork@gmail.com</p>
                    </div>
                    <div className="detail-item">
                        <p className="join-date">Joined: 01.01.2023</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default UserCardComponent;