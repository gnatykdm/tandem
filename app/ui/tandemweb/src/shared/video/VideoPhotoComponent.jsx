import { useState } from 'react';
import PropTypes from 'prop-types';
import './VideoPhotoComponent.css';

const VideoPhotoComponent = ({ mediaItems }) => {
  const [selectedMedia, setSelectedMedia] = useState(null);

  const openModal = (media) => {
    setSelectedMedia(media);
  };

  const closeModal = () => {
    setSelectedMedia(null);
  };

  return (
    <div className="video-photo-gallery">
      {mediaItems.map((item, index) => (
        <div
          key={index}
          className="media-item"
          onClick={() => openModal(item)}
        >
          {item.type === 'video' ? (
            <video src={item.src} muted preload="metadata"></video>
          ) : (
            <div
              className="photo-preview"
              style={{ backgroundImage: `url(${item.src})` }}
            ></div>
          )}
          {item.type === 'video' && <div className="play-icon">▶</div>}
          {item.type === 'audio' && (
            <div className="audio-icon">
              🎵 <span>Audio</span>
            </div>
          )}
        </div>
      ))}

      {selectedMedia && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            {selectedMedia.type === 'photo' && (
              <img src={selectedMedia.src} alt="Selected" />
            )}
            {selectedMedia.type === 'video' && (
              <video
                controls
                autoPlay
                src={selectedMedia.src}
                className="modal-video"
              />
            )}
            {selectedMedia.type === 'audio' && (
              <div className="modal-audio">
                <h2>Playing Audio</h2>
                <audio controls autoPlay src={selectedMedia.src}></audio>
              </div>
            )}
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
      type: PropTypes.oneOf(['photo', 'video', 'audio']).isRequired,
      src: PropTypes.string.isRequired,
    })
  ).isRequired,
};

export default VideoPhotoComponent;