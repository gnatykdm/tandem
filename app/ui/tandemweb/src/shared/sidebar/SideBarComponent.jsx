import './SideBarComponent.css';
import tandem_logo from '../../assets/icons/tandemlogo.png';
import home_logo from '../../assets/icons/home.png';
import profile_logo from '../../assets/icons/profile.png';
import settings_logo from '../../assets/icons/settings.png';
import logout_logo from '../../assets/icons/angle-left.png';
import files_logo from '../../assets/icons/files.png';
import users_logo from '../../assets/icons/users.png';
import search_logo from '../../assets/icons/search.png';

const SideBarComponent = () => {
    return (
        <div className="sidebar">
            <div className="logo">
                <img src={tandem_logo} alt="Tandem Logo" />
            </div>
            <div className="sidebar-menu">
                <ul className="sidebar-menu-list">
                    <li><img src={home_logo} alt="Home" />Home</li>
                    <li><img src={search_logo} alt="Search" />Search</li>
                    <li><img src={files_logo} alt="Files" />Files</li>
                    <li><img src={users_logo} alt="Users" />Groups</li>
                    <li><img src={settings_logo} alt="Settings" />Settings</li>
                </ul>
                <ul className="sidebar-menu-bottom">
                    <li><img src={profile_logo} alt="Profile" />Profile</li>
                    <li><img src={logout_logo} alt="Logout" />Logout</li>
                </ul>
            </div>
        </div>
    );
};

export default SideBarComponent;
