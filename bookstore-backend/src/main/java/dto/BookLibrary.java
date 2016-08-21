package dto;

import java.util.List;
import java.util.Map;

/**
 * Created by Wärnherr on 2016-08-17.
 */
public class BookLibrary {

    private List<Book> bookList;
    private Map<Book, Integer> inventory;

    public BookLibrary() {}

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

}
