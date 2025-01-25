import { useState, useEffect } from "react";
import axios from "axios";
import "./Group.css";
import logo from "../../assets/icons/tandemlogo.png";
import message_logo from "../../assets/icons/message.png";
import people_logo from "../../assets/icons/groups.png";
import GroupContent from "../groupcontent/GroupContent";
import PropTypes from "prop-types";

const Group = ({ groupName, currentUserLogin }) => {
    const [activeTab, setActiveTab] = useState("people");
    const [groupData, setGroupData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [memberCount, setMemberCount] = useState(0);
    const [adminStatus, setAdminStatus] = useState(false);
    const [isMember, setIsMember] = useState(false);

    // Fetch group details
    useEffect(() => {
        const fetchGroupData = async () => {
            try {
                const response = await axios.get(
                    `http://localhost:8080/api/v1/group/get_by_name/${groupName}`
                );
                setGroupData(response.data);
            } catch (err) {
                console.error("Error fetching group data:", err);
                setError("Failed to fetch group data");
            } finally {
                setLoading(false);
            }
        };

        fetchGroupData();
    }, [groupName]);

    // Fetch member count and check if the user is a member
    useEffect(() => {
        const fetchMemberCountAndCheckMembership = async () => {
            try {
                const response = await axios.get(
                    `http://localhost:8080/api/v1/group/get_users/${groupName}`
                );
                const members = response.data;
                setMemberCount(members.length);

                // Check if the current user is in the group
                const isUserMember = members.some(
                    (member) => member[1] === currentUserLogin // Assuming member[1] contains the user login
                );
                setIsMember(isUserMember);
            } catch (err) {
                console.error("Error fetching group members:", err);
            }
        };

        fetchMemberCountAndCheckMembership();
    }, [groupName, currentUserLogin]);

    // Fetch admin rights
    useEffect(() => {
        const fetchAdminRights = async () => {
            try {
                const response = await axios.get(
                    `http://localhost:8080/api/v1/group/is_user_admin/${currentUserLogin}`,
                    {
                        params: {
                            groupName: groupName,
                        },
                    }
                );
                setAdminStatus(response.data); // `true` or `false`
            } catch (err) {
                console.error("Error checking admin status:", err);
            }
        };

        fetchAdminRights();
    }, [groupName, currentUserLogin]);

    const handleTabChange = (tab) => {
        setActiveTab(tab);
    };

    const handleJoinGroup = async () => {
        try {
            await axios.post(
                `http://localhost:8080/api/v1/group/add_user/${groupName}`,
                null,
                {
                    params: { userName: currentUserLogin },
                }
            );
            alert("Joined the group successfully!");
            setIsMember(true);
            setMemberCount((prev) => prev + 1);
        } catch (err) {
            console.error("Error joining the group:", err);
            alert("Failed to join the group");
        }
    };

    const handleLeaveGroup = async () => {
        try {
            await axios.delete(
                `http://localhost:8080/api/v1/group/remove_user/${groupName}`,
                {
                    params: { userName: currentUserLogin },
                }
            );
            alert("Left the group successfully!");
            setIsMember(false);
            setMemberCount((prev) => prev - 1);
        } catch (err) {
            console.error("Error leaving the group:", err);
            alert("Failed to leave the group");
        }
    };

    const handleDeleteGroup = async () => {
        try {
            await axios.delete(
                `http://localhost:8080/api/v1/group/delete/${groupName}`
            );
            alert("Group deleted successfully!");
            // Redirect or handle group deletion
        } catch (err) {
            console.error("Error deleting the group:", err);
            alert("Failed to delete the group");
        }
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div className="group">
            <div className="group-header">
                <div className="info-block">
                    <img
                        src={groupData.groupIcon || logo} // Use the group icon if available, fallback to default logo
                        alt="Group Icon"
                        className="group-icon"
                    />
                    <div className="group-details">
                        <h1 className="group-name">{groupData.groupName}</h1>
                        <p className="group-description">{groupData.groupDescription}</p>
                    </div>
                </div>
                <div className="group-stats">
                    <span className="members-count">Members: {memberCount}</span> {/* Dynamically set member count */}
                    {adminStatus ? (
                        <button className="delete-button" onClick={handleDeleteGroup}>
                            Delete Group
                        </button>
                    ) : isMember ? (
                        <button className="leave-button" onClick={handleLeaveGroup}>
                            Leave Group
                        </button>
                    ) : (
                        <button className="join-button" onClick={handleJoinGroup}>
                            Join Group
                        </button>
                    )}
                </div>
            </div>

            <hr className="divider" />

            <div className="group-footer">
                <nav className="group-nav">
                    <button
                        className={`nav-button ${activeTab === "messages" ? "active" : ""}`}
                        onClick={() => handleTabChange("messages")}
                    >
                        <img src={message_logo} alt="Message" className="nav-icon" />
                        <span>Messages</span>
                    </button>
                    <button
                        className={`nav-button ${activeTab === "people" ? "active" : ""}`}
                        onClick={() => handleTabChange("people")}
                    >
                        <img src={people_logo} alt="People" className="nav-icon" />
                        <span>Members</span>
                    </button>
                </nav>
            </div>

            <GroupContent activeTab={activeTab} currentUserLogin={currentUserLogin} groupName={groupName}  />
        </div>
    );
};

Group.propTypes = {
    groupName: PropTypes.string.isRequired,
    currentUserLogin: PropTypes.string.isRequired,
};

export default Group;
