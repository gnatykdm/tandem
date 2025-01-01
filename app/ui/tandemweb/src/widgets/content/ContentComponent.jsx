import './ContentComponent.css';
import { useState } from 'react';
import audioLogo from '../../assets/icons/audio.png';
import videoLogo from '../../assets/icons/media.png';
import groupLogo from '../../assets/icons/users.png';
import plusLogo from '../../assets/icons/square-plus.png';
import audioFile1 from '../../assets/music/music1.mp3';
import audioFile2 from '../../assets/music/music4.mp3';
import audioFile3 from '../../assets/music/music6.mp3';
import audioFile4 from '../../assets/music/music5.mp3';
import audioFile5 from '../../assets/music/music2.mp3';
import audioFile6 from '../../assets/music/music1.mp3';
import photo1 from '../../assets/images/jenner.jpg';
import photo2 from '../../assets/images/musk.jpg';
import AudioComponent from '../../shared/audio/AudioComponent';
import VideoPhotoComponent from '../../shared/video/VideoPhotoComponent';

const ContentComponent = () => {
  const [selectedContent, setSelectedContent] = useState('audio');
  const [currentAudio, setCurrentAudio] = useState(audioFile1);
  const [isAudioPlaying, setIsAudioPlaying] = useState(false);

  const [audioTracks, setAudioTracks] = useState([
    { title: 'Track 1 - Music 3', duration: '4:23', src: audioFile1 },
    { title: 'Track 2 - Music 4', duration: '3:45', src: audioFile2 },
    { title: 'Track 3 - Music 6', duration: '5:00', src: audioFile3 },
    { title: 'Track 4 - Music 5', duration: '5:30', src: audioFile4 },
    { title: 'Track 2 - Music 4', duration: '3:45', src: audioFile5 },
    { title: 'Track 3 - Music 6', duration: '5:00', src: audioFile6 },
  ]);

  const [mediaItems, setMediaItems] = useState([
    { type: 'photo', src: photo1 },
    { type: 'photo', src: photo2 }
  ]);

  const contentOptions = [
    { type: 'audio', logo: audioLogo, label: 'Audio' },
    { type: 'video', logo: videoLogo, label: 'Media' },
    { type: 'group', logo: groupLogo, label: 'Group' },
  ];

  const handleContentChange = (contentType) => {
    setSelectedContent(contentType);
  };

  const handleFileUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      if (file.type.startsWith("audio/")) {
        const audio = new Audio(URL.createObjectURL(file));
        audio.addEventListener("loadedmetadata", () => {
          const duration = `${Math.floor(audio.duration / 60)}:${Math.floor(audio.duration % 60).toString().padStart(2, "0")}`;
          const newAudio = { title: file.name, duration, src: URL.createObjectURL(file) };
          setAudioTracks((prevTracks) => [...prevTracks, newAudio]);
        });
      } else if (file.type.startsWith("video/")) {
        const newVideo = { type: 'video', src: URL.createObjectURL(file) };
        setMediaItems((prevItems) => [...prevItems, newVideo]);
      } else if (file.type.startsWith("image/")) {
        const newPhoto = { type: 'photo', src: URL.createObjectURL(file) };
        setMediaItems((prevItems) => [...prevItems, newPhoto]);
      } else {
        alert("Unsupported file type!");
      }
    }
  };

  const renderContent = () => {
    switch (selectedContent) {
      case 'audio':
        return <AudioComponent tracks={audioTracks} currentAudio={currentAudio} isPlaying={isAudioPlaying} setIsPlaying={setIsAudioPlaying} setCurrentAudio={setCurrentAudio} />;
      case 'video':
        return <VideoPhotoComponent mediaItems={mediaItems} />;
      case 'group':
        return (
          <div className="group-content">
            <h2>Creative Groups</h2>
            <ul>
              <li>Design Team (12 members)</li>
              <li>Music Production (8 members)</li>
              <li>Film Crew (15 members)</li>
            </ul>
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div className="content">
      <div className="content-type">
      <button className="plus-content-button">
        <label>
          <img src={plusLogo} alt="Upload File" />
          <input type="file" style={{ display: 'none' }} onChange={handleFileUpload} />
        </label>
      </button>
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

export default ContentComponent;
