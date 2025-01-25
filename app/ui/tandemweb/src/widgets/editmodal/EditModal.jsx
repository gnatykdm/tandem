import { useState } from "react";
import ReactDOM from "react-dom";
import axios from "axios";
import "./EditModal.css";

const EditModal = ({ onClose, userLogin, initialUsername, initialBio }) => {
  const [formData, setFormData] = useState({
    username: initialUsername || "",
    bio: initialBio || "",
    photo: null, 
  });

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: files ? files[0] : value, 
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { username, bio, photo } = formData;

    const formDataToSend = new FormData();
    if (photo) {
      formDataToSend.append('file', photo);
    }

    try {
      let iconUpdated = false;
      if (photo) {
        const iconResponse = await axios.put(
          `http://localhost:8080/api/v1/content/change_icon/${userLogin}`,
          formDataToSend,
          {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          }
        );

        if (iconResponse.status === 200) {
          console.log("Profile picture updated.");
          iconUpdated = true;
        } else {
          alert("Error updating profile picture.");
          console.error("Profile picture update failed:", iconResponse.statusText);
        }
      } else {
        iconUpdated = true;
      }

      if (iconUpdated) {
        const updateUserDTO = {
          username: username || undefined,
          about: bio || undefined,
        };

        const response = await axios.put(
          `http://localhost:8080/api/v1/user/update/${userLogin}`,
          updateUserDTO,
          {
            headers: {
              'Content-Type': 'application/json',
            },
          }
        );

        if (response.status === 200) {
          onClose();
          alert(`User data for ${userLogin} has been updated.`);
        } else {
          const errorData = response.data;
          alert("Problem with updating user data.");
          console.error("Update failed with status:", response.status, errorData);
        }
      } else {
        alert("Profile picture update failed. User data not updated.");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

return ReactDOM.createPortal(
  <div className="edit__modal-overlay" onClick={onClose}>
    <div className="edit__modal" onClick={(e) => e.stopPropagation()}>
      <button className="close-btn" onClick={onClose}>
        &times;
      </button>
      <form onSubmit={handleSubmit}>
        <div className="photo-upload-section">
          <label htmlFor="photo" className="photo-upload-label">
            Change Profile Photo
          </label>
          <input
            type="file"
            id="photo"
            name="photo"
            accept="image/*"
            onChange={handleChange}
          />
        </div>
        <div className="edit__main-data">
          <input
            type="text"
            name="username"
            placeholder="Edit username"
            value={formData.username}
            onChange={handleChange}
          />
          <textarea
            name="bio"
            placeholder="Edit bio"
            rows="5"
            className="bio-textarea"
            value={formData.bio}
            onChange={handleChange}
          ></textarea>
        </div>
        <button type="submit" className="submit-btn">
          Save Changes
        </button>
      </form>
    </div>
  </div>,
  document.body
);

}
export default EditModal;
