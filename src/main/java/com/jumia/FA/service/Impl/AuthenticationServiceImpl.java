package com.jumia.FA.service.Impl;

import com.jumia.FA.Exception.EmailExistsException;
import com.jumia.FA.dto.request.Login;
import com.jumia.FA.dto.request.SignUp;
import com.jumia.FA.dto.response.ResponseModel;
import com.jumia.FA.entity.Role;
import com.jumia.FA.entity.User;
import com.jumia.FA.jwtUtils.TokenManager;
import com.jumia.FA.repository.UserRepository;
import com.jumia.FA.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenManager tokenManager;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthenticationServiceImpl(TokenManager tokenManager, AuthenticationManager authenticationManager,
                                     UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                                     UserRepository userRepository){
        this.tokenManager= tokenManager;
        this.authenticationManager=authenticationManager;
        this.userRepository=userRepository;
        this.userDetailsService=userDetailsService;
        this.passwordEncoder=passwordEncoder;
    }

    @Autowired
    JavaMailSender javaMailSender;

        @Override
    public ResponseModel signup(SignUp request) throws EmailExistsException {

        Optional<User> stored = Optional.ofNullable(userRepository.findByEmail(request.getEmail()));

        if (stored.isPresent()){
            throw new EmailExistsException("Email already exists!!");
        }

        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        userRepository.save(user);

        return ResponseModel.builder().build();
    }


    @Override
    public ResponseModel login(Login request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                request.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = tokenManager.generateJwtToken(userDetails);
        return new ResponseModel(token);
    }


    @Override
    public String generateOtp() {

        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName().trim();
                System.out.println("Username retrieved: " + username);

                User user = userRepository.findByEmail(username);

                if (user != null){
                    Date currentDate = new Date();

                int randomPIN = (int) (Math.random() * 9000) + 1000;
                user.setOtp(randomPIN);
                user.setActive(false);
                userRepository.save(user);
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setFrom("nnubiacobinna@gmail.com");
                msg.setTo(user.getEmail());
                msg.setSentDate(currentDate);

                msg.setSubject("Welcome");
                msg.setText("Hello \n\n" + "Your Login OTP :" + randomPIN + ".Please Verify. \n\n" + "Regards \n");

                javaMailSender.send(msg);
                    System.out.println(msg);

                return "success";
                } else {
                    // Handle the case where the user is not found
                    return "User not found";
                }
            } else {
                    // Handle the case where the user is not authenticated
                    return "User not authenticated";
                }
        }catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
