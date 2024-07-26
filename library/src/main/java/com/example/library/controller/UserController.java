package com.example.library.controller;

import com.example.library.dto.LoginRequest;
import com.example.library.dto.LoginResponse;
import com.example.library.entity.User;
import com.example.library.security.JwtTokenProvider;
import com.example.library.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            logger.info("Rejestracja użytkownika: " + user);
            User registeredUser = userService.registerUser(user);
            logger.info("Użytkownik zarejestrowany: " + registeredUser);
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalArgumentException e) {
            logger.error("Błąd rejestracji: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Nieoczekiwany błąd: " + e.getMessage());
            return ResponseEntity.status(500).body("Wystąpił nieoczekiwany błąd");
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        logger.info("Próba logowania dla email: " + loginRequest.getEmail());
        User user = userService.findByEmail(loginRequest.getEmail());
        if (user != null) {
            logger.info("Znaleziono użytkownika: " + user.getEmail());
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String token = jwtTokenProvider.generateToken(user.getEmail());
                session.setAttribute("userId", user.getId());
                return ResponseEntity.ok(new LoginResponse(token));
            } else {
                logger.warn("Niepoprawne hasło dla email: " + loginRequest.getEmail());
            }
        } else {
            logger.warn("Nie znaleziono użytkownika dla email: " + loginRequest.getEmail());
        }
        return ResponseEntity.status(401).body("Invalid email or password");
    }


    @GetMapping("/user/books")
    public String userBooks(Model model, @RequestParam String email) {
        User user = userService.findByEmail(email);
        model.addAttribute("books", user.getBorrowedBooks());
        return "userBooks";
    }


}

