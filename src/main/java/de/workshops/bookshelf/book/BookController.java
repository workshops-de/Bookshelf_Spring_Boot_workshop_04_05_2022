package de.workshops.bookshelf.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(BookController.REQUEST_URL)
@RequiredArgsConstructor
public class BookController {

    static final String REQUEST_URL = "/";

    private final BookService bookService;

    @GetMapping
    public String getBooks(Model model) {
        return getBooksView(model);
    }

    @GetMapping("/success")
    public String redirectToSuccessUrl(Model model) {
        return getBooksView(model);
    }

    private String getBooksView(Model model) {
        model.addAttribute("books", bookService.getBooks());

        return "books";
    }
}
