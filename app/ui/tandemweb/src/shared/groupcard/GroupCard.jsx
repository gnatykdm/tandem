import './GroupCard.css';
import PropTypes from 'prop-types';

const GroupCard = ({ groupname, groupicon, groupdescription, groupusers }) => {
    return (
        <div className="group_card" aria-label={`Group card for ${groupname}`}>
            <div className="group_left">
                <img
                    src={groupicon}
                    alt={`${groupname} icon`}
                    className="group_icon"
                />
                <span className="group_name">{groupname}</span>
            </div>
            <div className="group_right">
                <span className="group_description">
                    {groupdescription || "No description provided."}
                </span>
                <span className="group_users">
                    {groupusers} {parseInt(groupusers) === 1 ? 'member' : 'members'}
                </span>
            </div>
        </div>
    );
};

GroupCard.propTypes = {
    groupname: PropTypes.string.isRequired,
    groupicon: PropTypes.string.isRequired,
    groupdescription: PropTypes.string,
    groupusers: PropTypes.string.isRequired,
};

GroupCard.defaultProps = {
    groupdescription: '',
};

export default GroupCard;
