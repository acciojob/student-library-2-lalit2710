package com.driver.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Entity
public class Book {

    public Book(String name, Genre genre, Author author) {
        this.name = name;
        this.genre = genre;
        this.author = author;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties("booksWritten")
    private Author author;

    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties("books")
    private Card card;


    @Column(columnDefinition = "TINYINT(1)")
    private boolean available;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("book")
    private List<Transaction> transactions;

    public Book() {
    }
}

