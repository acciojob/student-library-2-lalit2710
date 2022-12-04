package com.driver.controller;

import com.driver.models.Book;
import com.driver.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Add required annotations
@RestController
@RequestMapping
public class BookController {

    @Autowired
    BookService bookService;
    //Write createBook API with required annotations
    @PostMapping("book")
    public  ResponseEntity createBook (@RequestBody Book book){
        try {
            bookService.createBook(book);
        }catch (Exception e){
            return new ResponseEntity("Not Success",HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }

    //Add required annotations
    @GetMapping("book")
    public ResponseEntity getBooks(@RequestParam(value = "genre", required = false) String genre,
                                   @RequestParam(value = "available", required = false, defaultValue = "false") boolean available,
                                   @RequestParam(value = "author", required = false) String author){

        List<Book> bookList = bookService.getBooks(genre,available,author); //find the elements of the list by yourself

        return new ResponseEntity<>(bookList, HttpStatus.OK);

    }
}
