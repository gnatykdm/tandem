package com.tandem.controller;

import com.tandem.model.dto.GroupDTO;
import com.tandem.model.dto.MessageDTO;
import com.tandem.model.entity.GroupEntity;
import com.tandem.model.entity.MessageEntity;
import com.tandem.model.entity.UserEntity;
import com.tandem.service.file.FileService;
import com.tandem.service.group.IGroupService;
import com.tandem.service.message.IMessageService;
import com.tandem.service.s3.IS3Connection;
import com.tandem.service.user.IUserService;
import com.tandem.utils.GenKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/group")
public class GroupController {

    @Autowired
    private IGroupService groupService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private FileService fileService;

    @Autowired
    private IS3Connection is3Connection;

    @PostMapping("/create/{login}")
    public ResponseEntity<String> createGroup(
            @PathVariable String login,
            @RequestParam String groupName,
            @RequestParam String groupDescription,
            @RequestParam boolean groupType
    ) throws IOException {
        return userService.findByLogin(login)
                .map(user -> handleGroupCreation(user, groupName, groupDescription, groupType))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User with login: " + login + " not found"));
    }

    @DeleteMapping("/delete/{groupName}")
    public ResponseEntity<String> deleteGroup(@PathVariable String groupName) {
        GroupEntity group = groupService.getGroupByName(groupName);
        groupService.deleteGroup(group.getGroupId());
        return ResponseEntity.ok("Group deleted successfully");
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        List<GroupDTO> groups = groupService.getAllGroups()
                .stream()
                .map(this::convertToGroupDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/get_by_name/{groupName}")
    public ResponseEntity<GroupDTO> getGroupByName(@PathVariable String groupName) {
        return ResponseEntity.ok(convertToGroupDTO(groupService.getGroupByName(groupName)));
    }

    @PutMapping("/update/{groupName}")
    public ResponseEntity<String> updateGroup(
            @PathVariable String groupName,
            @RequestParam String groupIcon,
            @RequestParam String description
    ) {
        GroupEntity group = groupService.getGroupByName(groupName);
        groupService.updateGroup(group.getGroupId(), groupIcon, description);
        return ResponseEntity.ok("Group updated successfully");
    }

    @PostMapping("/add_user/{groupName}")
    public ResponseEntity<String> addUserToGroup(
            @PathVariable String groupName,
            @RequestParam String userName
    ) {
        Optional<UserEntity> user = userService.findByLogin(userName);
        GroupEntity group = groupService.getGroupByName(groupName);

        if (user.isPresent()) {
            groupService.addUserToGroup(user.get().getId(), group.getGroupId(), false);
            return ResponseEntity.ok("User added to group");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
    }

    @GetMapping("/check_access_code/{groupName}")
    public ResponseEntity<?> checkAccessCode(@PathVariable String groupName,
                                             @RequestParam String accessCode) {
        GroupEntity group = groupService.getGroupByName(groupName);
        if (group != null) {
            boolean validate = validateAccessKey(group.getAccessCode(), accessCode);
            return ResponseEntity.ok(validate);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group is not found");
        }
    }

    @GetMapping("/get_users/{groupName}")
    public ResponseEntity<List<Object[]>> getGroupUsers(@PathVariable String groupName) {
        GroupEntity group = groupService.getGroupByName(groupName);
        return ResponseEntity.ok(groupService.listGroupUsers(group.getGroupId()));
    }

    @DeleteMapping("/delete_user/{groupName}")
    public ResponseEntity<String> deleteUserFromGroup(
            @PathVariable String groupName,
            @RequestParam String userLogin
    ) {
        Optional<UserEntity> user = userService.findByLogin(userLogin);
        GroupEntity group = groupService.getGroupByName(groupName);

        if (user.isPresent()) {
            groupService.removeUserFromGroup(user.get().getId(), group.getGroupId());
            return ResponseEntity.ok("User removed from group");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
    }

    @PostMapping("/add_message/{groupName}")
    public ResponseEntity<String> addMessageToGroup(
            @PathVariable String groupName,
            @RequestParam String userLogin,
            @RequestParam String messageContent
    ) {
        Optional<UserEntity> user = userService.findByLogin(userLogin);
        GroupEntity group = groupService.getGroupByName(groupName);

        if (user.isPresent()) {
            messageService.addMessage(
                    user.get().getId(),
                    messageContent,
                    group.getGroupId(),
                    LocalDateTime.now()
            );
            return ResponseEntity.ok("Message added to group");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
    }

    @GetMapping("/get_all_messages_from_group/{groupName}")
    public ResponseEntity<List<Object[]>> getAllMessagesFromGroup(@PathVariable String groupName) {
        GroupEntity group = groupService.getGroupByName(groupName);
        return ResponseEntity.ok(messageService.getAllMessagesFromGroup(group.getGroupId()));
    }

    @GetMapping("/is_user_admin/{userLogin}")
    public ResponseEntity<?> checkAdminStatus(@PathVariable String userLogin,
                                              @RequestParam String groupName) {
        Optional<UserEntity> user = userService.findByLogin(userLogin);
        GroupEntity group = groupService.getGroupByName(groupName);

        if (user.isPresent() && group != null) {
            boolean status = groupService.checkAdminRights(user.get().getId(), group.getGroupId());
            return ResponseEntity.ok(status);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User or Group not found");
        }
    }

    private ResponseEntity<String> handleGroupCreation(
            UserEntity user,
            String groupName,
            String groupDescription,
            boolean groupType
    ) {
        try {
            File groupIcon = fileService.getGroupLogoAsFile();
            is3Connection.createGroup(groupName);
            String groupLogoURL = is3Connection.uploadGroupLogo(groupIcon, groupName);

            groupService.createGroup(groupName, groupLogoURL, groupDescription, generateGroupKey(groupType), groupType);
            GroupEntity group = groupService.getGroupByName(groupName);
            groupService.addUserToGroup(user.getId(), group.getGroupId(), true);

            return ResponseEntity.ok("Group created successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create group: " + e.getMessage());
        }
    }

    private String generateGroupKey(boolean isPrivate) {
        return isPrivate ? new GenKey().generateGroupKey() : null;
    }

    private boolean validateAccessKey(String key, String sendKey) {
        return key.equals(sendKey);
    }

    private GroupDTO convertToGroupDTO(GroupEntity group) {
        return new GroupDTO(
                group.getGroupId(),
                group.getGroupName(),
                group.getGroupIcon(),
                group.getGroupDescription(),
                group.getCreationDate(),
                group.getAccessCode(),
                group.getType()
        );
    }

    private MessageDTO convertToMessageDTO(MessageEntity message) {
        return new MessageDTO(
                message.getMessageId(),
                message.getSenderMessage().getLogin(),
                message.getContentMessage(),
                message.getSendAt()
        );
    }
}
