package com.tandem.service.group;

import com.tandem.model.entity.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IGroupService {
    void addUserToGroup(Long userId, Long groupId, boolean admin);
    void removeUserFromGroup(Long userId, Long groupId);
    List<GroupEntity> listGroupUsers(Long groupId);
    boolean checkAdminRights(Long userId, Long groupId);
    List<GroupEntity> listGroups();
    void createGroup(GroupEntity group);
    List<VideoEntity> getAllVideosFromGroup(Long groupId);
    List<PhotoEntity> getAllPhotosFromGroup(Long groupId);
    List<MessageEntity> getAllMessagesFromGroup(Long groupId);
    List<AudioEntity> getAllAudioFromGroup(Long groupId);
}
