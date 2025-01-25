import './SearchPage.css';
import UserDisplay from '../../widgets/userdisplay/UserDisplay';
import SideBarComponent from '../../shared/sidebar/SideBarComponent';

const SearchPage = () => {  
    return (
        <div className="search__page">
            <div className="profile-sidebar">
                <SideBarComponent />
            </div>
            <div className="search-content">
                <UserDisplay/>
            </div>
        </div>
    );
};

export default SearchPage;
