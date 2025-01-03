package com.tandem.service.group;

import com.tandem.model.entity.*;
import com.tandem.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService implements IGroupService {

    @Autowired
    private GroupRepository groupRepository;
    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    @Override
    public void addUserToGroup(Long userId, Long groupId, boolean admin) {
        if (userId < 0 || groupId < 0) {
            logger.error("Group Service - addUserToGroup: userId or groupId can't be null");
            throw new RuntimeException("Group Service - addUserToGroup: userId or groupId can't be null");
        }

        logger.info("Group Service - addUserToGroup: Adding user: {} to group: {}", userId, groupId);
        groupRepository.addUserToGroup(userId, groupId, admin);
    }

    @Override
    public void removeUserFromGroup(Long userId, Long groupId) {
        if (userId < 0 || groupId < 0) {
            logger.error("Group Service - removeUserFromGroup: userId or groupId can't be null");
            throw new RuntimeException("Group Service - removeUserFromGroup: userId or groupId can't be null");
        }

        logger.info("Group Service - removeUserFromGroup: Removing user: {} from group: {}", userId, groupId);
        groupRepository.removeUserFromGroup(userId, groupId);
    }

    @Override
    public List<GroupEntity> listGroupUsers(Long groupId) {
        if (groupId < 0) {
            logger.error("Group Service - listGroupUsers: groupId can't be negative");
            throw new RuntimeException("Group Service - listGroupUsers: groupId can't be negative");
        }

        logger.info("Group Service - listGroupUsers: Listing users in group: {}", groupId);
        return groupRepository.listGroupUsers(groupId);
    }

    @Override
    public boolean checkAdminRights(Long userId, Long groupId) {
        if (userId < 0 || groupId < 0) {
            logger.error("Group Service - checkAdminRights: userId or groupId can't be negative");
            throw new RuntimeException("Group Service - checkAdminRights: userId or groupId can't be negative");
        }

        logger.info("Group Service - checkAdminRights: Checking admin rights for user: {} in group: {}", userId, groupId);
        return groupRepository.checkAdminRights(userId, groupId);
    }

    @Override
    public List<GroupEntity> listGroups() {
        logger.info("Group Service - listGroups: Fetching all groups");
        return groupRepository.listGroups();
    }

    @Override
    public void createGroup(GroupEntity group) {
        if (group == null) {
            logger.error("Group Service - createGroup: Group entity cannot be null");
            throw new RuntimeException("Group Service - createGroup: Group entity cannot be null");
        }

        logger.info("Group Service - createGroup: Creating a new group with name: {}", group.getGroupName());
        groupRepository.save(group);
    }

    @Override
    public List<VideoEntity> getAllVideosFromGroup(Long groupId) {
        if (groupId < 0 || groupId == 0) {
            logger.error("Group Service - getAllVideosFromGroup: userId can't be negative");
            throw new RuntimeException("Group Service - getAllVideosFromGroup: userId can't be negative");
        }

        logger.info("Group Service - getAllVideosFromGroup: Getting all video from group: {}", groupId);
        return groupRepository.getAllVideosFromGroup(groupId);
    }

    @Override
    public List<PhotoEntity> getAllPhotosFromGroup(Long groupId) {
        if (groupId < 0 || groupId == 0) {
            logger.error("Group Service - getAllPhotosFromGroup: userId can't be negative");
            throw new RuntimeException("Group Service - getAllPhotosFromGroup: userId can't be negative");
        }

        logger.info("Group Service - getAllPhotosFromGroup: Getting all video from group: {}", groupId);
        return groupRepository.getAllPhotosFromGroup(groupId);
    }

    @Override
    public List<MessageEntity> getAllMessagesFromGroup(Long groupId) {
        if (groupId < 0 || groupId == 0) {
            logger.error("Group Service - getAllMessagesFromGroup: userId can't be negative");
            throw new RuntimeException("Group Service - getAllMessagesFromGroup: userId can't be negative");
        }

        logger.info("Group Service - getAllMessagesFromGroup: Getting all video from group: {}", groupId);
        return groupRepository.getAllMessagesFromGroup(groupId);
    }

    @Override
    public List<AudioEntity> getAllAudioFromGroup(Long groupId) {
        if (groupId < 0 || groupId == 0) {
            logger.error("Group Service - getAllAudioFromGroup: userId can't be negative");
            throw new RuntimeException("Group Service - getAllMessagesFromGroup: userId can't be negative");
        }

        logger.info("Group Service - getAllAudioFromGroup: Getting all video from group: {}", groupId);
        return groupRepository.getAllAudioFromGroup(groupId);
    }
}
