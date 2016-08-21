package interfaces;

import dto.Book;

/**
 * Created by Wärnherr on 2016-08-16.
 */
public interface BookList {

    public Book[] list(String searchString);

    public boolean add(Book book, int quantity);

    public int[] buy(Book... books);
}
