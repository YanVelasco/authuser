package com.ead.authuser.configs;

import com.ead.authuser.constants.PaginationConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ResolverConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(
            PageRequest.of(
                PaginationConstants.DEFAULT_PAGE,
                PaginationConstants.DEFAULT_SIZE,
                Sort.by(Sort.Direction.DESC, PaginationConstants.DEFAULT_SORT)
            )
        );
        argumentResolvers.add(resolver);
    }

}
