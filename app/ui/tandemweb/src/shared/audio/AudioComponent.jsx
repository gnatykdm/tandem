import { useState, useRef, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Helmet } from 'react-helmet';
import './AudioComponent.css';

const AudioComponent = ({ tracks }) => {
  const [currentTrack, setCurrentTrack] = useState(0);
  const [playStatus, setPlayStatus] = useState({});
  const [progress, setProgress] = useState(0);
  const [durations, setDurations] = useState({});
  const audioRefs = useRef({});

  const togglePlayPause = (index) => {
    const selectedAudio = audioRefs.current[index];

    if (playStatus[index] === 'Playing') {
      selectedAudio.pause();
      setPlayStatus(prevState => ({ ...prevState, [index]: 'Pause' }));
    } else if (playStatus[index] === 'Pause') {
      selectedAudio.play();
      setPlayStatus(prevState => ({ ...prevState, [index]: 'Playing' }));
    } else {
      Object.keys(playStatus).forEach(key => {
        if (playStatus[key] === 'Playing') {
          audioRefs.current[key]?.pause();
          setPlayStatus(prevState => ({ ...prevState, [key]: 'Play' }));
        }
      });

      selectedAudio.play();
      setPlayStatus(prevState => ({ ...prevState, [index]: 'Playing' }));
      setCurrentTrack(index);
    }
  };

  const handleTimeUpdate = (index) => {
    const selectedAudio = audioRefs.current[index];
    if (selectedAudio) {
      const progress = (selectedAudio.currentTime / selectedAudio.duration) * 100;
      setProgress(progress);
    }
  };

  const handleSeek = (e) => {
    const selectedAudio = audioRefs.current[currentTrack];
    if (selectedAudio) {
      const newTime = (e.target.value / 100) * selectedAudio.duration;
      selectedAudio.currentTime = newTime;
      setProgress(e.target.value);
    }
  };

  useEffect(() => {
    if (currentTrack !== null) {
      const currentAudio = audioRefs.current[currentTrack];
      const handleEnded = () => {
        setPlayStatus(prevState => ({ ...prevState, [currentTrack]: 'Play' }));
        const nextTrack = currentTrack < tracks.length - 1 ? currentTrack + 1 : 0;
        setCurrentTrack(nextTrack);
        togglePlayPause(nextTrack);
      };

      currentAudio.addEventListener('ended', handleEnded);
      return () => {
        currentAudio.removeEventListener('ended', handleEnded);
      };
    }
  }, [currentTrack, tracks.length]);

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

  return (
    <div className="audio-list">
      <Helmet>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
      </Helmet>
      {tracks.map((track, index) => (
        <div key={index} className="audio-item">
          <div className="audio-info">
            <h3 className="audio-title">{track.title}</h3>
            <p className="audio-duration">
              {durations[index] || '00:00'}
            </p>
          </div>
          <button
            className={`play-pause-btn ${playStatus[index] === 'Playing' ? 'playing' : ''}`}
            onClick={() => togglePlayPause(index)}
          >
            {playStatus[index] === 'Playing' ? (
              <i className="fas fa-pause"></i>
            ) : (
              <i className="fas fa-play"></i>
            )}
          </button>
          <audio
            ref={(el) => (audioRefs.current[index] = el)}
            className="audio-player"
            onTimeUpdate={() => handleTimeUpdate(index)}
            onLoadedMetadata={() => handleLoadedMetadata(index)}
          >
            <source src={track.src} type="audio/mp3" />
            Your browser does not support the audio element.
          </audio>
          
          {currentTrack === index && (
            <input
              type="range"
              className="progress-bar"
              value={progress}
              onChange={handleSeek}
              min="0"
              max="100"
            />
          )}
        </div>
      ))}
    </div>
  );
};

AudioComponent.propTypes = {
  tracks: PropTypes.arrayOf(
    PropTypes.shape({
      title: PropTypes.string.isRequired,
      src: PropTypes.string.isRequired,
    })
  ).isRequired,
};

export default AudioComponent;