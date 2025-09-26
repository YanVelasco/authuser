package com.ead.authuser.repository;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID>,
        JpaSpecificationExecutor<UserCourseModel> {
    boolean existsByUserIdAndCourseId(UserModel userModel, UUID courseId);

        List<UserCourseModel> findAllByUserId(UserModel userModel);

}
