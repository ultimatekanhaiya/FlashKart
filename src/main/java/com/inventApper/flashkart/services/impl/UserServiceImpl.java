package com.inventApper.flashkart.services.impl;

import com.inventApper.flashkart.dtos.PageableResponse;
import com.inventApper.flashkart.dtos.UserDto;
import com.inventApper.flashkart.entities.User;
import com.inventApper.flashkart.exceptions.ResourceNotFoundException;
import com.inventApper.flashkart.helper.Helper;
import com.inventApper.flashkart.repositories.UserRepository;
import com.inventApper.flashkart.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDto createUser(UserDto userDto) {
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User user = mapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
        logger.info("User saved with id: " + savedUser.getUserId());
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        User.builder()
                .name(userDto.getName())
                .about(userDto.getAbout())
                .gender(userDto.getGender())
                .password(userDto.getPassword())
                .imageName(userDto.getImageName()).build();
        User updatedUser = userRepository.save(user);
        logger.info("User updated with id: " + updatedUser.getUserId());
        return mapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> page = userRepository.findAll(pageable);

        return Helper.getPageableResponse(page, UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        return users.stream().map(user -> mapper.map(user, UserDto.class)).toList();
    }
}
