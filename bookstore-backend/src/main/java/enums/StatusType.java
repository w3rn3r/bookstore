package enums;

/**
 * Created by Wärnherr on 2016-08-17.
 */
public enum StatusType {

    OK(0),
    NOT_IN_STOCK(1),
    DOES_NOT_EXIST(2);

    private int statusCode;

    StatusType(int code) {
        this.statusCode = code;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
