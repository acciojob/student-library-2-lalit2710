package com.driver.services;

import com.driver.models.Book;
import com.driver.models.Genre;
import com.driver.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {


    @Autowired
    BookRepository bookRepository2;

    public void createBook(Book book){

        bookRepository2.save(book);
    }

    public List<Book> getBooks(String genre, boolean available, String author){
        List<Book> books = bookRepository2.findAll(); //find the elements of the list by yourself
        List<Book> filtBook = new ArrayList<>();
        if(genre == null && author == null){
            if(available == false) {
                for (Book book : books) {
                    if (!book.isAvailable()) {
                        filtBook.add(book);
                    }
                }
            }
                else{
                    for(Book book : books){
                        if(book.isAvailable()){
                            filtBook.add(book);
                        }
                    }
                }
                return filtBook;
            }
            if(author == null){
                for(Book book : books){
                    if(book.getGenre() == Genre.valueOf(genre) && book.isAvailable() == available){
                        filtBook.add(book);
                    }
                }
                return filtBook;
            }
            if(genre == null){
                for(Book book : books){
                    if(book.getAuthor().getName().equals(author) && book.isAvailable() == available){
                        filtBook.add(book);
                    }
                }
                return filtBook;
            }
        return filtBook;
    }
}
