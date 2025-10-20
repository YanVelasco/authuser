package com.ead.authuser.service;

import com.ead.authuser.enums.RoleName;
import com.ead.authuser.models.RoleModel;

public interface RoleService {

    RoleModel findByRoleName(RoleName roleName);

}
