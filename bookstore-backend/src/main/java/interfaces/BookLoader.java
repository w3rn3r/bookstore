package interfaces;

import dto.Book;
import dto.BookLibrary;
import exception.BookReaderException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Wärnherr on 2016-08-16.
 */

@Service("bookLoader")
public interface BookLoader {

    public BookLibrary loadBooks(String location) throws BookReaderException;
}
