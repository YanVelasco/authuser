package com.ead.authuser.service.impl;

import com.ead.authuser.enums.RoleName;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.models.RoleModel;
import com.ead.authuser.repository.RoleRepository;
import com.ead.authuser.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleModel findByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new NotFoundException("Role not found: " + roleName));
    }
}
