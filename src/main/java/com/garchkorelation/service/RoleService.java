package com.garchkorelation.service;

import com.garchkorelation.model.Role;

public interface RoleService {

	Role findUserRole();

	Role findAdminRole();

	Role findModeratorRole();
	
	Role findUnverifiedRole();

}
