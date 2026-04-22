package com.example.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.CreateUserRequestDto;
import com.example.dto.CreateUserResponseDto;
import com.example.entity.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public CreateUserResponseDto createUser(CreateUserRequestDto createUserDto) {
        // Logic to create a new user
        User user = new User();
        user.setUsername(createUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setRoles(List
                .of(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"))));
        userRepository.save(user);
        CreateUserResponseDto responseDto = new CreateUserResponseDto();
        responseDto.setUsername(user.getUsername());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        return responseDto;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("user not found exception"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                role.getName()))
                        .toList());
        return userDetails;
    }

}
