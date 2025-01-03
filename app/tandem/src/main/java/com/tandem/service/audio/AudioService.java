package com.tandem.service.audio;

import com.tandem.model.entity.AudioEntity;
import com.tandem.repository.AudioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AudioService implements IAudioService {

    @Autowired
    private AudioRepository audioRepository;
    private final Logger logger = LoggerFactory.getLogger(AudioService.class);

    @Override
    public void saveAudio(AudioEntity audio) {
        if (audio == null) {
            logger.error("AudioService - saveAudio: Audio entity cannot be null");
            throw new IllegalArgumentException("Audio entity cannot be null");
        }

        logger.info("AudioService - saveAudio: Saving audio entity with URL: {}", audio.getAudioUrl());
        audioRepository.save(audio);
    }

    @Override
    public void deleteAudioById(Long audioId) {
        if (audioId == null || audioId <= 0) {
            logger.error("AudioService - deleteAudioById: Invalid audio ID");
            throw new IllegalArgumentException("Invalid audio ID");
        }

        logger.info("AudioService - deleteAudioById: Deleting audio with ID: {}", audioId);
        audioRepository.deleteById(audioId);
    }

    @Override
    public AudioEntity getAudioById(Long audioId) {
        if (audioId == null || audioId <= 0) {
            logger.error("AudioService - getAudioById: Invalid audio ID");
            throw new IllegalArgumentException("Invalid audio ID");
        }

        logger.info("AudioService - getAudioById: Fetching audio with ID: {}", audioId);
        Optional<AudioEntity> audioEntity = audioRepository.findById(audioId);
        if (audioEntity.isPresent()) {
            return audioEntity.get();
        } else {
            logger.error("AudioService - getAudioById: Audio with ID: {} not found", audioId);
            throw new RuntimeException("Audio not found");
        }
    }

    @Override
    public List<AudioEntity> getAllAudio() {
        logger.info("AudioService - getAllAudio: Fetching all audio entities");
        return audioRepository.findAll();
    }

    @Override
    public List<AudioEntity> getAllAudioByUser(Long userId) {
        if (userId == null || userId <= 0) {
            logger.error("AudioService - getAllAudioByUser: Invalid user ID");
            throw new IllegalArgumentException("Invalid user ID");
        }

        logger.info("AudioService - getAllAudioByUser: Fetching all audio for user with ID: {}", userId);
        return audioRepository.getAllAudioByUser(userId);
    }
}
