package com.TY_CSE.Project.Controller;

import com.TY_CSE.Project.DTO.AuthRequest;
import com.TY_CSE.Project.DTO.AuthResponse;
import com.TY_CSE.Project.Model.Role;
import com.TY_CSE.Project.Model.User;
import com.TY_CSE.Project.Repository.UserRepository;
import com.TY_CSE.Project.Service.UserService;
import com.TY_CSE.Project.Util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://127.0.0.1:5500")
@SessionAttributes("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private HttpServletRequest request;

    @Autowired // Properly inject JwtUtil
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    // User registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthRequest registerRequest) {
        try {
            // Check if user already exists
            if (userService.userExists(registerRequest.getEmail())) {
                System.out.println("Exist");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("User already exists with this email!");
            }

            // Register new user
            userService.registerUser(registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during registration: " + e.getMessage());
        }
    }


    // User login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Fetch the user by email to retrieve the role
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Role role = user.getRole(); // Assuming role is an enum in the User entity

            // Generate JWT Token
            String token = jwtUtil.generateToken(loginRequest.getEmail());
            return ResponseEntity.ok(new AuthResponse(token, role, "success"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null, null,"Invalid email or password"));
        }
    }

    // Test endpoint to verify authentication
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current User: " + authentication.getName());
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok("Authenticated as " + authentication.getName());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authenticated");
        }
    }

    @GetMapping("/session-info")
    public ResponseEntity<String> sessionInfo(HttpSession session) {
        if (session != null) {
            return ResponseEntity.ok("Session ID: " + session.getId());
        }
        return ResponseEntity.ok("No session available");
    }

}
