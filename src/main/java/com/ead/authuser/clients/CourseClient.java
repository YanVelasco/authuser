package com.ead.authuser.clients;

import com.ead.authuser.dtos.CoursePageDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
public class CourseClient {

    Logger logger = LogManager.getLogger(CourseClient.class);

    @Value("${ead.api.url.course}")
    String BASE_URL_COURSE;

    final RestClient restClient;

    public CourseClient(RestClient.Builder restClientbuilder) {
        this.restClient = restClientbuilder.build();
    }

    @CircuitBreaker(name = "circuitbreakerInstance")
    @Retry(name = "retryInstance")
    public CoursePageDto getAllCoursesByUser(UUID userId, Pageable pageable, String token) throws AccessDeniedException {
        String url = BASE_URL_COURSE + "/courses?" + "page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize() +
                "&sort=" + pageable.getSort().toString().replace(": ", ",") +
                "&userInstructor=" + userId;
        logger.debug("Request URL: {}", url);
        try {
            return restClient
                    .get()
                    .uri(url)
                    .header("Authorization", token)
                    .retrieve()
                    .body(CoursePageDto.class);
        } catch (HttpStatusCodeException e) {
            logger.error("HTTP Status Error fetching courses for user {}: {}", userId, e.getStatusCode());
            switch (e.getStatusCode()) {
                case FORBIDDEN -> throw new AccessDeniedException("FORBIDDEN");
                default ->
                        throw new RuntimeException("HTTP Status Error fetching courses for user " + userId + ": " + e.getStatusCode(), e);
            }
        } catch (RestClientException e) {
            logger.error("Error fetching courses for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Error fetching courses for user " + userId, e);
        }
    }

}
