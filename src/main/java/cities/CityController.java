package cities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Configuration
public class CityController {

    private CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route(RequestPredicates.GET("/api/cities/{name}"), this::findByName)
                .andRoute(RequestPredicates.GET("/api/cities/temperatures/{name}"), this::findByNameWithTemperature);
    }

    public Mono<ServerResponse> findByName(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(cityService.findByName(request.pathVariable("name")), City.class);
    }

    public Mono<ServerResponse> findByNameWithTemperature(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(cityService.findByNameWithTemperature(request.pathVariable("name")), City.class);
    }
}
