package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.service.BookService;
import com.example.library.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping("/books")
    public String listBooks(Model model, HttpSession session) {

        model.addAttribute("books", bookService.getAllBooks());


        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.findById(userId);
            model.addAttribute("userBooks", user.getBorrowedBooks());
        }

        return "books";
    }

    @PostMapping("/books/rent")
    @ResponseBody
    public ResponseEntity<?> rentBook(@RequestParam Long bookId, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            Book rentedBook = bookService.rentBook(bookId, userId);
            return ResponseEntity.ok("{\"success\": true}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/books/buy")
    @ResponseBody
    public ResponseEntity<?> buyBook(@RequestParam Long bookId, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            Book boughtBook = bookService.buyBook(bookId, userId);
            return ResponseEntity.ok("{\"success\": true}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
    @GetMapping("/user/books")
    public String userBooks(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.findById(userId);
            model.addAttribute("books", user.getPurchasedBooks());
            return "userPurchasedBooks";
        }
        return "redirect:/login";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }




}
