package com.sti.project.projekt;

import com.sti.project.projekt.entities.BlogEntity;
import com.sti.project.projekt.repositories.BlogRepository;
import io.r2dbc.spi.Clob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.BaseStream;

@Slf4j
@SpringBootApplication
public class ProjektApplication {

    private List<BlogEntity> exampleBlogs = List.of(new BlogEntity(null, "Kristoffer Näsström", generateLargeString(), LocalDateTime.now()),
            new BlogEntity(null, "Kristoffer Näsström", generateLargeString(), LocalDateTime.now()),
            new BlogEntity(null, "Kristoffer Näsström", generateLargeString(), LocalDateTime.now()));

    public static void main(String[] args) {
        SpringApplication.run(ProjektApplication.class, args);
    }


    @Bean
    public ApplicationRunner seeder(DatabaseClient client, BlogRepository repository) {
        return args -> getSchema()
                .flatMap(sql -> executeSql(client, sql))
                .doOnSuccess(count -> log.info("Schema created, rows updated: {}", count))
                .then(repository.deleteAll())
                .doOnSuccess(v -> log.info("Repository cleared"))
                .thenMany(getPeople())
                .flatMap(repository::save)
                .subscribe(person -> log.info("Person saved: {}", person));
    }

    private Mono<Integer> executeSql(DatabaseClient client, String sql) {
        return client.execute(sql).fetch().rowsUpdated();
        //return client.execute().sql(sql).fetch().rowsUpdated();
    }

    private Flux<BlogEntity> getPeople() {
        return Flux.fromIterable(exampleBlogs);
    }


        private Mono<String> getSchema() throws URISyntaxException {
        Path path = Paths.get(ClassLoader.getSystemResource("schema.sql").toURI());
        return Flux
                .using(() -> Files.lines(path), Flux::fromStream, BaseStream::close)
                .reduce((line1, line2) -> line1 + "\n" + line2);
    }

    private String generateLargeString() {
        StringBuilder returnValue = new StringBuilder();
        for (int i = 0; i < 40; i++) {
            returnValue.append(UUID.randomUUID().toString());
        }
        return returnValue.toString();
    }


}
