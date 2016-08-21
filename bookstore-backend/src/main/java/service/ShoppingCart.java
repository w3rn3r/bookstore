package service;

import dto.Book;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Wärnherr on 2016-08-18.
 */

@Service("shoppingCart")
public class ShoppingCart implements Serializable {

    private BigDecimal totalPrice;
    private LinkedList<Book> itemList;

    public ShoppingCart() {}

    public void addItem (Book book) {
        if (getItemList() == null) {
            itemList = new LinkedList<Book>();
        }
        getItemList().add(book);
        setTotalPrice(getTotalPrice().add(book.getPrice()));
    }

    public boolean removeItem(int index) {
        if (itemList != null) {
            Book item = null;
            item = itemList.get(index);
            if (item != null) {
                setTotalPrice(getTotalPrice().subtract(item.getPrice()));
                return itemList.remove(item);
            }
        }
        return false;
    }

    public Integer getTotalItems() {
        return (itemList == null) ? 0 : itemList.size();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice == null ? new BigDecimal("0.0") : totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LinkedList<Book> getItemList() {
        return itemList;
    }

    public Book[] getItems() {
        Book[] books = new Book[getTotalItems()];
        for (int i = 0; i < getTotalItems(); i++) {
            Book tmp = getItemList().get(i);
            books[i] = tmp;
        }
        return books;
    }

    public void setItemList(LinkedList<Book> itemList) {
        this.itemList = itemList;
        if (itemList != null) {
            Iterator<Book> itr = itemList.iterator();
            while (itr.hasNext()) {
                Book book = itr.next();
                setTotalPrice(getTotalPrice().add(book.getPrice()));
            }
        }
    }




}
