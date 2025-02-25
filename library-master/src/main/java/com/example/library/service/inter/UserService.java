package com.example.library.service.inter;

import com.example.library.dto.UserDTO;
import com.example.library.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User findUserByEmail(String email);

    User findUserByEmailAndPassword(String email, String password);

    void updatePassword(String password, String email);

    void saveUser(User user);

    void saveMember(UserDTO userDTO);

    User convertUserDTOToUser(UserDTO userDTO);

    User findUserBySurname(String surname);

}
