package de.workshops.bookshelf;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.workshops.bookshelf.book.BookRepository;
import de.workshops.bookshelf.book.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class RepositoryInitializationTest {

    @Autowired(required = false)
    private BookRepository bookRepository;

    @Autowired(required = false)
    private BookService bookService;

    @MockBean
    private ObjectMapper objectMapper;

    @Test
    void initalizeRepository() {
        assertNotNull(bookRepository);
        assertNull(bookService);
    }
}
