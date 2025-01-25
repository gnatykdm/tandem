import './GroupDisplay.css';
import GroupWindow from '../groupwindow/GroupWindow';
import PropTypes from 'prop-types';

const GroupDisplay = ({ userLogin }) => {
    return (
        <div className="group_display">
            <GroupWindow currentLogin={userLogin}/>
        </div>
    );
    }

GroupDisplay.propTypes = {
    userLogin: PropTypes.string.isRequired
}

export default GroupDisplay;