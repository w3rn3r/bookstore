import dto.Book;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import service.BookstoreService;
import service.ShoppingCart;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Wärnherr
 * Date: 2016-08-20
 * Time: 19:13
 */

@Component
public class BookstoreConsoleApplication {

    public static void main(String[] args) {
        System.out.println("Starting BookstoreConsoleApplication..");
        System.out.println("Loading configurations...");
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        BookstoreConsoleApplication application =  context.getBean(BookstoreConsoleApplication.class);
        application.start(args);
    }

    private BookstoreService bookstoreService;
    private ShoppingCart shoppingCart;
    private Scanner scanner;

    private void start(String[] args) {
        System.out.println("BookstoreConsoleApplication started");
        if (args != null && args.length > 0) {
            System.out.println("Loading books from " + args[0] + "...");
            getBookstoreService().loadBooks(args[0]);
        } else {
            System.out.println("Loading books from...");
            getBookstoreService().loadBooks(null);
        }
        System.out.println(getBookstoreService().getBookList().size() + " titles loaded.\n");
        scanner = new Scanner(System.in);
        mainMenu();
    }

    private void listBooks() {
        System.out.println("List books\nTo list all books in bookstore, please press enter without supplying any search phrase." +
                "\nTo list specific books, please supply full or partial titles or names of the authors. Searching is not case sensitive." +
                "\nPlease press enter or enter search criteria: ");
        scanner.nextLine();
        String searchStr = scanner.nextLine();
        System.out.println("Searching...");
        Book[] searchResults = getBookstoreService().list(searchStr);

        if (searchResults == null || searchResults.length < 1) {
            System.out.println("\"" + searchStr + "\" offered no results." +
                    "\nReturning to main menu...");
            mainMenu();
        }

        System.out.println("Listing results for \"" + searchStr + "\":\n");
        for (int i = 0; i < searchResults.length; i++) {
            System.out.println("[" + (i) + "] " + searchResults[i].toString());
        }
        System.out.println("\nPlease choose an index number to add a book to your shopping cart: ");
        while (scanner.hasNextInt()) {
            int index = scanner.nextInt();
            if (index >= 0 && index < searchResults.length) {
                getShoppingCart().addItem(searchResults[index]);
                System.out.println(searchResults[index] + " added to shopping cart. ");
                System.out.println("Shopping Cart items: " + getShoppingCart().getTotalItems() + " Total Price: " + getShoppingCart().getTotalPrice());
                mainMenu();
            } else {
                System.out.println("Please enter a valid index:\n");
            }
        }
    }


    private void addBooksToStore() {
        System.out.println("Add books to store\nTo add a new book, enter the Title, Author name, price and qunatity on one line separated by a semicolon, e.g:" +
                "\nThe Title;First Author;500.00;3");
        scanner.nextLine();
        String line = scanner.nextLine();
        String[] info = line.split(";");
        if (info == null || info.length != 4) {
            System.out.println("Incorrect format. Select option [2] from main menu and try again.");
            mainMenu();
        }
        Book newBook = new Book();
        newBook.setTitle(info[0]);
        newBook.setAuthor(info[1]);
        newBook.setPrice(new BigDecimal(info[2].replaceAll(",", "")));
        Integer quantity = 0;
        try {
            quantity = Integer.valueOf(info[3]);
        } catch (Exception e) {}
        System.out.println(getBookstoreService().add(newBook,quantity) ? "New book added" : "Failed to add new book.");
        mainMenu();
    }

    private void buyBooks() {
        System.out.println("Buy books\nHere you will buy the books in your shopping cart.");
        System.out.println("Sub-Total: " + getShoppingCart().getTotalPrice());
        System.out.println("Total items: " + getShoppingCart().getTotalItems());
        if (getShoppingCart().getTotalItems() < 1) {
            System.out.println("You have no items in your shopping cart. Add items to purchase under [1]");
            mainMenu();
        }
        Book[] selectedItems = getShoppingCart().getItems();
        int[] purchaseCodes = getBookstoreService().buy(selectedItems);
        HashMap<Integer, String> purchaseMap = new HashMap<Integer, String>();
        purchaseMap.put(0, "OK");
        purchaseMap.put(1, "NOT_IN_STOCK");
        purchaseMap.put(2, "DOES_NOT_EXIST");
        for (int i = 0; i < selectedItems.length; i++) {
            System.out.println("["+purchaseCodes[i]+ "- " + purchaseMap.get(purchaseCodes[i]) + "] " + selectedItems[i]);
        }
        System.out.println("Total: " + getBookstoreService().getTotalToPay());
        getBookstoreService().clearTotal();
        mainMenu();
    }

    private void exitApplication() {
        System.out.println("Exiting BookstoreConsoleApplication...");
        System.out.println("BookstoreConsoleApplication exited.");
        System.exit(0);
    }

    private void openShoppingCart() {
        System.out.println("\nShopping cart");
        System.out.println("Sub-Total: " + getShoppingCart().getTotalPrice());
        System.out.println("Total items: " + getShoppingCart().getTotalItems());
        System.out.println("Current cart contents: ");
        if (getShoppingCart().getTotalItems() > 0) {
            Iterator<Book> itr = getShoppingCart().getItemList().iterator();
            int index = 0;
            while (itr.hasNext()) {
                System.out.println("["+index+"] " + itr.next());
                index++;
            }
            System.out.println("\nTo remove an item from the cart, please enter the corresponding index for the book to remove." +
                    "\nEnter -1 to return to main menu");
            while (scanner.hasNextInt()) {
                int option = scanner.nextInt();
                if (option == -1) {
                    mainMenu();
                }
                if (option >= 0 && option < getShoppingCart().getTotalItems()) {
                    System.out.println(getShoppingCart().removeItem(option) ? "Item removed" : "Failed to remove item");
                    System.out.println("Shopping Cart items: " + getShoppingCart().getTotalItems() + " Total Price: " + getShoppingCart().getTotalPrice());
                    mainMenu();
                } else {
                    System.out.println("Please enter a valid index:\n");
                }
            }
        } else {
            System.out.println("Your cart is empty, to add items navigate to [1] List books");
            mainMenu();
        }
    }


    private void mainMenu() {
        System.out.println("\nMain menu\nPlease choose an action below by typing one of the following numbers: ");
        System.out.println("[1] List books");
        System.out.println("[2] Add books to store");
        System.out.println("[3] Buy books");
        System.out.println("[4] Shopping cart");
        System.out.println("[9] Exit application\n");
        while (scanner.hasNextInt()) {
            int option = scanner.nextInt();
            switch (option) {
                case 1 : listBooks();
                case 2 : addBooksToStore();
                case 3 : buyBooks();
                case 4 : openShoppingCart();
                case 9 : exitApplication();
                default : {
                    System.out.println("An invalid selection was supplied. Please try again: ");
                }
            }
        }
    }

    public BookstoreService getBookstoreService() {
        return bookstoreService;
    }

    public void setBookstoreService(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}
