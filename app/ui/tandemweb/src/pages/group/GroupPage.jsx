import './GroupPage.css';
import { useState, useEffect } from 'react';
import SideBarComponent from '../../shared/sidebar/SideBarComponent';
import GroupDisplay from '../../widgets/groupdisplay/GroupDisplay';

const GroupPage = () => {
    const [currentUserLogin, setCurrentUserLogin] = useState(null);

    useEffect(() => {
        setCurrentUserLogin(localStorage.getItem("userLogin"));
    }, [])

    return (
        <div className="group_page">
            <div className="sidebar-group">
                <SideBarComponent/>
            </div>
           <div className="group-content">
                <GroupDisplay userLogin={currentUserLogin}/>
           </div>
        </div>
    );
}

export default GroupPage;