package com.aivhich.Physics.dto.response;


import com.aivhich.Physics.dto.request.LessonProgressDto;
import com.aivhich.Physics.models.Lesson;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    List<Lesson> passed;
    Long LastPassedId;
    Set<LessonProgressDto> lessonProgressSet;
}
