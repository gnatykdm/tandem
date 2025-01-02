import ReactDOM from "react-dom";
import "./EditModal.css";

const EditModal = ({ onClose }) => {
  return ReactDOM.createPortal(
    <div className="edit__modal-overlay" onClick={onClose}>
      <div
        className="edit__modal"
        onClick={(e) => e.stopPropagation()}
      >
        <button className="close-btn" onClick={onClose}>
          &times;
        </button>
        <form>
          <div className="photo-upload-section">
            <label htmlFor="photo" className="photo-upload-label">
              Change Profile Photo
            </label>
            <input type="file" id="photo" name="photo" />
          </div>
          <div className="edit__main-data">
            <input type="text" placeholder="Edit username" />
            <textarea 
              placeholder="Edit bio" 
              rows="5"
              className="bio-textarea" 
            ></textarea>
            <input type="email" placeholder="Edit email" />
          </div>
          <button type="submit" className="submit-btn">
            Save Changes
          </button>
        </form>
      </div>
    </div>,
    document.body // Render into <body> using React Portal
  );
};

export default EditModal;
