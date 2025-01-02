import ReactDOM from "react-dom";
import "./PostModal.css";

const PostModal = ({ onClose }) => {
  return ReactDOM.createPortal(
    <div className="post__modal-overlay" onClick={onClose}>
      <div
        className="post__modal"
        onClick={(e) => e.stopPropagation()} 
      >
        <button className="close-btn" onClick={onClose}>
          &times;
        </button>
        <form>
          <div className="upload-section">
            <label htmlFor="file-upload" className="file-upload-label">
              Upload File
            </label>
            <input type="file" id="file-upload" name="file" />
          </div>
          <div className="post-description">
            <label htmlFor="description" className="description-label">
              Add a Description
            </label>
            <textarea
              id="description"
              placeholder="Write something about your post..."
              rows="5"
              className="description-textarea"
            ></textarea>
          </div>
          <button type="submit" className="submit-btn">
            Post
          </button>
        </form>
      </div>
    </div>,
    document.body // Render into <body> using React Portal
  );
};

export default PostModal;
