package service;

import dto.Book;
import dto.BookLibrary;
import exception.BookReaderException;
import interfaces.BookLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Wärnherr on 2016-08-16.
 */


public class BookLoaderImpl implements BookLoader{

    private String bookstoreDataSource;
    private static final Logger LOG = Logger.getLogger(BookLoaderImpl.class.getName());

    public BookLoaderImpl() {}

    @Override
    public BookLibrary loadBooks(String location) throws BookReaderException {
        if (location != null && !location.isEmpty()) {
            setBookstoreDataSource(location);
        }
        BookLibrary bookLibrary = new BookLibrary();
        List<Book> bookList = new ArrayList<Book>();
        Map<Book, Integer> inventory = new HashMap<Book, Integer>();
        try {
            URL url = new URL(getBookstoreDataSource());
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] bookInfo = new String[4];
                bookInfo = line.split(";");
                BigDecimal price = null;
                String strPrice = (bookInfo[2] == null) ? "" : bookInfo[2].replaceAll(",", "");
                price = new BigDecimal(strPrice);
                Integer quantity = bookInfo[3] == null ? 0 : Integer.valueOf(bookInfo[3]);
                Book book = new Book(bookInfo[0], bookInfo[1], price);
                bookList.add(book);
                inventory.put(book, quantity);
            }
            Collections.sort(bookList);
            bookLibrary.setBookList(bookList);
            bookLibrary.setInventory(inventory);
            return bookLibrary;

        } catch (FileNotFoundException e) {
            LOG.log(Level.SEVERE, "The bookstoreDataSource " + getBookstoreDataSource() + " not found");
            throw new BookReaderException(101, "The bookstoreDataSource " + getBookstoreDataSource() + " not found");
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }


    public String getBookstoreDataSource() {
        return bookstoreDataSource;
    }

    public void setBookstoreDataSource(String bookstoreDataSource) {
        this.bookstoreDataSource = bookstoreDataSource;
    }


}
