package exception;

/**
 * Created with IntelliJ IDEA.
 * User: Wärnherr
 * Date: 2016-08-20
 * Time: 15:08
 */
public class BookstoreSearchException extends BookstoreServiceException {

    public BookstoreSearchException(){
        super();
    }

    public BookstoreSearchException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

}
