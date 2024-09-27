package com.aivhich.Physics.dto.response;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {
    int courseProgress;
    long points;
    long answerTasks;
}
