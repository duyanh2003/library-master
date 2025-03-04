package com.example.library.service;

import com.example.library.dto.UserDTO;
import com.example.library.entity.Role;
import com.example.library.entity.User;
import com.example.library.repository.RoleRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.inter.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User findUserByEmailAndPassword(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password);
    }

    @Override
    public void updatePassword(String password, String email) {
        password = passwordEncoder.encode(password);
        userRepository.updatePassword(password, email);
    }

    @Override
    public void saveUser(User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));

        Role role = roleRepository.findRoleByRoleName("USER");
        if (role == null) {
            role = checkRoleExists("USER");
        }
        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);
    }

    @Override
    public void saveMember(UserDTO userDTO) {
        User user = convertUserDTOToUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Role role = roleRepository.findRoleByRoleName(userDTO.getRole());
        if (role == null) {
            role = checkRoleExists(userDTO.getRole());
            if (userDTO.getRole().equals("ADMIN")) {
                checkRoleExists("USER");
            }
        }

        if (userDTO.getRole().equals("ADMIN")) {
            user.setRoles(Arrays.asList(role, roleRepository.findRoleByRoleName("USER")));
        } else {
            user.setRoles(Collections.singletonList(role));
        }

        userRepository.save(user);
    }

    @Override
    public User convertUserDTOToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @Override
    public User findUserBySurname(String surname) {
        return userRepository.findUserBySurname(surname);
    }

    public Role checkRoleExists(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        return roleRepository.save(role);
    }

}
