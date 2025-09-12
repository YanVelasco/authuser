package com.ead.authuser.dtos;

import java.util.List;

public record CoursePageDto(
        List<CourseDto> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last,
        boolean first,
        boolean empty
) {
}