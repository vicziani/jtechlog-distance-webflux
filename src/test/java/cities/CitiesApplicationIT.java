package cities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
		classes = CitiesApplication.class)
class CitiesApplicationIT {

	@Autowired
	ApplicationContext applicationContext;

	WebTestClient webClient;

	@BeforeEach
	void initWebClient() {
		webClient = WebTestClient.bindToApplicationContext(applicationContext).build();
	}

	@Test
	void testFindByNameWithTemperature() {
		webClient.get().uri("/api/cities/temperatures/{name}", "Budapest")
				.exchange()
				.expectBody()
				.jsonPath("city.name").isEqualTo("Budapest")
				.jsonPath("temperature").isNotEmpty();
	}

}
