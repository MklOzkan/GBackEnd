package com.project.service.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PageableHelper {

    public Pageable getPageableWithProperties(int page, int size, String sort, String type) {
        Sort.Direction direction = "desc".equalsIgnoreCase(type) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(direction, sort));
    }
}

