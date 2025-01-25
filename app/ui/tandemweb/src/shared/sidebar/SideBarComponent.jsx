import './SideBarComponent.css';
import tandem_logo from '../../assets/icons/tandemlogo.png';
import home_logo from '../../assets/icons/home.png';
import logout_logo from '../../assets/icons/angle-left.png';
import users_logo from '../../assets/icons/users.png';
import search_logo from '../../assets/icons/search.png';
import { Link } from 'react-router-dom';
import { useEffect, useState } from 'react';

const SideBarComponent = () => {

    const [currentUser, setCurrentUser] = useState(null);

    useEffect(() => {
        setCurrentUser(localStorage.getItem("userLogin"));
    }, []);

    return (
        <div className="sidebar">
            <div className="logo">
                <img src={tandem_logo} alt="Tandem Logo" />
            </div>
            <div className="sidebar-menu">
                <ul className="sidebar-menu-list">
                    <li><Link to={`/profile?login=${currentUser}`}><img src={home_logo} alt="Home" />Home</Link></li>
                    <li><Link to={"/search"}><img src={search_logo} alt="Search" />Search</Link></li>
                    <li><Link to={"/groups"}><img src={users_logo} alt="Users" />Groups</Link></li>
                </ul>
                <ul className="sidebar-menu-bottom">
                    <li><Link to="/login"><img src={logout_logo} alt="Logout" />Logout</Link></li>
                </ul>
            </div>
        </div>
    );
};

export default SideBarComponent;
