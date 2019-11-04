package com.sti.project.projekt;

import com.sti.project.projekt.blog.entities.BlogEntity;
import com.sti.project.projekt.blog.repositories.BlogRepository;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
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
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.BaseStream;

@Slf4j
@SpringBootApplication
public class ProjektApplication {

    private Lorem lorem = LoremIpsum.getInstance();

    private List<BlogEntity> exampleBlogs = List.of(
            new BlogEntity(null, "Kristoffer Näsström", "Angående räkor", lorem.getWords(500), LocalDateTime.of(2010, Month.JANUARY, 14, 13, 22)),
            new BlogEntity(null, "Kalle Anka", "Jäkla fiskmåsar", lorem.getWords(500), LocalDateTime.now()),
            new BlogEntity(null, "Gunde Svan", "Varför frasses är bäst", lorem.getWords(500), LocalDateTime.of(1987, Month.OCTOBER, 14, 13, 22)));


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
                .thenMany(getBlogs())
                .flatMap(repository::save)
                .subscribe(person -> log.info("Blog saved: {}", person));
    }

    private Mono<Integer> executeSql(DatabaseClient client, String sql) {
        return client.execute(sql).fetch().rowsUpdated();
    }

    private Flux<BlogEntity> getBlogs() {
        List<BlogEntity> dummyBlogs = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dummyBlogs.add(new BlogEntity(null, lorem.getName(), lorem.getTitle(5), lorem.getWords(500), LocalDateTime.now()));
        }
        return Flux.fromIterable(dummyBlogs);
    }

    private Mono<String> getSchema() throws URISyntaxException {
        Path path = Paths.get(ClassLoader.getSystemResource("schema.sql").toURI());
        return Flux
                .using(() -> Files.lines(path), Flux::fromStream, BaseStream::close)
                .reduce((line1, line2) -> line1 + "\n" + line2);
    }




}
