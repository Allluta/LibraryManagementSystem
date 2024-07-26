package com.example.library.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_borrowed_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> borrowedBooks = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserBookPurchase> purchasedBooks = new HashSet<>();



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(Set<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public Set<UserBookPurchase> getPurchasedBooks() {
        return purchasedBooks;
    }

    public void setPurchasedBooks(Set<UserBookPurchase> purchasedBooks) {
        this.purchasedBooks = purchasedBooks;
    }
}
