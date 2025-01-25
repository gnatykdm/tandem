import { useEffect, useState } from 'react';
import './UserDisplay.css';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import UserSearchCard from '../../shared/usersearchcard/UserSearchCard';
import UserSearch from '../../shared/search/UserSearch';

const UserDisplay = () => {
    const [users, setUsers] = useState([]); 
    const [filteredUsers, setFilteredUsers] = useState([]); 
    const [postCounts, setPostCounts] = useState({}); 
    const [followers, setFollowers] = useState({}); 
    const [following, setFollowing] = useState({}); 
    const [noUserFound, setNoUserFound] = useState(false);
    const [currentUserLogin, setCurrentUserLogin] = useState(null); 

    useEffect(() => {
        setCurrentUserLogin(localStorage.getItem("userLogin"));
        const fetchUsers = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/v1/user/find_all');
                const data = await response.json();

                if (Array.isArray(data)) {
                    setUsers(data);
                    setFilteredUsers(data.filter(user => user.login !== currentUserLogin)); 
                    data.forEach(user => {
                        fetchPostCount(user.login);
                        fetchFollowers(user.login);
                        fetchFollowing(user.login);
                    });
                }
            } catch (error) {
                console.error("Error fetching users:", error);
            }
        };

        fetchUsers();
    }, [currentUserLogin]);

    const fetchPostCount = async (login) => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/user/get_post_count/${login}`);
            const data = await response.json();
            setPostCounts((prev) => ({ ...prev, [login]: data }));
        } catch (error) {
            console.error(`Error fetching post count for ${login}:`, error);
        }
    };

    const fetchFollowers = async (login) => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/follows/get_followers/${login}`);
            const data = await response.json();
            setFollowers((prev) => ({ ...prev, [login]: data.length }));
        } catch (error) {
            console.error(`Error fetching followers for ${login}:`, error);
        }
    };

    const fetchFollowing = async (login) => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/follows/get_following/${login}`);
            const data = await response.json();
            setFollowing((prev) => ({ ...prev, [login]: data.length }));
        } catch (error) {
            console.error(`Error fetching following for ${login}:`, error);
        }
    };

    const handleSearch = (query) => {
        if (query.trim() === "") {
            setFilteredUsers(users); 
            setNoUserFound(false); 
        } else {
            const filtered = users.filter(user =>
                user.username.toLowerCase().includes(query.toLowerCase()) ||
                user.login.toLowerCase().includes(query.toLowerCase())
            );
            setFilteredUsers(filtered); 
            setNoUserFound(filtered.length === 0); 
        }
    };

    return (
        <div className={"user-display__overlay"}>
            <UserSearch onSearch={handleSearch} />
            <div className="user-card__window">
                <div className="users-cards">
                    {noUserFound ? (
                        <center>
                            <p className={"user-not-found"}>No user found with this login</p>
                        </center>
                    ) : (
                        filteredUsers.map((user) => (
                            <center key={user.login}>
                                <Link to={`/profile?login=${user.login}`}>
                                    <UserSearchCard
                                        userImage={user.profileImage}
                                        userLogin={user.login}
                                        userName={user.username}
                                        userPosts={postCounts[user.login] || 0}  
                                        userFollowers={followers[user.login] || 0} 
                                        userFollowing={following[user.login] || 0}  
                                    />
                                </Link>
                            </center>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

UserDisplay.propTypes = {
    onSearch: PropTypes.func.isRequired,
};

export default UserDisplay;
