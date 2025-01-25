import { useState } from 'react';
import PropTypes from 'prop-types'; 
import './UserSearch.css';
import searchIcon from '../../assets/icons/search.png';

const UserSearch = ({ onSearch }) => {
    const [searchValue, setSearchValue] = useState(''); 

    const handleInputChange = (e) => {
        setSearchValue(e.target.value);
    };

    const handleSearch = () => {
        onSearch(searchValue); 
    };

    return (
        <div className={"user-search__overlay"}>
            <div className="search-section">
                <img src={searchIcon} alt="Search Icon" />
                <input
                    type="text"
                    placeholder="Search..."
                    value={searchValue}
                    onChange={handleInputChange} 
                />
                <button onClick={handleSearch}>Search</button>
            </div>
        </div>
    );
};

UserSearch.propTypes = {
    onSearch: PropTypes.func.isRequired, 
};

export default UserSearch;
