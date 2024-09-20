package com.project.payload.mappers;

import com.project.service.business.process.PolisajService;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class PolisajMapper {

    private final PolisajService polisajService;
}
