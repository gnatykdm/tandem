import { useState, useRef } from 'react';
import PropTypes from 'prop-types';
import playIcon from '../../assets/icons/play.png';
import pauseIcon from '../../assets/icons/pause.png';
import './AudioComponent.css';

const AudioComponent = ({ tracks, currentAudio, isPlaying, setIsPlaying, setCurrentAudio, setTracks, currentUserLogin, userLogin }) => {
  const [progress, setProgress] = useState({});
  const [durations, setDurations] = useState({});
  const audioRefs = useRef([]);

  const backendUrl = import.meta.env.VITE_BACKEND_URL;

  const togglePlayPause = (index) => {
    const selectedAudio = audioRefs.current[index];

    if (isPlaying && currentAudio === index) {
      selectedAudio.pause();
      setIsPlaying(false);
    } else {
      if (currentAudio !== index) {
        if (currentAudio !== null && audioRefs.current[currentAudio]) {
          audioRefs.current[currentAudio].pause();
        }
        setCurrentAudio(index);
      }
      selectedAudio.play();
      setIsPlaying(true);
    }
  };

  const handleTimeUpdate = (index) => {
    const selectedAudio = audioRefs.current[index];
    if (selectedAudio) {
      const progress = (selectedAudio.currentTime / selectedAudio.duration) * 100;
      setProgress((prevProgress) => ({
        ...prevProgress,
        [index]: progress,
      }));
    }
  };

  const handleSeek = (e) => {
    const selectedAudio = audioRefs.current[currentAudio];
    if (selectedAudio) {
      const newTime = (e.target.value / 100) * selectedAudio.duration;
      selectedAudio.currentTime = newTime;
      setProgress((prevProgress) => ({
        ...prevProgress,
        [currentAudio]: e.target.value,
      }));
    }
  };

  const handleLoadedMetadata = (index) => {
    const selectedAudio = audioRefs.current[index];
    if (selectedAudio) {
      const duration = selectedAudio.duration;
      setDurations((prevState) => ({
        ...prevState,
        [index]: formatDuration(duration),
      }));
    }
  };

  const formatDuration = (duration) => {
    const minutes = Math.floor(duration / 60);
    const seconds = Math.floor(duration % 60);
    return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
  };

  const getFileNameFromUrl = (url) => {
    const fileName = url.split('/').pop();
    const cleanName = decodeURIComponent(fileName)
      .replace(/^(\d+)(-|_)/, '') 
      .replace(/\.[^/.]+$/, ''); 
    return cleanName;
  };

  const handleDelete = async (id) => {
    const trackId = Number(id);  

    if (isNaN(trackId)) {
      alert('Invalid track ID');
      return;
    }

    try {
      const response = await fetch(`${backendUrl}/api/v1/content/delete_audio/${trackId}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        const result = await response.text();
        alert(result); 
        setTracks((prevTracks) => prevTracks.filter((track) => track.audioId !== trackId)); 
      } else {
        const errorText = await response.text();
        alert(`Error: ${errorText}`);
      }
    } catch (error) {
      console.error('Error deleting audio:', error);
      alert('An error occurred while deleting the audio.');
    }
  };

  return (
    <div className="audio-list">
      {tracks.length === 0 ? (
        <center><div className="empty-tracks-message">You dont have any tracks</div></center>
      ) : (
        tracks.map((track, index) => (
          <div key={track.audioId} className="audio-item">
            <div className="audio-info">
              <h3 className="audio-title">
                {track.title || getFileNameFromUrl(track.audioUrl)}
              </h3>
              <p className="audio-duration">{durations[index] || '00:00'}</p>
            </div>

            <button
              className={`play-pause-btn ${isPlaying && currentAudio === index ? 'playing' : ''}`}
              onClick={() => togglePlayPause(index)}
            >
              {isPlaying && currentAudio === index ? (
                <img src={pauseIcon} height={20} width={20} alt="Pause" />
              ) : (
                <img src={playIcon} height={20} width={20} alt="Play" />
              )}
            </button>

            {track.audioId && !isNaN(track.audioId) && (
              userLogin === currentUserLogin ? (
                <button className="delete-btn" onClick={() => handleDelete(track.audioId)}>
                  Delete
                </button>
              ) : null
            )}

            <audio
              ref={(el) => (audioRefs.current[index] = el)}
              className="audio-player"
              onTimeUpdate={() => handleTimeUpdate(index)}
              onLoadedMetadata={() => handleLoadedMetadata(index)}
            >
              <source src={track.audioUrl} type="audio/mp3" />
              Your browser does not support the audio element.
            </audio>

            {currentAudio === index && (
              <input
                type="range"
                className="progress-bar"
                value={progress[index] || 0}
                onChange={handleSeek}
                min="0"
                max="100"
              />
            )}
          </div>
        ))
      )}
    </div>
  );
};

AudioComponent.propTypes = {
  tracks: PropTypes.arrayOf(
    PropTypes.shape({
      audioId: PropTypes.number.isRequired,
      title: PropTypes.string,
      audioUrl: PropTypes.string.isRequired,
      currentUserLogin: PropTypes.string.isRequired,
      userLogin: PropTypes.string.isRequired
    })
  ).isRequired,
  currentAudio: PropTypes.number.isRequired,
  isPlaying: PropTypes.bool.isRequired,
  setIsPlaying: PropTypes.func.isRequired,
  setCurrentAudio: PropTypes.func.isRequired,
  setTracks: PropTypes.func.isRequired,
  currentUserLogin: PropTypes.string.isRequired,
  userLogin: PropTypes.string.isRequired
};

export default AudioComponent;