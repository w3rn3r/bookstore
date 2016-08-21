package exception;

/**
 * Created by Wärnherr on 2016-08-16.
 */
public class BookReaderException extends RuntimeException {

    private int errorCode;
    private String errorMessage;

    public BookReaderException(){}

    public BookReaderException(int errorCode, String errorMessage) {
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

    public void setErrorMessage(String errorMEssage) {
        this.errorMessage = errorMEssage;
    }


}
