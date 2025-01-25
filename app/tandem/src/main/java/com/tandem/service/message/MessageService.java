package com.tandem.service.message;

import com.tandem.model.entity.MessageEntity;
import com.tandem.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService implements IMessageService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public void addMessage(Long senderId, String content, Long groupId, LocalDateTime sendAt) {
        groupRepository.addMessage(senderId, content, groupId, sendAt);
    }

    @Override
    public List<Object[]> getAllMessagesFromGroup(Long groupId) {
        return groupRepository.getMessagesByGroup(groupId);
    }
}