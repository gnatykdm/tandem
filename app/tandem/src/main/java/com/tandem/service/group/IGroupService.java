package com.tandem.service.group;

import com.tandem.model.dto.GroupDTO;
import com.tandem.model.dto.GroupUserDTO;
import com.tandem.model.entity.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IGroupService {
    void createGroup(String groupName, String groupIcon, String groupDescription, String accessCode, boolean type);
    void deleteGroup(Long groupId);
    void updateGroup(Long groupId, String groupIcon, String groupDescription);
    void addUserToGroup(Long userId, Long groupId, boolean admin);
    void removeUserFromGroup(Long userId, Long groupId);
    List<Object[]> listGroupUsers(Long groupId);
    boolean checkAdminRights(Long userId, Long groupId);
    List<GroupEntity> getAllGroups();
    void createGroup(GroupEntity group);
    List<VideoEntity> getAllVideosFromGroup(Long groupId);
    List<PhotoEntity> getAllPhotosFromGroup(Long groupId);
    List<MessageEntity> getAllMessagesFromGroup(Long groupId);
    List<AudioEntity> getAllAudioFromGroup(Long groupId);
    GroupEntity getGroupByName(String groupName);
}
