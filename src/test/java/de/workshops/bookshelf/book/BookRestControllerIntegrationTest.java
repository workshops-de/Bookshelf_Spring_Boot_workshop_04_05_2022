package de.workshops.bookshelf.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRestController bookRestController;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void getAllBooks() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BookRestController.REQUEST_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title", is("Clean Code")))
                .andReturn();

        Book[] books = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Book[].class);
        assertEquals(4, books.length);
        assertEquals("Clean Code", books[2].getTitle());
    }

    @Test
    void testWithRestAssuredMockMvc() {
        RestAssuredMockMvc.standaloneSetup(bookRestController);
        RestAssuredMockMvc.
                given().
                log().all().
                when().
                get(BookRestController.REQUEST_URL).
                then().
                log().all().
                statusCode(200).
                body("author[0]", equalTo("Erich Gamma"));
    }

    @Test
    void testWithRestAssured() {
        RestAssured.
                given().
                log().all().
                when().
                get(BookRestController.REQUEST_URL).
                then().
                log().all().
                statusCode(200).
                body("size()", is(4)).
                body("author[0]", equalTo("Erich Gamma"));
    }

    @Test
    void createBook() {
        RestAssuredMockMvc.standaloneSetup(bookRestController);

        Book book = new Book();
        book.setAuthor("Eric Evans");
        book.setTitle("Domain-Driven Design: Tackling Complexity in the Heart of Software");
        book.setIsbn("978-0321125217");
        book.setDescription("This is not a book about specific technologies. It offers readers a systematic approach to domain-driven design, presenting an extensive set of design best practices, experience-based techniques, and fundamental principles that facilitate the development of software projects facing complex domains.");

        RestAssuredMockMvc.
                given().
                log().all().
                body(book).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                when().
                post("/book").
                then().
                log().all().
                statusCode(200).
                body("author", equalTo("Eric Evans"));
    }
}