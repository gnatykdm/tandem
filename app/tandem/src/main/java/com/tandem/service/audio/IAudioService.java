package com.tandem.service.audio;

import com.tandem.model.entity.AudioEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAudioService {
    void saveAudio(AudioEntity audio);
    void deleteAudioById(Long audioId);
    AudioEntity getAudioById(Long audioId);
    List<AudioEntity> getAllAudio();
    List<AudioEntity> getAllAudioByUser(Long userId);
}
