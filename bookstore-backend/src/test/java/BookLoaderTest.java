import dto.Book;
import dto.BookLibrary;
import interfaces.BookLoader;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import service.BookLoaderImpl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Wärnherr on 2016-08-18.
 */
public class BookLoaderTest {

    private BookLoaderImpl bookLoader;
    private BookLibrary bookLibrary;
    private List<Book> bookList;
    private Map<Book, Integer> inventory;

    @Before
    public void init() {
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/mock-bookstore-backend-config.xml");
//        bookLoader = context.getBean("bookLoader", BookLoaderImpl.class);
        bookLoader = new BookLoaderImpl();
        bookLoader.setBookstoreDataSource("http://www.contribe.se/bookstoredata/bookstoredata.txt");
        bookLibrary = bookLoader.loadBooks(null);
        bookList = bookLibrary.getBookList();
        inventory = bookLibrary.getInventory();
    }

    @Test
    public void testInstantiation() {
        TestCase.assertNotNull(bookLoader);
        TestCase.assertNotNull(bookLibrary);
    }

    @Test
    public void testBookListSize() {
        TestCase.assertNotNull(bookList);
        TestCase.assertEquals(7, bookList.size());
    }

    @Test
    public void testInventorySize() {
        TestCase.assertNotNull(inventory);
        TestCase.assertEquals(7, inventory.size());
    }

    @Test
    public void testBookEquals() {
        Book book7 = new Book(" desired   ", "rIcH BlokE ", new BigDecimal("564.50"));
        TestCase.assertTrue(isBookLoadedCorrectly(book7, 0));
    }


    /*
        - Tests that all books were read from the text file and all fields
        correctly used to create Book objects and added to the list and map.
        - Also tests that a Book instance can be retrieved from the list and the map. I.e
        this tests that the compareTo(), equals() and hashCode() functions work correctly.
        - All books below should pass this test.
     */
    @Test
    public void testCorrectInfoRead() {
        Book book1 = new Book("Mastering åäö", "Average Swede", new BigDecimal("762.00"));
        TestCase.assertTrue(isBookLoadedCorrectly(book1, 15));

        Book book2 = new Book("How To Spend Money", "Rich Bloke", new BigDecimal("1,000,000.00".replaceAll(",", "")));
        TestCase.assertTrue(isBookLoadedCorrectly(book2, 1));

        Book book3 = new Book("Generic Title", "First Author", new BigDecimal("185.50"));
        TestCase.assertTrue(isBookLoadedCorrectly(book3, 5));

        Book book4 = new Book("Generic Title", "Second Author", new BigDecimal("1,748.00".replaceAll(",", "")));
        TestCase.assertTrue(isBookLoadedCorrectly(book4, 3));

        Book book5 = new Book("Random Sales", "Cunning Bastard", new BigDecimal("999.00"));
        TestCase.assertTrue(isBookLoadedCorrectly(book5, 20));

        Book book6 = new Book("Random Sales", "Cunning Bastard", new BigDecimal("499.50"));
        TestCase.assertTrue(isBookLoadedCorrectly(book6, 3));

        Book book7 = new Book("Desired", "Rich Bloke", new BigDecimal("564.50"));
        TestCase.assertTrue(isBookLoadedCorrectly(book7, 0));
    }

    /*
        Condition 1: Book must exist in the list
        Condition 2: The Book object in the list must equal the Book object sought
        Condition 3: The map must contain the sought Book object as a key
        Condition 4: The quantity for the sought Book must equal the expected quantity
     */
    private boolean isBookLoadedCorrectly(Book book, Integer quantity) {
        int index = Collections.binarySearch(bookList, book);  //Condition 1
        System.out.println(bookList.get(index));
        //Condition 2 and Condition 3 and Condition 4
        return book.equals(bookList.get(index)) && inventory.containsKey(book) && inventory.get(book) == quantity;
    }





}
