package com.inventApper.flashkart.controllers;

import com.inventApper.flashkart.dtos.ApiResponseMessage;
import com.inventApper.flashkart.dtos.PageableResponse;
import com.inventApper.flashkart.dtos.UserDto;
import com.inventApper.flashkart.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    //create user
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
        UserDto userDto = userService.createUser(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    //update user
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user, @PathVariable String userId) {
        UserDto userDto = userService.updateUser(user, userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    //delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponseMessage("User deleted successfully", true, HttpStatus.OK), HttpStatus.OK);
    }


    //get all users
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
        PageableResponse<UserDto> users = this.userService.getAllUser(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //get single user
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }


    //get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    //search user
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword) {
        return ResponseEntity.ok(userService.searchUser(keyword));
    }

}
