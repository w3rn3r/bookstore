package exception;

/**
 * Created with IntelliJ IDEA.
 * User: Wärnherr
 * Date: 2016-08-20
 * Time: 15:11
 */
public class BookstoreServiceException extends RuntimeException {

    private int errorCode;
    private String errorMessage;

    public BookstoreServiceException(){}

    public BookstoreServiceException(int errorCode, String errorMessage) {
        setErrorCode(errorCode);
        setErrorMessage(errorMessage);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
