package com.tandem.service.content;

import com.tandem.model.entity.ContentEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IContentService {
    void saveContent(Long photoId, Long videoId,
                     Long audioId, Long textId);

    void deleteContent(Long contentId);
    boolean checkContentDeletable(Long contentId);
    List<ContentEntity> getAllContents();
}
