package de.workshops.bookshelf;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("prod")
@Slf4j
class EnvironmentWithActiveProfileTest {

	@Value("${server.port}")
	private int port;

	@Autowired
	Environment environment;

	@Test
	@EnabledIf(expression = "#{environment.acceptsProfiles('prod')}", loadContext = true)
	void verifyProdPort() {
		log.debug("'prod' profile is active: {}", environment.acceptsProfiles(Profiles.of("prod")));
		log.debug("Active profiles: {}", (Object[]) environment.getActiveProfiles());
		log.debug("Default profiles: {}", (Object[]) environment.getDefaultProfiles());

		assertEquals(8090, port);
	}
}
