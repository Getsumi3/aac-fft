package com.garchkorelation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garchkorelation.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findOneByName(String name);
}
