package com.inventApper.flashkart.services;

import com.inventApper.flashkart.dtos.PageableResponse;
import com.inventApper.flashkart.dtos.UserDto;

import java.util.List;

public interface UserService {

    // create user
    UserDto createUser(UserDto userDto);

    // update user
    UserDto updateUser(UserDto userDto, String userId);

    // delete user
    void deleteUser(String userId);

    // get all users
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    // get single user by Id
    UserDto getUserById(String userId);

    // get user by email
    UserDto getUserByEmail(String email);

    // search user
    List<UserDto> searchUser(String keyword);

    // other user specific

}
