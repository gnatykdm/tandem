import { useState, useRef } from 'react';
import PropTypes from 'prop-types';
import './VideoPhotoComponent.css';

const VideoPhotoComponent = ({ mediaItems, onDelete, currentUserLogin, userLogin }) => {
  const [selectedMedia, setSelectedMedia] = useState(null);
  const [videoProgress, setVideoProgress] = useState({});
  const videoRefs = useRef([]);

  const openModal = (media) => {
    setSelectedMedia(media);
  };

  const closeModal = () => {
    setSelectedMedia(null);
  };

  const handleDelete = async () => {
    const endpoint = selectedMedia.photoUrl
      ? `http://localhost:8080/api/v1/content/delete_photo/${selectedMedia.photoId}`
      : `http://localhost:8080/api/v1/content/delete_video/${selectedMedia.videoId}`;
    
    const response = await fetch(endpoint, { method: 'DELETE' });
    const result = await response.text();
    alert(result);
    onDelete(selectedMedia);
    closeModal();
  };

  const handleTimeUpdate = (index) => {
    const video = videoRefs.current[index];
    if (video && video.duration > 0) {
      const progress = (video.currentTime / video.duration) * 100;
      setVideoProgress((prev) => ({
        ...prev,
        [index]: progress,
      }));
    }
  };

  const handleSeek = (index, e) => {
    const video = videoRefs.current[index];
    if (video && !video.paused) {
      const newTime = (e.target.value / 100) * video.duration;
      video.currentTime = newTime;
      setVideoProgress((prev) => ({
        ...prev,
        [index]: e.target.value,
      }));
    }
  };

  return (
    <div className="video-photo-gallery">
      {mediaItems.length === 0 ? (
        <center>
          <div className="empty-message">You dont have any media</div>
        </center>
      ) : (
        mediaItems.map((item, index) => (
          <div
            key={index}
            className="media-item"
            onClick={() => openModal(item)} 
          >
            {item.videoUrl ? (
              <div className="video-thumbnail-container">
                <video
                  ref={(el) => {
                    if (el && videoRefs.current.length <= index) {
                      videoRefs.current.push(el);
                    } else if (el) {
                      videoRefs.current[index] = el;
                    }
                  }}
                  src={item.videoUrl}
                  muted
                  preload="metadata"
                  className="video-thumbnail"
                  controls
                  onTimeUpdate={() => handleTimeUpdate(index)}
                />
                <div className="progress-bar-container">
                  <input
                    type="range"
                    className="progress-bar"
                    value={videoProgress[index] || 0}
                    onChange={(e) => handleSeek(index, e)}
                    min="0"
                    max="100"
                  />
                </div>
                <div className="play-icon">▶</div>
              </div>
            ) : item.photoUrl ? (
              <div
                className="photo-preview"
                style={{ backgroundImage: `url(${item.photoUrl})` }}
              />
            ) : null}
          </div>
        ))
      )}

      {selectedMedia && (
        <div className="modal-overlay" onClick={closeModal}>
          <div
            className="modal-content"
            onClick={(e) => e.stopPropagation()} 
          >
            {selectedMedia.photoUrl && (
              <img
                src={selectedMedia.photoUrl}
                alt="Selected"
                className="modal-image"
              />
            )}
            {selectedMedia.videoUrl && (
              <video
                controls
                autoPlay
                ref={(el) => {
                  videoRefs.current[mediaItems.indexOf(selectedMedia)] = el;
                }}
                src={selectedMedia.videoUrl}
                className="modal-video"
              />
            )}

            {currentUserLogin === userLogin ? (
              <button className="delete-btn" onClick={handleDelete}>
                Delete
              </button>
            ) : null}

            <button className="close-btn" onClick={closeModal}>
              ✖
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

VideoPhotoComponent.propTypes = {
  mediaItems: PropTypes.arrayOf(
    PropTypes.shape({
      videoId: PropTypes.number,
      videoUrl: PropTypes.string,
      photoId: PropTypes.number,
      photoUrl: PropTypes.string,
      description: PropTypes.string,
      postAt: PropTypes.instanceOf(Date),
      userId: PropTypes.number,
    })
  ).isRequired,
  onDelete: PropTypes.func.isRequired,
  currentUserLogin: PropTypes.string.isRequired,
  userLogin: PropTypes.string.isRequired,
};

export default VideoPhotoComponent;