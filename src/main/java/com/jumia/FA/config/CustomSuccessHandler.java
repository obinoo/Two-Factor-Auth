package com.jumia.FA.config;

import java.io.IOException;
import java.util.Objects;
import com.jumia.FA.entity.User;
import com.jumia.FA.repository.UserRepository;
import com.jumia.FA.service.Impl.AuthenticationServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler{


    private final UserRepository userRepo;
    private final AuthenticationServiceImpl userService;


    public CustomSuccessHandler(UserRepository userRepo, AuthenticationServiceImpl userService){
        this.userRepo = userRepo;
        this.userService= userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = null;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByEmail(username);
        String output = userService.generateOtp();
        if(Objects.equals(output, "success"))
            redirectUrl="/login/otpVerification";

        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

}