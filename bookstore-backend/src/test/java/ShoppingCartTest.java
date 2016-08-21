import dto.Book;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import service.BookstoreService;
import service.ShoppingCart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Wärnherr
 * Date: 2016-08-20
 * Time: 12:46
 */

public class ShoppingCartTest {

    private ShoppingCart shoppingCart;

    @Before
    public void init() {
        shoppingCart = new ShoppingCart();
        LinkedList<Book> itemList = new LinkedList<Book>();
        Book book1 = new Book("Mastering åäö", "Average Swede", new BigDecimal("762.00"));
        itemList.add(book1);
        Book book2 = new Book("How To Spend Money", "Rich Bloke", new BigDecimal("1,000,000.00".replaceAll(",", "")));
        itemList.add(book2);
        Book book3 = new Book("Generic Title", "First Author", new BigDecimal("185.50"));
        itemList.add(book3);
        Book book4 = new Book("Generic Title", "Second Author", new BigDecimal("1,748.00".replaceAll(",", "")));
        itemList.add(book4);
        Book book5 = new Book("Random Sales", "Cunning Bastard", new BigDecimal("999.00"));
        itemList.add(book5);
        Book book6 = new Book("Random Sales", "Cunning Bastard", new BigDecimal("499.50"));
        itemList.add(book6);
        Book book7 = new Book("Desired", "Rich Bloke", new BigDecimal("564.50"));
        itemList.add(book7);
        shoppingCart.setItemList(itemList);
    }

    @Test
    public void testInit() {
        TestCase.assertNotNull(shoppingCart);
        TestCase.assertNotNull(shoppingCart.getItemList());
    }

    @Test
    public void testTotalItems() {
        TestCase.assertTrue(shoppingCart.getTotalItems() == 7);
    }

    @Test
    public void testTotalPrice() {
        TestCase.assertTrue(shoppingCart.getTotalPrice().equals(new BigDecimal("1004758.50")));
    }

    @Test
    public void testRemove() {
        TestCase.assertTrue(shoppingCart.getTotalPrice().equals(new BigDecimal("1004758.50"))); //Total before removal
        TestCase.assertTrue(shoppingCart.removeItem(2)); //Remove book3 by index
        TestCase.assertTrue(shoppingCart.getTotalPrice().equals(new BigDecimal("1004573.00"))); //Total after removal, minus price for book3
        Book book3 = new Book("Generic Title", "First Author", new BigDecimal("185.50"));
        TestCase.assertFalse(shoppingCart.getItemList().contains(book3)); //Ensure item list no longer contains book3
        TestCase.assertTrue(shoppingCart.getTotalItems() == 6); //Ensure total items was reduced by 1
    }

    @Test
    public void testAddItem() {
        Book book = new Book("New book", "New Author", new BigDecimal("2000.00"));
        shoppingCart.addItem(book);
        TestCase.assertTrue(shoppingCart.getTotalPrice().equals(new BigDecimal("1006758.50"))); //Cart should have new total price
        TestCase.assertTrue(shoppingCart.getTotalItems() == 8); //Cart should have an additional item
        TestCase.assertTrue(shoppingCart.getItemList().contains(book)); //Cart should contain added book in item list
    }

    @Test
    public void testRemoveSameTitleAuthor() {
        Book book6 = new Book("Random Sales", "Cunning Bastard", new BigDecimal("499.50"));
        TestCase.assertTrue(shoppingCart.removeItem(5));//remove book6 at index 5
        TestCase.assertTrue(shoppingCart.getTotalItems() == 6); //Ensure other book with same title still exists
        TestCase.assertFalse(shoppingCart.getItemList().contains(book6)); //Ensure book6 is removed
    }


}
