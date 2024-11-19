import './ContentComponent.css';
import audioLogo from '../../shared/assets/icons/audio.png';
import videoLogo from '../../shared/assets/icons/media.png';
import groupLogo from '../../shared/assets/icons/users.png';
import { useState, useEffect } from 'react';

const ContentComponent = () => {
    const [selectedContent, setSelectedContent] = useState('audio');
    const [isTransitioning, setIsTransitioning] = useState(false);

    const handleContentChange = (contentType) => {
        setIsTransitioning(true);
        setSelectedContent(contentType);
        setTimeout(() => setIsTransitioning(false), 300);
    };

    const renderContent = () => {
        const contentMap = {
            audio: {
                title: "Audio Collection",
                items: [
                    { title: "Ambient Waves", duration: "3:45" },
                    { title: "Deep Focus", duration: "5:20" },
                    { title: "Night Drive", duration: "4:15" },
                    { title: "Morning Calm", duration: "6:10" }
                ]
            },
            video: {
                title: "Video Gallery",
                items: [
                    { title: "Nature Documentary", duration: "15:30" },
                    { title: "Urban Landscapes", duration: "12:45" },
                    { title: "Ocean Life", duration: "18:20" },
                    { title: "Mountain Adventures", duration: "20:15" }
                ]
            },
            group: {
                title: "Creative Groups",
                items: [
                    { title: "Design Team", members: 12 },
                    { title: "Music Production", members: 8 },
                    { title: "Film Crew", members: 15 },
                    { title: "Art Collective", members: 10 }
                ]
            }
        };

        const content = contentMap[selectedContent];
        const contentClass = `${selectedContent}-content`;
        const itemClass = `${selectedContent}-item`;

        return (
            <div className={`${contentClass} ${isTransitioning ? 'fade' : ''}`}>
                <h2>{content.title}</h2>
                <div className={`${selectedContent}-list`}>
                    {content.items.map((item, index) => (
                        <div 
                            key={index} 
                            className={itemClass}
                            style={{
                                animationDelay: `${index * 0.1}s`,
                                opacity: isTransitioning ? 0 : 1,
                                transform: `translateY(${isTransitioning ? '20px' : '0'})`
                            }}
                        >
                            <h3>{item.title}</h3>
                            <p>
                                {item.duration && `Duration: ${item.duration}`}
                                {item.members && `Members: ${item.members}`}
                            </p>
                        </div>
                    ))}
                </div>
            </div>
        );
    };

    return (
        <div className="content">
            <div className="content-type">
                {[
                    { type: 'audio', logo: audioLogo, label: 'Audio' },
                    { type: 'video', logo: videoLogo, label: 'Media' },
                    { type: 'group', logo: groupLogo, label: 'Group' }
                ].map(({ type, logo, label }) => (
                    <div
                        key={type}
                        className={`content-item ${selectedContent === type ? 'active' : ''}`}
                        onClick={() => handleContentChange(type)}
                    >
                        <img 
                            src={logo} 
                            alt={label}
                            style={{
                                transform: selectedContent === type ? 'scale(1.2) rotate(5deg)' : 'scale(1)'
                            }}
                        />
                        {label}
                    </div>
                ))}
            </div>
            <div className="content-display">
                {renderContent()}
            </div>
        </div>
    );
};

export default ContentComponent;
