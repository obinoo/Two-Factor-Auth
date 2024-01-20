package com.jumia.FA.service;


import com.jumia.FA.Exception.EmailExistsException;
import com.jumia.FA.dto.request.Login;
import com.jumia.FA.dto.request.SignUp;
import com.jumia.FA.dto.response.ResponseModel;


public interface AuthenticationService {
     ResponseModel signup(SignUp request) throws EmailExistsException;

     ResponseModel login(Login request);

     String generateOtp();
}