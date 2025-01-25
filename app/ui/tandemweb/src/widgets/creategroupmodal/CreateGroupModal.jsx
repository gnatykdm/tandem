import { useEffect, useState } from 'react';
import './CreateGroupModal.css';
import PropTypes from 'prop-types';
import axios from 'axios';

const CreateGroupModal = ({ currentLogin, onClose }) => {
    const [groupName, setGroupName] = useState('');
    const [groupDescription, setGroupDescription] = useState('');
    const [groupType, setGroupType] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleCreateGroup = async () => {
        try {
            const response = await axios.post(
                `http://localhost:8080/api/v1/group/create/${currentLogin}`,
                null,
                {
                    params: {
                        groupName,
                        groupDescription,
                        groupType,
                    },
                }
            );

            if (response.status === 200) {
                setSuccessMessage(response.data);
                setErrorMessage('');
                setTimeout(onClose, 1500);
            } else {
                setErrorMessage('Failed to create group.');
            }
        } catch (error) {
            setErrorMessage(
                error.response?.data || 'An error occurred while creating the group.'
            );
        }
    };

    useEffect(() => {
        setGroupType(false);
    }, [groupType])

    return (
        <div className="create-group-modal">
            <div className="modal-content">
                <h2>Create New Group</h2>
                {errorMessage && <p className="error-message">{errorMessage}</p>}
                {successMessage && <p className="success-message">{successMessage}</p>}
                <div className="form-group">
                    <label htmlFor="groupName">Group Name:</label>
                    <input
                        type="text"
                        id="groupName"
                        value={groupName}
                        onChange={(e) => setGroupName(e.target.value)}
                        placeholder="Enter group name"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="groupDescription">Group Description:</label>
                    <textarea
                        id="groupDescription"
                        value={groupDescription}
                        onChange={(e) => setGroupDescription(e.target.value)}
                        placeholder="Enter group description"
                    ></textarea>
                </div>
                <div className="modal-buttons">
                    <button className="create-button" onClick={handleCreateGroup}>
                        Create Group
                    </button>
                    <button className="cancel-button" onClick={onClose}>
                        Cancel
                    </button>
                </div>
            </div>
        </div>
    );
};

CreateGroupModal.propTypes = {
    currentLogin: PropTypes.string.isRequired,
    onClose: PropTypes.func.isRequired,
};

export default CreateGroupModal;
