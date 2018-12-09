package com.garchkorelation.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garchkorelation.model.Role;
import com.garchkorelation.repository.RoleRepository;
import com.garchkorelation.service.RoleService;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role findUserRole() {
		return roleRepository.findOne(1L);
	}

	@Override
	public Role findAdminRole() {
		return roleRepository.findOne(2L);
	}

	@Override
	public Role findModeratorRole() {
		return roleRepository.findOne(3L);
	}

	@Override
	public Role findUnverifiedRole() {
		return roleRepository.findOne(4L);
	}

}
