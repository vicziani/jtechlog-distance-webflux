package cities;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Configuration
@Slf4j
public class H2Configuration extends AbstractR2dbcConfiguration {

    @Bean
    @Override
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
