package de.workshops.bookshelf;

import de.workshops.bookshelf.config.BookshelfProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import javax.annotation.PostConstruct;

@SpringBootApplication
@ConfigurationPropertiesScan
@Slf4j
public class BookshelfApplication {

	@Autowired(required = false)
	private BookshelfProperties bookshelfProperties;

	public static void main(String[] args) {
		SpringApplication.run(BookshelfApplication.class, args);
	}

	@PostConstruct
	private void printBookshelfProperties() {
		if (bookshelfProperties != null) {
			log.info("Bookshelf properties: {}, {}", bookshelfProperties.getSomeNumber(), bookshelfProperties.getSomeText());
		}
	}
}
