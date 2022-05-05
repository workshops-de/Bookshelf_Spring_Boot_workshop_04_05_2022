package de.workshops.bookshelf.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    private List<Book> books;

    @PostConstruct
    public void init() {
        books = Collections.emptyList();

        try {
            books = bookRepository.findAll();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public Book getSingleBook(String isbn) throws BookException {
        return books.stream().filter(book -> hasIsbn(book, isbn)).findFirst().orElseThrow(BookException::new);
    }

    public Book searchBookByAuthor(String author) throws BookException {
        return books.stream().filter(book -> hasAuthor(book, author)).findFirst().orElseThrow(BookException::new);
    }

    public List<Book> searchBooks(BookSearchRequest request) {
        return books.stream()
                .filter(book -> hasAuthor(book, request.getAuthor()))
                .filter(book -> hasIsbn(book, request.getIsbn()))
                .toList();
    }

    private boolean hasIsbn(Book book, String isbn) {
        return isbn == null || book.getIsbn().equals(isbn);
    }

    private boolean hasAuthor(Book book, String author) {
        return author == null || book.getAuthor().contains(author);
    }
}
