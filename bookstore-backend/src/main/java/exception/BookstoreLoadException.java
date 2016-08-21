package exception;

/**
 * Created with IntelliJ IDEA.
 * User: Wärnherr
 * Date: 2016-08-20
 * Time: 14:56
 */
public class BookstoreLoadException extends BookstoreServiceException {

    public BookstoreLoadException(){
        super();
    }

    public BookstoreLoadException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }


}
