package com.aivhich.Physics.services;


import com.aivhich.Physics.auth.dto.UserDto;
import com.aivhich.Physics.auth.dto.request.ChangePasswordRequest;
import com.aivhich.Physics.auth.models.User;
import com.aivhich.Physics.dto.response.LeaderUsersResponse;
import com.aivhich.Physics.models.Course;
import com.aivhich.Physics.models.Lesson;
import com.aivhich.Physics.models.Page;
import com.aivhich.Physics.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;


    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    public UserDto getUserData(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .emailVerified(user.isEmailVerified())
                .build();
    }

    public List<User> getUsers(int page) {
        List<User> users = repository.findAll();
        int toIndex = users.size();
        if (toIndex > 25) {
            toIndex = 25 * page;
            return users.subList((page - 1) * 25, toIndex);
        }
        return users;
    }


    private long calculateScore(User user, long epochMilli) {
        return user.getPassedPages()
                .stream()
                .filter(page -> page.getTimestamp() > epochMilli)
                .map(Page::getPoints)
                .flatMapToLong(it -> LongStream.of(it.longValue()))
                .sum();
    }


    public LeaderUsersResponse getLeaders(Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        long epochMilli = Instant.now().toEpochMilli() - (LocalDate.now().with(java.time.DayOfWeek.MONDAY).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());

        List<User> users = repository.findAll().stream()
                .sorted(Comparator.comparingLong(a -> calculateScore((User) a, epochMilli)).reversed())
                .toList();


        Map<UUID, Long> usersPoints = users
                .stream()
                .collect(Collectors.toMap(User::getId, it -> {
                            return it.getPassedPages()
                                    .stream()
                                    .filter(a -> a.getTimestamp() > epochMilli)
                                    .map(Page::getPoints)
                                    .flatMapToLong(b -> LongStream.of(b.longValue()))
                                    .sum();
                        }
                ));
        int place;
        try {
            place = users.indexOf(user);
        } catch (Exception e) {
            place = 101;
        }
        Long points = user.getPassedPages()
                .stream()
                .filter(it -> it.getTimestamp() > epochMilli)
                .map(Page::getPoints)
                .flatMapToLong(it -> LongStream.of(it.longValue()))
                .sum();
        return LeaderUsersResponse.builder()
                .userPoints(usersPoints)
                .points(points)
                .place(place)
                .users(users.subList(Math.max((users.size() - 100), 0), users.size()))
                .build();
    }
}
