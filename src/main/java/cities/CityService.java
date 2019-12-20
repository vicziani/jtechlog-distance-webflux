package cities;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Scanner;

@Service
public class CityService {

    private CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public Mono<City> findByName(String name) {
        return cityRepository.findByName(name);
    }

    public Mono<CityWithTemperature> findByNameWithTemperature(String name) {
        return cityRepository
                .findByName(name)
                .zipWith(findTemperature(name) , CityWithTemperature::new);
    }

    private Mono<String> findTemperature(String name) {
        return WebClient.create("https://www.idokep.hu/idojaras/".concat(name))
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .flatMapMany(s -> Flux.fromStream(new Scanner(s).findAll("\"homerseklet\">([^<]*)<")))
                .map(m -> m.group(1))
                .next();
    }
}
