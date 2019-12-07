package com.boubyan.hackathon.banchar.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boubyan.hackathon.banchar.common.RoleName;
import com.boubyan.hackathon.banchar.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName roleName);
}
