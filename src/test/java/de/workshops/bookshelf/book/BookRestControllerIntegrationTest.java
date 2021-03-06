package de.workshops.bookshelf.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @LocalServerPort
    private int port;

    @TestConfiguration
    static class JacksonConfig {

        @Bean
        public ObjectMapper mapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, false);

            return mapper;
        }
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @WithMockUser
    void getAllBooks() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BookRestController.REQUEST_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title", is("Coding for Fun")))
                .andReturn();

        Book[] books = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Book[].class);
        assertEquals(3, books.length);
        assertEquals("Coding for Fun", books[2].getTitle());
    }

    @Test
    @WithMockUser
    void testWithRestAssuredMockMvc() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders
                        .standaloneSetup(bookRestController)
                        .apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
        );
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
                auth().basic("dbUser", "workshops").
                log().all().
                when().
                get(BookRestController.REQUEST_URL).
                then().
                log().all().
                statusCode(200).
                body("size()", is(3)).
                body("author[0]", equalTo("Erich Gamma"));
    }

    @Test
    @WithMockUser
    void createBook() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders
                        .standaloneSetup(bookRestController)
                        .apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
        );

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
