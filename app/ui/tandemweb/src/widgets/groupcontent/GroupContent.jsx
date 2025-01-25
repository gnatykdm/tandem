import './GroupContent.css';
import Messages from '../messages/Messages';
import GroupMembers from '../groupmembers/GroupMembers';
import PropTypes from 'prop-types';

const GroupContent = ({ activeTab, currentUserLogin, groupName }) => {
    return (
        <div className="group-content">
            {activeTab === "messages" && <Messages currentUserLogin={currentUserLogin} currentGroupName={groupName}/>}
            {activeTab === "people" && <GroupMembers groupName={groupName} currentUserLogin={currentUserLogin}/>}
        </div>
    );
}

GroupContent.propTypes = {
    activeTab: PropTypes.oneOf(['messages', 'people']).isRequired,
    currentUserLogin: PropTypes.string.isRequired,
    groupName: PropTypes.string.isRequired
};

export default GroupContent;
