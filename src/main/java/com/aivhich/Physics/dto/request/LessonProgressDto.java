package com.aivhich.Physics.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LessonProgressDto {
    UUID lesson;
    Long points;

}
