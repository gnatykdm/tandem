package com.tandem.service.group;

import com.tandem.model.dto.GroupUserDTO;
import com.tandem.model.entity.*;
import com.tandem.repository.GroupRepository;
import org.hibernate.graph.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService implements IGroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void addUserToGroup(Long userId, Long groupId, boolean admin) {
        logger.info("Adding user with ID {} to group with ID {}. Admin status: {}", userId, groupId, admin);

        try {
            groupRepository.addUserToGroup(userId, groupId, admin);
            logger.info("User with ID {} successfully added to group with ID {}", userId, groupId);
        } catch (Exception e) {
            logger.error("Error adding user with ID {} to group with ID {}: {}", userId, groupId, e.getMessage());
            throw e;  // Rethrow exception after logging
        }
    }

    @Override
    public void removeUserFromGroup(Long userId, Long groupId) {
        logger.info("Removing user with ID {} from group with ID {}", userId, groupId);

        try {
            groupRepository.removeUserFromGroup(userId, groupId);
            logger.info("User with ID {} successfully removed from group with ID {}", userId, groupId);
        } catch (Exception e) {
            logger.error("Error removing user with ID {} from group with ID {}: {}", userId, groupId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object[]> listGroupUsers(Long groupId) {
        logger.info("Fetching users for group with ID {}", groupId);

        try {
            List<Object[]> groupUsers = groupRepository.listGroupUsers(groupId);
            logger.info("Successfully fetched users for group with ID {}", groupId);
            return groupUsers;
        } catch (Exception e) {
            logger.error("Error fetching users for group with ID {}: {}", groupId, e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean checkAdminRights(Long userId, Long groupId) {
        logger.info("Checking admin rights for user with ID {} in group with ID {}", userId, groupId);

        try {
            boolean isAdmin = groupRepository.checkAdminRights(userId, groupId);
            logger.info("User with ID {} has admin rights in group with ID {}: {}", userId, groupId, isAdmin);
            return isAdmin;
        } catch (Exception e) {
            logger.error("Error checking admin rights for user with ID {} in group with ID {}: {}", userId, groupId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<GroupEntity> getAllGroups() {
        return groupRepository.getAllGroups();
    }

    @Override
    public void createGroup(GroupEntity group) {
        logger.info("Creating new group with name: {}", group.getGroupName());

        try {
            groupRepository.save(group);
            logger.info("Successfully created group with name: {}", group.getGroupName());
        } catch (Exception e) {
            logger.error("Error creating group with name {}: {}", group.getGroupName(), e.getMessage());
            throw e;
        }
    }

    @Override
    public List<VideoEntity> getAllVideosFromGroup(Long groupId) {
        logger.info("Fetching all videos for group with ID {}", groupId);

        try {
            List<VideoEntity> videos = groupRepository.getAllVideosFromGroup(groupId);
            logger.info("Successfully fetched {} videos for group with ID {}", videos.size(), groupId);
            return videos;
        } catch (Exception e) {
            logger.error("Error fetching videos for group with ID {}: {}", groupId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<PhotoEntity> getAllPhotosFromGroup(Long groupId) {
        logger.info("Fetching all photos for group with ID {}", groupId);

        try {
            List<PhotoEntity> photos = groupRepository.getAllPhotosFromGroup(groupId);
            logger.info("Successfully fetched {} photos for group with ID {}", photos.size(), groupId);
            return photos;
        } catch (Exception e) {
            logger.error("Error fetching photos for group with ID {}: {}", groupId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<MessageEntity> getAllMessagesFromGroup(Long groupId) {
        logger.info("Fetching all messages for group with ID {}", groupId);

        try {
            List<MessageEntity> messages = groupRepository.getMessagesByUser(groupId);
            logger.info("Successfully fetched {} messages for group with ID {}", messages.size(), groupId);
            return messages;
        } catch (Exception e) {
            logger.error("Error fetching messages for group with ID {}: {}", groupId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<AudioEntity> getAllAudioFromGroup(Long groupId) {
        logger.info("Fetching all audio for group with ID {}", groupId);

        try {
            List<AudioEntity> audio = groupRepository.getAllAudioFromGroup(groupId);
            logger.info("Successfully fetched {} audio files for group with ID {}", audio.size(), groupId);
            return audio;
        } catch (Exception e) {
            logger.error("Error fetching audio for group with ID {}: {}", groupId, e.getMessage());
            throw e;
        }
    }

    @Override
    public GroupEntity getGroupByName(String groupName) {
        return groupRepository.getGroupByName(groupName);
    }


    @Transactional
    @Override
    public void createGroup(String groupName, String groupIcon, String groupDescription, String accessCode, boolean type) {
        groupRepository.createGroup(groupName, groupIcon, groupDescription, accessCode, type);
    }

    @Transactional
    @Override
    public void deleteGroup(Long groupId) {
        groupRepository.deleteGroup(groupId);
    }

    @Override
    public void updateGroup(Long groupId, String groupIcon, String groupDescription) {
        groupRepository.updateGroup(groupId, groupIcon, groupDescription);
    }

}
