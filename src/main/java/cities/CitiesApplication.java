package cities;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import io.r2dbc.h2.H2ConnectionFactory;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
@EnableR2dbcRepositories
@Slf4j
public class CitiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CitiesApplication.class, args);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        var connectionFactory = new H2ConnectionFactory(
                H2ConnectionConfiguration.builder()
                        .url("mem:testdb;DB_CLOSE_DELAY=-1;")
                        .username("sa")
                        .build()
        );

        var client = DatabaseClient.create(connectionFactory);

        log.info("Init database...");

        client.execute("CREATE TABLE cities (id INT AUTO_INCREMENT, name VARCHAR(50), lat DOUBLE, lon DOUBLE)")
                .then()
                .subscribe();

        try (var cities = new BufferedReader(
                new InputStreamReader(
                        CitiesApplication.class.getResourceAsStream("/cities.csv")))
				.lines()
                .skip(1)
                .map(City::parse)) {

            client
                    .insert()
                    .into(City.class)
                    .using(Flux.fromStream(cities))
                    .fetch()
                    .all()
                    .subscribe();
        }

        client.select()
                .from(City.class)
                .fetch()
                .first()
                .map(City::toString)
                .subscribe(log::info);

        return connectionFactory;
    }

}
