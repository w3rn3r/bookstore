import dto.Book;
import exception.BookstoreLoadException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import service.BookLoaderImpl;
import service.BookstoreService;
import service.ShoppingCart;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Wärnherr
 * Date: 2016-08-20
 * Time: 14:25
 */

public class BookstoreServiceTest {

    private BookstoreService bookstoreService;
    private ShoppingCart shoppingCart;

    @Before
    public void init() {
        //Instantiate for testing
        bookstoreService = new BookstoreService();

        BookLoaderImpl bookLoader = new BookLoaderImpl();
        bookLoader.setBookstoreDataSource("http://www.contribe.se/bookstoredata/bookstoredata.txt");
        bookstoreService.setBookLoader(bookLoader);
        bookstoreService.loadBooks(null); //public helper method for testing
        shoppingCart = new ShoppingCart(); //Shopping Cart separate
    }



    @Test
    public void testListAllBooks() {
        Book[] results = bookstoreService.list("");  //Empty string or null as parameter to list all books
        TestCase.assertNotNull(results);
        TestCase.assertTrue(results.length == 7); //We know this will initially be 7
//        for (int i = 0; i < results.length; i++) {
//            System.out.println(results[i].toString());
//        }
    }

    @Test
    public void testListSearchBookMultiple() {
        Book[] results = bookstoreService.list(" cunning "); //Tests non-case sensitivity and preceding and trailing whitespaces
        TestCase.assertNotNull(results);
        TestCase.assertTrue(results.length == 2); //should be two matches that contains the word "cunning"
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i].toString());
        }
    }


    @Test
    public void testListSearchBook() {
        Book[] results = bookstoreService.list("how to spend money");
        TestCase.assertNotNull(results);
        TestCase.assertTrue(results.length == 1); //Testing for one book only
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i].toString());
        }
    }

    @Test
    public void testAddBook() {
        Book book7 = new Book("Desired", "Rich Bloke", new BigDecimal("564.50"));
        bookstoreService.add(book7, 2); //Tests adding multiple stock for existing book
        Book[] results = bookstoreService.list("Desired");
        TestCase.assertNotNull(results);
        TestCase.assertTrue(results.length == 1); //searching should only return one hit
    }


    @Test
    public void testBuyBooks() {
        Book[] results = bookstoreService.list("mastering");  //OK
        shoppingCart.addItem(results[0]);

        Book[] results2 = bookstoreService.list("Desired");   //NOT_IN_STOCK
        shoppingCart.addItem(results2[0]);

        Book[] results3 = bookstoreService.list("Generic Title");  //OK
        shoppingCart.addItem(results3[1]);

        shoppingCart.addItem(new Book("The Unknown", "Ghost Writer", new BigDecimal("458.50")));  //DOES_NOT_EXIST

        int[] purchaseStatus = bookstoreService.buy(shoppingCart.getItems()); //Tests trying to buy different books
        int[] correctSequence = new int[]{0, 1, 0, 2};

        for (int i = 0; i < purchaseStatus.length; i++) {
            TestCase.assertTrue(purchaseStatus[i] == correctSequence[i]);
        }

        TestCase.assertTrue(bookstoreService.getTotalToPay().equals(new BigDecimal("2510.00"))); //Total of all OK codes purchases
    }








}
