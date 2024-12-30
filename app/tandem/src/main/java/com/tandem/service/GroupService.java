package com.tandem.service;

import com.tandem.model.entity.UserEntity;
import com.tandem.repository.GroupRepository;
import com.tandem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(GroupService.class);

    public void addUserToGroup(Long userId, Long groupId, Boolean admin) {
        try {
            logger.info("Adding user with ID {} to group with ID {}", userId, groupId);
            groupRepository.addUserToGroup(userId, groupId, admin);
            logger.info("User with ID {} successfully added to group with ID {}", userId, groupId);
        } catch (Exception e) {
            logger.error("Error adding user with ID {} to group with ID {}: {}", userId, groupId, e.getMessage());
            throw new RuntimeException("Error adding user to group", e);
        }
    }

    public void removeUserFromGroup(Long userId, Long groupId) {
        try {
            logger.info("Removing user with ID {} from group with ID {}", userId, groupId);
            groupRepository.removeUserFromGroup(userId, groupId);
            logger.info("User with ID {} successfully removed from group with ID {}", userId, groupId);
        } catch (Exception e) {
            logger.error("Error removing user with ID {} from group with ID {}: {}", userId, groupId, e.getMessage());
            throw new RuntimeException("Error removing user from group", e);
        }
    }

    public void inviteUserToGroup(Long userId, Long groupId, String accessCode) {
        try {
            logger.info("Inviting user with ID {} to group with ID {} using access code {}", userId, groupId, accessCode);
            groupRepository.inviteToGroup(userId, groupId, accessCode);
            logger.info("User with ID {} successfully invited to group with ID {}", userId, groupId);
        } catch (Exception e) {
            logger.error("Error inviting user with ID {} to group with ID {}: {}", userId, groupId, e.getMessage());
            throw new RuntimeException("Error inviting user to group", e);
        }
    }

    public List<UserEntity> listGroupUsers(Long groupId) {
        try {
            logger.info("Fetching users for group with ID {}", groupId);
            List<Object[]> result = groupRepository.listGroupUsers(groupId);

            List<UserEntity> users = new ArrayList<>();
            for (Object[] row : result) {
                Long userId = (Long) row[0];
                UserEntity user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found with ID:" + userId));
                users.add(user);
            }
            logger.info("Fetched {} users for group with ID {}", users.size(), groupId);
            return users;
        } catch (Exception e) {
            logger.error("Error fetching users for group with ID {}: {}", groupId, e.getMessage());
            throw new RuntimeException("Error fetching users for group", e);
        }
    }
}
