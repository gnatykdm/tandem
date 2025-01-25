import PropTypes from "prop-types";
import defaultLogo from '../../assets/icons/userlogo.png';
import "./UserSearchCard.css";

const UserSearchCard = ({ userImage, userLogin, userName, userFollowers, userFollowing, userPosts }) => {
    return (
        <div className={"user-search__card"}>
            <img
                src={userImage || defaultLogo}  
                alt="User Icon"
            />
            <div className="user-search-card__">
                <span className="user-login">@{userLogin}</span> 
                <span className="user-username">{userName}</span> 
            </div>
            <div className="user-search">
                <span>Followers: {userFollowers}</span> 
                <span>Following: {userFollowing}</span> 
                <span>Posts: {userPosts}</span> 
            </div>
        </div>
    );
};

UserSearchCard.propTypes = {
    userImage: PropTypes.string,
    userLogin: PropTypes.string.isRequired,
    userName: PropTypes.string.isRequired,
    userFollowers: PropTypes.number.isRequired,
    userFollowing: PropTypes.number.isRequired,
    userPosts: PropTypes.number.isRequired,
};

export default UserSearchCard;
