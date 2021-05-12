package com.example.spring_boot_jwt.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignUpRequest {
    @NotEmpty(message = "please enter a firstname")
    @Size(min = 3)
    private String firstName;
    @NotEmpty(message = "please enter a lastname")
    @Size(min = 3)
    private String lastName;
    @NotEmpty(message = "input a email")
    @Email
    private String email;
    @NotEmpty(message = "please input a valid password")
    @Size(min = 8)
    private String password;
    private Set<String> role;
}
