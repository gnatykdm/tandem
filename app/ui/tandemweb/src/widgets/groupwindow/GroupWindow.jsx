import { useEffect, useState } from 'react';
import './GroupWindow.css';
import PropTypes from 'prop-types';
import CreateGroupModal from '../creategroupmodal/CreateGroupModal';
import GroupCard from '../../shared/groupcard/GroupCard';
import { Link, useNavigate } from 'react-router-dom';

const GroupWindow = ({ currentLogin }) => {
    const [groups, setGroups] = useState([]); 
    const [isLoading, setIsLoading] = useState(true); 
    const [error, setError] = useState(null); 
    const [isModalOpen, setIsModalOpen] = useState(false); 
    const [showAccessModal, setShowAccessModal] = useState(false);
    const [selectedGroup, setSelectedGroup] = useState(null);
    const [accessCode, setAccessCode] = useState("");
    const [accessError, setAccessError] = useState(null);
    const navigate = useNavigate();

    const fetchGroups = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/v1/group/get_all');
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json(); 
            setGroups(data); 
        } catch (err) {
            setError(err.message); 
        } finally {
            setIsLoading(false); 
        }
    };

    useEffect(() => {
        fetchGroups(); 
    }, []); 

    const handleModalClose = () => {
        setIsModalOpen(false);
        fetchGroups(); 
    };

    const handleGroupClick = (group) => {
        if (group.type) { // If the group is private
            setSelectedGroup(group);
            setShowAccessModal(true);
        } else {
            navigate(`/group?name=${group.groupName}`);
        }
    };

    const handleAccessSubmit = async (e) => {
        e.preventDefault();
        setAccessError(null);

            const response = await fetch(`/check_access_code/${selectedGroup.groupName}?accessCode=${accessCode}`);
            if (response.ok) {
                const isValid = await response.json();
                if (isValid) {
                    navigate(`/group?name=${selectedGroup.groupName}`);
                } else {
                    setAccessError("Invalid access code. Please try again.");
                }
            } else {
                const errorMessage = await response.text();
                setAccessError(errorMessage || "An error occurred. Please try again later.");
            }
    };

    return (
        <div className="group_window">
            <div className="main-nav-group">
                <span>Welcome to Group Section: @{currentLogin}</span>
                <div className="group_btn">
                    <button onClick={() => setIsModalOpen(true)}>Create Group</button>
                </div>
            </div>
            <div className="groups_items">
                {isLoading ? (
                    <center>Loading...</center>
                ) : error ? (
                    <center>Error: {error}</center> 
                ) : groups.length === 0 ? (
                    <center>No groups found!</center> 
                ) : (
                    groups.map((group) => (
                        <center key={group.id}>
                            <div onClick={() => handleGroupClick(group)} style={{ cursor: "pointer" }}>
                                <Link to={`/group?name=${group.groupName}`}>
                                <GroupCard
                                    groupname={group.groupName}
                                    groupicon={group.groupIcon}
                                    groupdescription={group.groupDescription}
                                    groupusers={group.accessCode} 
                                />
                                </Link>
                            </div>
                        </center>
                    ))
                )}
            </div>
            {isModalOpen && (
                <CreateGroupModal 
                    currentLogin={currentLogin} 
                    onClose={handleModalClose} 
                />
            )}
            {showAccessModal && (
                <div className="modal-access-overlay">
                    <form onSubmit={handleAccessSubmit} className="modal-form">
                        <h2>Enter Access Code for {selectedGroup.groupName}</h2>
                        <input
                            type="text"
                            placeholder="Access Code"
                            value={accessCode}
                            onChange={(e) => setAccessCode(e.target.value)}
                            required
                        />
                        <button type="submit">Submit</button>
                        {accessError && <p className="error-message">{accessError}</p>}
                        <button type="button" onClick={() => setShowAccessModal(false)}>
                            Cancel
                        </button>
                    </form>
                </div>
            )}
        </div>
    );
};

GroupWindow.propTypes = {
    currentLogin: PropTypes.string.isRequired,
};

export default GroupWindow;
