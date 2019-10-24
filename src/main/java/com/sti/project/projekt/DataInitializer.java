package com.sti.project.projekt;

import com.sti.project.projekt.entities.BlogEntity;
import com.sti.project.projekt.repositories.BlogRepository;
import io.r2dbc.spi.Clob;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public class DataInitializer implements CommandLineRunner {

    private BlogRepository blogRepository;

    public DataInitializer(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }


    @Override
    public void run(String... args) throws Exception {
//        initializeData();
    }
//
//
//    private void initializeData() {
//        blogRepository.deleteAll()
//                .thenMany(Flux.fromIterable(exampleBlogs))
//                .flatMap(blogRepository::save)
//                .thenMany(blogRepository.findAll())
//                .subscribe(System.out::println);
//
//    }
//
//    private String generateLargeString() {
//        StringBuilder returnValue = new StringBuilder();
//        for (int i = 0; i < 2; i++) {
//            returnValue.append(UUID.randomUUID().toString());
//        }
//        return returnValue.toString();
//    }
}
