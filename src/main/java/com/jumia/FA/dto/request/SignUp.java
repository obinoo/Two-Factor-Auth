package com.jumia.FA.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SignUp {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
