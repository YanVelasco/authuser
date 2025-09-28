package com.ead.authuser.clients;

import com.ead.authuser.dtos.CoursePageDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class CourseClient {

    Logger logger = LogManager.getLogger(CourseClient.class);

    @Value("${ead.api.url.course}")
    String BASE_URL_COURSE;

    final RestClient restClient;

    public CourseClient(RestClient.Builder restClientbuilder) {
        this.restClient = restClientbuilder.build();
    }

    public CoursePageDto getAllCoursesByUser(UUID userId, Pageable pageable) {
        String url = BASE_URL_COURSE + "/courses?" + "page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize() +
                "&sort=" + pageable.getSort().toString().replace(": ", ",") +
                "&userInstructor=" + userId;
        logger.debug("Request URL: {}", url);
        try {
            return restClient
                    .get()
                    .uri(url)
                    .retrieve()
                    .body(CoursePageDto.class);
        } catch (Exception e) {
            logger.error("Error fetching courses for user {}: {}", userId, e.getMessage());
            return null;
        }
    }

    public void deleteUserCourseInCourse(UUID userId) {
        String url = BASE_URL_COURSE + "/courses/users/" + userId;
        logger.debug("Request URL for deleting user courses: {}", url);
        try {
            restClient
                    .delete()
                    .uri(url)
                    .retrieve()
                    .body(Void.class);
        } catch (Exception e) {
            logger.error("Error deleting courses for user {}: {}", userId, e.getMessage());
        }
    }

}
