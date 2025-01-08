package com.example.library.repository;

import com.example.library.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findRoleByRoleName(String roleName);

}
