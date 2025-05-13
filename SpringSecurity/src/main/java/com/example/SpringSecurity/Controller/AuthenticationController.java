package com.example.SpringSecurity.Controller;

import com.example.SpringSecurity.Entity.Role;
import com.example.SpringSecurity.Response.ResponseHandler;
import com.example.SpringSecurity.Service.AuthService;
import com.example.SpringSecurity.Service.AuthenticationService;
import com.example.SpringSecurity.Service.JWTService;
import com.example.SpringSecurity.Validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthenticationController {

    private final AuthService userService;
    private final JWTService jwtService;
    private  final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthService userService, JWTService jwtService,AuthenticationService authenticationService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationService=authenticationService;
    }



    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("name") String name, @RequestParam("email") String email,
                                      @RequestParam("password") String password,@RequestParam("role") String role){
        try{
            Role userRole = Role.valueOf(role.toUpperCase());
            authenticationService.register(name,email,password,userRole);
            return ResponseHandler.generate(null,"Registered Successfully", HttpStatus.OK);
        }catch (Validation e){
            return ResponseHandler.generate(e.getError(),"Failed", HttpStatus.OK);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam("email") String email,
                                                     @RequestParam("password") String password) {
        Map<String, String> response = authenticationService.login(email, password);

        if (response.containsKey("message")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

}