import './ContentComponent.css';
import { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import audioLogo from '../../assets/icons/audio.png';
import videoLogo from '../../assets/icons/media.png';
import plusLogo from '../../assets/icons/square-plus.png';
import AudioComponent from '../../shared/audio/AudioComponent';
import VideoPhotoComponent from '../../shared/video/VideoPhotoComponent';
import axios from 'axios';

const ContentComponent = ({ userLogin, currentUserLogin }) => {
  const [selectedContent, setSelectedContent] = useState('audio');
  const [currentAudio, setCurrentAudio] = useState(null);
  const [isAudioPlaying, setIsAudioPlaying] = useState(false);
  const [audioTracks, setAudioTracks] = useState([]);
  const [photoItems, setPhotoItems] = useState([]);  
  const [videoItems, setVideoItems] = useState([]);  
  const [error, setError] = useState(null);

  const backendUrl = import.meta.env.VITE_BACKEND_URL;

  const contentOptions = [
    { type: 'audio', logo: audioLogo, label: 'Audio' },
    { type: 'video', logo: videoLogo, label: 'Media' }
  ];

  useEffect(() => {
    const fetchContentData = async () => {

      try {
        const audioResponse = await axios.get(`${backendUrl}/api/v1/content/get_all_audio_by_id/${userLogin}`, {
          
        });
        if (Array.isArray(audioResponse.data)) {
          setAudioTracks(audioResponse.data); 
        }

        const videoResponse = await axios.get(`${backendUrl}/api/v1/content/get_all_videos_by_id/${userLogin}`, {
        });
        if (Array.isArray(videoResponse.data)) {
          setVideoItems(videoResponse.data);  
        }

        const photoResponse = await axios.get(`${backendUrl}/api/v1/content/get_all_photos_by_id/${userLogin}`, {
        });
        if (Array.isArray(photoResponse.data)) {
          setPhotoItems(photoResponse.data); 
        }
      } catch (error) {
        setError('Error fetching content');
        console.error('Error fetching content:', error);
      }
    };

    if (userLogin) {
      fetchContentData();
    }
  }, [userLogin]);

  const handleContentChange = (contentType) => {
    setSelectedContent(contentType);
  };

  const handleFileUpload = async (event) => {
    const file = event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('description', 'Uploaded content'); 

      try {
        if (file.type.startsWith("audio/")) {
          const response = await axios.post(`${backendUrl}/api/v1/content/add_audio/${userLogin}`, formData, {
        
          });
          alert(response.data);
        } else if (file.type.startsWith("video/")) {
          const response = await axios.post(`${backendUrl}/api/v1/content/add_video/${userLogin}`, formData, {
            headers: { 
              'Content-Type': 'multipart/form-data',
            }
          });
          alert(response.data);
        } else if (file.type.startsWith("image/")) {
          const response = await axios.post(`${backendUrl}/api/v1/content/add_photo/${userLogin}`, formData, {
            headers: { 
              'Content-Type': 'multipart/form-data',
            }
          });
          alert(response.data);
        } else {
          alert("Unsupported file type!");
        }
      } catch (error) {
        console.error('Error uploading file:', error);
        alert('Failed to upload content');
      }
    }
  };

  const renderContent = () => {
    switch (selectedContent) {
      case 'audio':
        return <AudioComponent 
                  tracks={audioTracks} 
                  currentAudio={currentAudio} 
                  isPlaying={isAudioPlaying} 
                  setIsPlaying={setIsAudioPlaying} 
                  setCurrentAudio={setCurrentAudio}
                  currentUserLogin={currentUserLogin} 
                  userLogin={userLogin} />;
      case 'video':
        return <VideoPhotoComponent 
                  mediaItems={[...videoItems, ...photoItems]} 
                  currentUserLogin={currentUserLogin} 
                  userLogin={userLogin} />;  
      default:
        return null;
    }
  };

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="content">
      <div className="content-type">
        {currentUserLogin === userLogin ? (
          <button className="plus-content-button">
            <label>
              <img src={plusLogo} alt="Upload File" />
              <input type="file" style={{ display: 'none' }} onChange={handleFileUpload} />
            </label>
          </button>
        ) : null}

        {contentOptions.map(({ type, logo, label }) => (
          <div key={type} className={`content-item ${selectedContent === type ? 'active' : ''}`} onClick={() => handleContentChange(type)}>
            <img src={logo} alt={label} style={{ transform: selectedContent === type ? 'scale(1.2)' : 'scale(1)' }} />
            {label}
          </div>
        ))}
      </div>
      <div className="content-display">{renderContent()}</div>
    </div>
  );
};

ContentComponent.propTypes = {
  userLogin: PropTypes.string.isRequired,
  currentUserLogin: PropTypes.string.isRequired
};

export default ContentComponent;
