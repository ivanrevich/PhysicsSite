package com.aivhich.Physics.auth;


import com.aivhich.Physics.auth.dto.request.*;
import com.aivhich.Physics.auth.dto.response.AuthenticationResponse;
import com.aivhich.Physics.auth.dto.response.ExistCheckResponse;
import com.aivhich.Physics.auth.dto.request.EmailCodeVerifyRequest;
import com.aivhich.Physics.auth.dto.request.ReturnAccessRequest;
import com.aivhich.Physics.repository.UserRepository;
import com.aivhich.Physics.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.security.Principal;

@RestController
@CrossOrigin(origins = {"http://physics-site-816529-ecdefa-77-73-132-179.traefik.me"})
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/userStage")
    public ResponseEntity<?> userStage(@RequestBody EmailRequest emailRequest){
        return ResponseEntity.ok(authenticationService.userStage(emailRequest));
    }

    @PostMapping("/isExistsCheck")
    public ResponseEntity<ExistCheckResponse> isExistChecking(
            @RequestBody ExistCheckRequest existCheckRequest) {
        return ResponseEntity.ok(authenticationService.isExistCheck(existCheckRequest.getNickname(), existCheckRequest.getEmail()));
    }


    @PostMapping("/emailVerify")
    public ResponseEntity<?> emailVerify(@RequestBody EmailCodeVerifyRequest emailCodeVerifyRequest){
        return ResponseEntity.ok(authenticationService.emailVerify(emailCodeVerifyRequest));
    }

    @PostMapping("/rememberPassword")
    public ResponseEntity<?> rememberPassword(@RequestBody ReturnAccessRequest returnAccessRequest){
        return ResponseEntity.ok(authenticationService.returnAccess(returnAccessRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PatchMapping("/change_password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}
