package com.tandem.service.user;

import com.tandem.model.dto.UpdateUserDTO;
import com.tandem.model.dto.UserDTO;
import com.tandem.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IUserService {
    void addUser(UserDTO user);
    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findByEmail(String email);
    boolean checkAuthorization(String login, String password);
    void deleteUserByLogin(String login);
    void updateUserByLogin(UpdateUserDTO updateUserDTO, String login);
    List<UserEntity> getAllUsers();
}
