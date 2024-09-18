package com.aivhich.Physics.dto.response;

import com.aivhich.Physics.auth.models.User;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaderUsersResponse {
    List<User> users;
    Long points;
    Map<UUID, Long> userPoints;
    int place;
}
