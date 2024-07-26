package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.entity.UserBookPurchase;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;
import com.example.library.repository.UserBookPurchaseRepository;
import com.example.library.configuration.NotificationWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBookPurchaseRepository userPurchaseRepository;

    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public Book rentBook(Long bookId, Long userId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalBook.isPresent() && optionalUser.isPresent()) {
            Book book = optionalBook.get();
            User user = optionalUser.get();

            user.getBorrowedBooks().add(book);
            userRepository.save(user);

            return book;
        } else {
            throw new RuntimeException("Book or User not found");
        }
    }

    public Book buyBook(Long bookId, Long userId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalBook.isPresent() && optionalUser.isPresent()) {
            Book book = optionalBook.get();
            User user = optionalUser.get();


            UserBookPurchase purchase = new UserBookPurchase(user, book);
            userPurchaseRepository.save(purchase);


            notificationWebSocketHandler.sendNotification("Book with ID " + bookId + " has been bought by user with ID " + userId + "!");

            return book;
        } else {
            throw new RuntimeException("Book or User not found");
        }
    }
}