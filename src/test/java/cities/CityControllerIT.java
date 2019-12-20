package cities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@ContextConfiguration(classes = CityController.class)
public class CityControllerIT {

    @MockBean
    CityService cityService;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testFindByNameWithTemperature() {
        when(cityService.findByNameWithTemperature(anyString()))
                .thenReturn(Mono.just(
                        new CityWithTemperature(
                        new City(1L, "Budapest", 47.4825,19.15933333), "8")
                ));

        webClient.get().uri("/api/cities/temperatures/{name}", "Budapest")
                .exchange()
                .expectBody()
                .jsonPath("city.name").isEqualTo("Budapest")
                .jsonPath("temperature").isEqualTo("8");
    }
}
