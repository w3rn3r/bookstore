package dto;

import java.math.BigDecimal;

/**
 * Created by Wärnherr on 2016-08-16.
 */
public class Book implements Comparable<Book> {

    private String title;
    private String author;
    private BigDecimal price;

    public Book(){}

    public Book(String title, String author, BigDecimal price){
        setTitle(title);
        setAuthor(author);
        setPrice(price);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        int authorHash = getAuthor().toLowerCase().hashCode();
        int titleHash = getTitle().toLowerCase().hashCode();
        return (authorHash == titleHash) ? authorHash : (authorHash ^ titleHash);
    }

    @Override
    public String toString() {
        return  getTitle() + ", "  + getAuthor() + "; " + getPrice();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Book) {
            Book otherBook = (Book) obj;
            return getTitle().toLowerCase().matches(otherBook.getTitle().toLowerCase())
                    && getAuthor().toLowerCase().matches(otherBook.getAuthor().toLowerCase())
                    && getPrice().equals(otherBook.getPrice());
        }
        return false;
    }

    @Override
    public int compareTo(Book obj) {
        int compAuthor = getAuthor().compareToIgnoreCase(obj.getAuthor());
        if (compAuthor != 0) {
            return compAuthor;
        } else {
            int compTitle = getTitle().compareToIgnoreCase(obj.getTitle());
            return (compTitle != 0) ? compTitle : getPrice().compareTo(obj.getPrice());
        }
    }
}
