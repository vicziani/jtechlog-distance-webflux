package cities;

import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CityRepository extends ReactiveCrudRepository<City, Long> {

    @Query("select id, name, lat, lon from cities where name = :name")
    Mono<City> findByName(String name);
}
