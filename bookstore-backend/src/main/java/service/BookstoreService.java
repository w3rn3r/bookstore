package service;

import dto.Book;
import dto.BookLibrary;
import enums.StatusType;
import exception.*;
import interfaces.BookList;
import interfaces.BookLoader;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Wärnherr on 2016-08-16.
 */

@Service("bookstoreService")
public class BookstoreService implements BookList {

    private static final Logger LOG = Logger.getLogger(BookstoreService.class.getName());
    private List<Book> bookList;
    private Map<Book, Integer> inventory;
    private BookLoader bookLoader;
    private BigDecimal totalToPay;

    public BookstoreService() {
        totalToPay = new BigDecimal("0.0");
        bookList = new ArrayList<Book>();
        inventory = new HashMap<Book, Integer>();
    }

    @Override
    public Book[] list(String searchString) throws BookstoreSearchException {
        try {
            //if no search criterion is specified, assume whole list is to be returned
            if (searchString == null || searchString.trim().matches("")) {
                return searchResultListToArray(getBookList());
            }

            List<Book> searchResults = new ArrayList<Book>();
            for (Book book : getBookList()) {
                if (book.getAuthor().toLowerCase().contains(searchString.trim().toLowerCase()) ||
                        book.getTitle().toLowerCase().contains(searchString.trim().toLowerCase())) {
                    searchResults.add(book);
                }
            }
            return searchResultListToArray(searchResults);

        }  catch (Exception e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "101:Error occurred while searching for:"+ searchString + "Message: " + e.getLocalizedMessage());
            throw new BookstoreSearchException(101, "Error occurred while searching for: " + searchString) ;
        }
    }

    private Book[] searchResultListToArray(List<Book> searchResults) {
        Book[] results = new Book[searchResults.size()];
        for (int i = 0; i < searchResults.size(); i++) {
            results[i] = searchResults.get(i);
        }
        return results;
    }

    @Override
    public boolean add(Book book, int quantity) throws BookstoreAddException {

        try {
            if (getInventory().containsKey(book)) { //book already exists, only update quantity
                Integer oldQuantity = inventory.get(book);
                return inventory.replace(book, oldQuantity, (oldQuantity + quantity)); //update quantity only

            } else {
                getBookList().add(book); //Only add book to list and map if not there
                Collections.sort(getBookList()); //Update order after adding to make search quicker
                getInventory().put(book, quantity);
                return inventory.get(book) == quantity; //test that book got added
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "201:Error occurred while attempting to add books to bookstore. " + "Message: " + e.getLocalizedMessage());
            throw new BookstoreAddException(201, "Error occurred while attempting to add books to bookstore") ;
        }
    }

    @Override
    public int[] buy(Book... books) throws BookstoreBuyException {

        try {
            int[] statusCodes = new int[books.length];
            for (int i = 0; i < books.length; i++) {

                if (!getInventory().containsKey(books[i])) { //if book does not exist
                    statusCodes[i] = StatusType.DOES_NOT_EXIST.getStatusCode();

                } else { //book exists
                    Integer quantity = getInventory().get(books[i]);
                    if (quantity == null || quantity < 1) { //no stock
                        statusCodes[i] = StatusType.NOT_IN_STOCK.getStatusCode();
                    } else { //stock exists
                        statusCodes[i] = StatusType.OK.getStatusCode();
                        getInventory().replace(books[i], quantity, (quantity - 1)); //update quantity
                        setTotalToPay(getTotalToPay().add(books[i].getPrice()));//only calculate total price for successful cases
                    }
                }
            }
            return statusCodes;

        } catch (Exception e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "301:Error occurred while attempting to check out books from bookstore. " + "Message: " + e.getLocalizedMessage());
            throw new BookstoreBuyException(301, "Error occurred while attempting to check out books from bookstore");
        }
    }

    public void loadBooks(String location) throws BookstoreLoadException {
        try {
            BookLibrary bookLibrary = getBookLoader().loadBooks(location);
            setBookList(bookLibrary.getBookList());
            setInventory(bookLibrary.getInventory());
        } catch (BookReaderException e) {
//            e.printStackTrace();
            LOG.log(Level.SEVERE, "401:Error occurred while loading books to store. " + "Message: " + e.getLocalizedMessage());
            throw new BookstoreLoadException(401, "Error occurred while loading books to store: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "402:Error occurred while loading books to store. " + "Message: " + e.getLocalizedMessage());
            throw new BookstoreLoadException(402, "Error occurred while loading books to store: " + e.getMessage());
        }
    }

    public void clearTotal() {
        setTotalToPay(new BigDecimal("0.0"));
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public Map<Book, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<Book, Integer> inventory) {
        this.inventory = inventory;
    }

    public BigDecimal getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(BigDecimal totalToPay) {
        this.totalToPay = totalToPay;
    }

    public BookLoader getBookLoader() {
        return bookLoader;
    }

    public void setBookLoader(BookLoader bookLoader) {
        this.bookLoader = bookLoader;
    }

}
