package exception;

/**
 * Created with IntelliJ IDEA.
 * User: Wärnherr
 * Date: 2016-08-20
 * Time: 15:10
 */

public class BookstoreBuyException extends BookstoreServiceException {

    public BookstoreBuyException(){}

    public BookstoreBuyException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }



}
