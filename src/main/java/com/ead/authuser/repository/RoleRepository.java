package com.ead.authuser.repository;

import com.ead.authuser.models.RoleModel;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RoleRepository extends CrudRepository<RoleModel, UUID> {
}
