package com.driver.services;

import com.driver.models.*;
import com.driver.repositories.BookRepository;
import com.driver.repositories.CardRepository;
import com.driver.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    BookRepository bookRepository5;

    @Autowired
    CardRepository cardRepository5;

    @Autowired
    TransactionRepository transactionRepository5;

    @Value("${books.max_allowed}")
    public int max_allowed_books;

    @Value("${books.max_allowed_days}")
    public int getMax_allowed_days;

    @Value("${books.fine.per_day}")
    public int fine_per_day;

    public String issueBook(int cardId, int bookId) throws Exception {
        //check whether bookId and cardId already exist
        Book book;
        Card card;

        //conditions required for successful transaction of issue book:
        //1. book is present and available
        if(bookRepository5.existsById(bookId)){
            book = bookRepository5.findById(bookId).get();
            // If it fails: throw new Exception("Book is either unavailable or not present");
                if(book.isAvailable() == false) {
                    throw new Exception("Book is either unavailable or not present");
                    }
                }else {
                    throw new Exception("Book is either unavailable or not present");
                }

        //2. card is present and activated
        if(cardRepository5.existsById(cardId)){
            card = cardRepository5.findById(cardId).get();
            // If it fails: throw new Exception("Card is invalid");
            if(!card.getCardStatus().equals(CardStatus.ACTIVATED)) {
                throw new Exception("Card is invalid");
                }
            }else{
                throw new Exception("Card is invalid");
            }
            //3. number of books issued against the card is strictly less than max_allowed_books

            List<Book> bookList = card.getBooks();
            if (bookList.size() >= max_allowed_books){
                // If it fails: throw new Exception("Book limit has reached for this card");
                throw new Exception("Book limit has reached for this card");
            }
            book.setCard(card);
            bookList.add(book);
            card.setBooks(bookList);
            Transaction transaction = new Transaction();
            transaction.setBook(book);
            transaction.setFineAmount(0);
            transaction.setIssueOperation(true);
            transaction.setCard(card);
        //If the transaction is successful, save the transaction to the list of transactions and return the id
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setTransactionDate(new Date());
        String trasactionId = transaction.getTransactionId();
        //Note that the error message should match exactly in all cases

       return trasactionId; //return transactionId instead
    }

    public Transaction returnBook(int cardId, int bookId) throws Exception {

        List<Transaction> transactions = transactionRepository5.find(cardId, bookId, TransactionStatus.SUCCESSFUL, true);
        Transaction transaction = transactions.get(transactions.size() - 1);

        //for the given transaction calculate the fine amount considering the book has been returned exactly when this function is called
        Date todayDate = new Date();
        long todayTime = todayDate.getTime();
        long intiTime = transaction.getTransactionDate().getTime();
        long timeTaken = todayTime - intiTime;
        int tDays = (int) ((timeTaken / (1000 * 60 * 60 * 24)) % 7);
        int Edays = tDays - getMax_allowed_days;
        int fineAmount = Edays * fine_per_day;
        //make the book available for other users
        Card card = transaction.getCard();
        Book book = transaction.getBook();
        card.getBooks().remove(book);
        book.setAvailable(true);
        book.setCard(null);
        //make a new transaction for return book which contains the fine amount as well

        Transaction returnBookTransaction = new Transaction();
        returnBookTransaction.setTransactionDate(new Date());
        returnBookTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);

        returnBookTransaction.setIssueOperation(false);
        returnBookTransaction.setCard(card);
        returnBookTransaction.setFineAmount(fineAmount);
        returnBookTransaction.setBook(book);
        return returnBookTransaction; //return the transaction after updating all details

    }
}
