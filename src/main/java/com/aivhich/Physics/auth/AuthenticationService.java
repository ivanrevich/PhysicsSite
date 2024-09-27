package com.aivhich.Physics.auth;

import com.aivhich.Physics.auth.code.Code;
import com.aivhich.Physics.auth.code.CodeRepository;
import com.aivhich.Physics.auth.code.CodeTimeType;
import com.aivhich.Physics.auth.dto.request.*;
import com.aivhich.Physics.auth.dto.response.*;
import com.aivhich.Physics.auth.token.Token;
import com.aivhich.Physics.auth.token.TokenRepository;
import com.aivhich.Physics.auth.token.TokenType;
import com.aivhich.Physics.auth.models.User;
import com.aivhich.Physics.auth.models.states.Role;
import com.aivhich.Physics.auth.models.states.UserStage;
import com.aivhich.Physics.models.Course;
import com.aivhich.Physics.repository.UserRepository;
import com.aivhich.Physics.services.MailSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Time;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final CodeRepository codeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailSender mailSender;

    public AuthenticationResponse register(RegisterRequest request) {

        //checking data
        var user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .surname(request.getSurname())
                .nickname(request.getUsername())
                .courses(Set.of(Course.PHYSICS))
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        Code code = Code.builder().code(newEmailToken()).createdAt(Time.from(Instant.now()).getTime()).codeTimeType(CodeTimeType.TEN_MINUTES).user(user).build();
        codeRepository.save(code);
        List<Code> codes = new ArrayList<>();
        codes.add(code);
        user.setCodes(codes);

        if(request.getEmail().equals("aivhich@yandex.ru") || repository.findAll().size()<2){
            user.setRole(Role.ADMIN);
        }
        //user.setEmailVerified(false);
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);


        mailSender.sendEmail(user.getEmail(), "Физбот: Подтверждение email адреса",
                "Здраствуйте, " + user.getNickname() +
                        "!\n\nЧтобы подтвердить свой адрес электронной почты в приложении ФизБот, введите этот код:\n"
                        + code.getCode() +
                        "\nЭтот код действует 10 минут\n" +
                        "\nЕсли вы получили это письмо по ошибке, просто проигнорируйте его!\n\nФизбот\n\nДля вопросов: founder@applang.ru");
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .roles(user.getRole())
                .build();
    }


    public ExistCheckResponse isExistCheck(String nickname, String email) {
        return new ExistCheckResponse(repository.findByNickname(nickname).isEmpty(), repository.findByEmail(email).isEmpty());
    }

    private Integer newEmailToken() {
        Random r = new Random(Time.from(Instant.now()).getTime());
        int low = 100000;
        int high = 1000000;

        return r.nextInt(high - low) + low;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        //revokeAllUserTokens(user); /// delete other sessions but should do that
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .roles(user.getRole())
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                //revokeAllUserTokens(user); /// delete other sessions but should do that
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private Code getEmailCode(User user, List<Code> codes) {
        for (Code code : codes) {
            Long outTime = code.getCreatedAt();
            CodeTimeType codeTimeType = code.getCodeTimeType();
            switch (codeTimeType) {
                case FIVE_MINUTES -> outTime += 300_000L;
                case TEN_MINUTES -> outTime += 600_000L;
                case THREE_MINUTES -> outTime += 180_000L;
                default -> outTime += 0;
            }

            if (Time.from(Instant.now()).getTime() <= outTime) {
                return code;
            } else {
                if (!user.isEmailVerified()) {
                    repository.deleteById(user.getId());
                }
            }
        }
        return null;
    }

    private Boolean isAllTokensExpired(List<Token> tokens) {
        for (Token token : tokens) if (!token.expired) return false;
        return true;
    }

    public EmailVerifyResponse emailVerify(EmailCodeVerifyRequest emailCodeVerifyRequest) {
        Token token = tokenRepository.findByToken(emailCodeVerifyRequest.getAccessToken()).orElse(null);
        if (token != null && Objects.equals(token.user.getEmail(), emailCodeVerifyRequest.getEmail())) {
            User user = token.user;
            Code code = getEmailCode(user, user.getCodes());
            if (!user.isEmailVerified() && isAllTokensExpired(user.getTokens())) {
                repository.deleteById(user.getId());
                return EmailVerifyResponse.builder().isValid(false).message("USER DELETED").build();
            }else if (code != null && code.getCode() == emailCodeVerifyRequest.getCode()) {
                user.setEmailVerified(true);
                repository.save(user);
                return EmailVerifyResponse.builder().isValid(true).build();
            }
        }
        return EmailVerifyResponse.builder().isValid(false).build();
    }

    public ReturnAccessResponse returnAccess(ReturnAccessRequest returnAccessRequest) {
        User user = repository.findByEmail(returnAccessRequest.getEmail()).orElseThrow();
        if (returnAccessRequest.getCode() == null) {
            //if (user.getBirthday() == returnAccessRequest.getBirthday()) {
                Code code = Code.builder().code(newEmailToken()).createdAt(Time.from(Instant.now()).getTime()).codeTimeType(CodeTimeType.FIVE_MINUTES).user(user).build();
                mailSender.sendEmail(user.getEmail(), "Applang: Email verification",
                        "Hello, " + user.getNickname() +
                                "!\n\nTo verify you in the Applang app, enter this code:\n"
                                + code.getCode() +
                                "\nThis code will expire in 5 minutes.\n" +
                                "\nIf you received this email in error, simply ignore it!\n\nYour Applang\n\nFor Questions founder@applang.ru");
            //}
        } else if (returnAccessRequest.getCode()
                == Objects.requireNonNull(getEmailCode(user, user.getCodes())).getCode()
                /*&& user.getBirthday() == returnAccessRequest.getBirthday()*/) {
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return ReturnAccessResponse.builder()
                    .successfully(true)
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken).build();
        }
        return ReturnAccessResponse.builder().successfully(true).build();
    }

    public StageResponse userStage(EmailRequest emailRequest) {
        User user = repository.findByEmail(emailRequest.getEmail()).orElse(null);
        if(user!=null) {
            if (!user.isEmailVerified() && isAllTokensExpired(user.getTokens())) {
                return new StageResponse(UserStage.DELETED_BECAUSE_NOT_VERIFIED); // is deleted
            }
            if (!user.isEmailVerified()) {
                return new StageResponse(UserStage.NOT_VERIFIED); // is verif time
            }
//            if (user.getStartInterests().isEmpty()) {
//                return new StageResponse(UserStage.QA_NOT_COMPLETE); // is qa time
//            }
            return new StageResponse(UserStage.COMPLETE_AUTH); // full signup
        }
        return new StageResponse(UserStage.NOT_EXIST);
    }
}