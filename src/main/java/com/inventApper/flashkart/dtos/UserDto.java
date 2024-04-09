package com.inventApper.flashkart.dtos;

import com.inventApper.flashkart.validate.ImageNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {

    private String userId;

    @Size(min = 3, max = 20, message = "Invalid Name length !!")
    private String name;

    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid Email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is Required !!")
    private String password;

    @Size(min = 4, max = 6, message = "Invalid Gender!! Choose Either 'Male' or 'Female'")
    private String gender;

    @NotBlank(message = "About can not be blank")
    private String about;

    @ImageNameValid
    private String imageName;

    private Set<RoleDto> roles = new HashSet<>();
}
