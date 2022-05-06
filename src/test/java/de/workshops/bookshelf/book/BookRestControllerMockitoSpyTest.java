package de.workshops.bookshelf.book;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class BookRestControllerMockitoSpyTest {

    @Autowired
    private BookRestController bookRestController;

    @SpyBean
    private BookService bookService;

    @Test
    void getAllBooks() {
        bookRestController.getAllBooks();
        Mockito.verify(bookService).getBooks();
    }
}
