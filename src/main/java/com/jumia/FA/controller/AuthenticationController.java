package com.jumia.FA.controller;

import com.jumia.FA.Exception.EmailExistsException;
import com.jumia.FA.dto.request.Login;
import com.jumia.FA.dto.request.OtpVerification;
import com.jumia.FA.dto.request.SignUp;
import com.jumia.FA.dto.response.ResponseModel;
import com.jumia.FA.entity.User;
import com.jumia.FA.repository.UserRepository;
import com.jumia.FA.service.Impl.AuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin

public class AuthenticationController {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @Autowired
   private UserRepository userRepository;

    @Operation(summary = "User Registration",
                description = "Register a new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or data"),
            @ApiResponse(responseCode = "409", description = "User with the provided email already exists")
    })
    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:2021/")
    public ResponseEntity<ResponseModel> register(@Valid @RequestBody SignUp request) throws EmailExistsException {

        if (request == null && request.toString().equals("")){

            throw new RuntimeException("Add values");
        }

        authenticationService.signup(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "User Login",
            description = "Login a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping("/signIn")
    @CrossOrigin(origins = "http://localhost:2021/")
    public ResponseEntity<ResponseModel> authenticate(@Valid @RequestBody Login request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Otp Sent to Email"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/otpVerification")
    public ResponseEntity <String> otpSent(User user) {

        return ResponseEntity.ok(authenticationService.generateOtp());

    }

    @Operation(
            summary = "User inputs the otp from the Email"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Otp Verification Successful"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/otpCode")
    public ResponseEntity<String> otpVerification(@RequestBody OtpVerification otpVerification) {
        String username = otpVerification.getEmail();
        int enteredOtp = otpVerification.getOtp();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User users = userRepository.findByEmail(username);
        if(users.getOtp() == enteredOtp) {
            users.setActive(true);
            userRepository.save(users);
            return ResponseEntity.ok("OTP verification successful. Redirecting to /dashboard");
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
    }
}
