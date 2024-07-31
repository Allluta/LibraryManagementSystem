package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.service.BookService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    private final BookService bookService;

    public PaymentController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/payment/{bookId}")
    public String paymentPage(@PathVariable Long bookId, Model model) {
        Book book = bookService.getBookById(bookId);
        model.addAttribute("bookId", bookId);
        model.addAttribute("bookPrice", book.getPrice());
        return "payment";
    }

    @PostMapping("/books/pay")
    public String processPayment(@RequestParam Long bookId,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String cardNumber,
                                 @RequestParam String expiryDate,
                                 @RequestParam String cvv,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {

        com.stripe.Stripe.apiKey = stripeApiKey;

        try {
            Book book = bookService.getBookById(bookId);
            BigDecimal priceInZloty = book.getPrice();
            long amountInCents = priceInZloty.multiply(new BigDecimal("100")).longValue();

            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", amountInCents);
            chargeParams.put("currency", "pln");
            chargeParams.put("source", "tok_visa");
            chargeParams.put("description", "Zakup książki ID: " + bookId + " - Imię: " + firstName + ", Nazwisko: " + lastName);

            RequestOptions requestOptions = RequestOptions.builder().setApiKey(stripeApiKey).build();
            Charge charge = Charge.create(chargeParams, requestOptions);

            Long userId = (Long) session.getAttribute("userId");
            bookService.buyBook(bookId, userId);

            redirectAttributes.addFlashAttribute("message", "Payment successful!");
            return "redirect:/user/books";

        } catch (StripeException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Payment failed!");
            return "redirect:/books?error";
        }
    }
}
