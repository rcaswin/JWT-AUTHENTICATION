package com.example.SpringSecurity.Service;

import com.example.SpringSecurity.Entity.AuthEntity;
import com.example.SpringSecurity.Entity.Role;
import com.example.SpringSecurity.Repository.UserRepository;
import com.example.SpringSecurity.Validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;  // Ensure it's injected via constructor

//    String Regex = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthenticationService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    UserRepository userRepository;
    public void register(String name, String email, String password, Role userRole) throws IOException {
        List<String> errors=new ArrayList<>();

        if(name == null || name.isEmpty()){
            errors.add("Name cannot be null or empty");
        }
        if(email == null || email.isEmpty()){
            errors.add("Email cannot be null or empty");
        }

        // Validate Email
        if (!isValidEmail(email)) {
            errors.add("Invalid email format! Must contain '@' and a valid domain.");
        }
        if(password == null || password.isEmpty()){
            errors.add("Password cannot be null or empty");
        }

        if(!errors.isEmpty()){
            throw new Validation(errors,"errors");
        }


        AuthEntity userEntity=new AuthEntity();
        userEntity.setName(name);
        userEntity.setEmail(email);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setRole(userRole);
        userRepository.save(userEntity);

    }

    public Map<String, String> login(String email, String password) {
        Map<String, String> response = new HashMap<>();

        Optional<AuthEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            response.put("message", "Invalid email or password");
            return response;
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            response.put("message", "Invalid email or password");
            return response;
        }

        AuthEntity user = userOptional.get();
        String role = String.valueOf(user.getRole());
        String token = jwtService.generateToken(email, role);

        response.put("role", role);
        response.put("token", token);
        return response;
    }

    private static final Pattern GMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@gmail\\.com$");

    public static boolean isValidEmail(String email) {
        return GMAIL_REGEX.matcher(email).matches();
    }

}

