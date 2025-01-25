import { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";
import UserSearchCard from "../../shared/usersearchcard/UserSearchCard";
import "./GroupMembers.css";

const GroupMembers = ({ groupName }) => {
    const [groupUsers, setGroupUsers] = useState([]); // Store the list of users in the group
    const [usersDetails, setUsersDetails] = useState({}); // Store user details mapped by userId
    const [loading, setLoading] = useState(true);

    // Fetch user details including followers, following, and post counts by userId
    const fetchUserDetailsById = async (userId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/user/find_by_id/${userId}`);
            const userData = await response.json();
            if (!response.ok) {
                throw new Error(userData); // Error if user data is not found
            }

            // Extract the userLogin to fetch followers, following, and posts
            const { login: userLogin } = userData;

            // Fetch followers, following, and post counts based on userLogin
            const followersResponse = await fetch(`http://localhost:8080/api/v1/follows/get_followers/${userLogin}`);
            const followersData = await followersResponse.json();

            const followingResponse = await fetch(`http://localhost:8080/api/v1/follows/get_following/${userLogin}`);
            const followingData = await followingResponse.json();

            const postsResponse = await fetch(`http://localhost:8080/api/v1/user/get_post_count/${userLogin}`);
            const postsCount = await postsResponse.json();

            return {
                ...userData, // Spread user data from the find_by_id query
                followers: Array.isArray(followersData) ? followersData.length : 0,
                following: Array.isArray(followingData) ? followingData.length : 0,
                posts: postsCount,
            };
        } catch (error) {
            console.error(`Error fetching user details for userId ${userId}:`, error);
            return null;
        }
    };

    useEffect(() => {
        const fetchGroupUsers = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/v1/group/get_users/${groupName}`);
                const usersData = await response.json();

                const users = {}; // This will map userId to userDetails
                const userDetailsPromises = usersData.map(async (userData) => {
                    const [userId] = userData; // Extract userId from usersData (first element)
                    const userDetails = await fetchUserDetailsById(userId); // Fetch details by userId

                    if (userDetails) {
                        users[userId] = userDetails; // Store the user details by userId
                    }
                });

                // Wait for all user details to be fetched
                await Promise.all(userDetailsPromises);

                setGroupUsers(usersData); // Store the group users data (array of [userId, userLogin])
                setUsersDetails(users); // Store the detailed user information in usersDetails
                setLoading(false);
            } catch (error) {
                console.error("Error fetching group users:", error);
                setLoading(false);
            }
        };

        fetchGroupUsers();
    }, [groupName]);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div className="group-members-container">
            <div className="group-members-list">
                {groupUsers.map((userData) => {
                    const [userId] = userData; 
                    const userDetails = usersDetails[userId]; // Get the user details from the usersDetails object

                    if (userDetails) {
                        return (
                            <center key={userId}>
                                <Link to={`/profile?login=${userDetails.login}`}>
                                <UserSearchCard
                                userImage={userDetails.profileImage}
                                userLogin={userDetails.login}
                                userName={userDetails.username}
                                userFollowers={userDetails.followers}
                                userFollowing={userDetails.following}
                                userPosts={userDetails.posts}
                                />
                                </Link>
                            </center>
                        );
                    }
                    return null; // Return null if the userDetails are not available
                })}
            </div>
        </div>
    );
};

GroupMembers.propTypes = {
    groupName: PropTypes.string.isRequired,
};

export default GroupMembers;
