package cities;

import io.r2dbc.h2.H2Connection;
import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Statement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

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
        long start = System.currentTimeMillis();

        client.execute("CREATE TABLE cities (id INT AUTO_INCREMENT, name VARCHAR(50), lat DOUBLE, lon DOUBLE)")
                .then()
                .subscribe();

//
//        var cities = Flux.using(this::lines, Flux::fromStream, BaseStream::close)
//                .skip(1)
//                .map(City::parse);
//        client
//                .insert()
//                .into(City.class)
//                .using(cities)
//                .fetch()
//                .all()
//                .subscribe();

        Flux.from(connectionFactory.create())
                .flatMap(c -> Flux.from(createBatchStatement(c).execute()).doFinally(st -> c.close()))
                .count()
                .subscribe(l -> log.info("Loaded {} cities", l));

        log.info("Db initialized {} ms", System.currentTimeMillis() - start);

        client.select()
                .from(City.class)
                .fetch()
                .first()
                .map(City::toString)
                .subscribe(log::info);

        return connectionFactory;
    }


    private Statement createBatchStatement(H2Connection connection) {
        var lines = Flux.using(this::lines, Flux::fromStream, BaseStream::close);
        var st = connection
                .createStatement("INSERT INTO cities(name, lat, lon) values ($1, $2, $3)");
        lines
                .map(City::parse)
                .map(city -> st
                        .bind("$1", city.getName())
                        .bind("$2", city.getLat())
                        .bind("$3", city.getLon())
                        .add()
                ).subscribe();
        return st;
    }

    private Stream<String> lines() {
        return new BufferedReader(
                new InputStreamReader(
                        CitiesApplication.class.getResourceAsStream("/cities.csv")))
                .lines()
                .skip(1);

        }
}
