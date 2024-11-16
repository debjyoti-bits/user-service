package org.bits.userservice.controller;

import org.bits.userservice.model.User;
import org.bits.userservice.repository.RoleRepository;
import org.bits.userservice.repository.UserRepository;
import org.bits.userservice.security.JwtDenyListService;
import org.bits.userservice.security.JwtTokenProvider;
import org.bits.userservice.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AuthenticationController {

    @Autowired
    private JwtDenyListService jwtDenyListService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleRepository.findByName(user.getUserRole()));
        userRepository.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(Collections.singletonMap("token", jwt));
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userRepository.findByUsername(username));
    }

    @PutMapping("/profile")
    public User updateUserProfile(@RequestBody User userDetails) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        user.setName(userDetails.getName());
        user.setAddress(userDetails.getAddress());
        user.setContact(userDetails.getContact());
        return userRepository.save(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(name = "Authorization") String token) {
        // Remove "Bearer " prefix
        String jwtToken = token.replace("Bearer ", "");
        jwtDenyListService.addTokenToDenyList(jwtToken);
        return ResponseEntity.ok("Logged out successfully");
    }
}
