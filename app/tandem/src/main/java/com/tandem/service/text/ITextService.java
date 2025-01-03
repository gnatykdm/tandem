package com.tandem.service.text;

import com.tandem.model.entity.TextEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITextService {
    void saveText(TextEntity text);
    void deleteTextById(Long textId);
    List<TextEntity> getAllTexts();
    List<TextEntity> getAllTextsSentByUser(Long userId);
}
