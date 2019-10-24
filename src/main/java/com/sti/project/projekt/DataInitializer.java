package com.sti.project.projekt;

import com.sti.project.projekt.entities.BlogEntity;
import com.sti.project.projekt.repositories.BlogRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private BlogRepository blogRepository;

    public DataInitializer(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    private List<BlogEntity> exampleBlogs = List.of(new BlogEntity(null, "Kristoffer Näsström", generateLargeString(), LocalDateTime.now()),
            new BlogEntity(null, "Kristoffer Näsström", generateLargeString(), LocalDateTime.now()),
            new BlogEntity(null, "Kristoffer Näsström", generateLargeString(), LocalDateTime.now()));

    @Override
    public void run(String... args) throws Exception {
        exampleBlogs.forEach(blog -> {
            System.out.println("saving: " + blog);
            blogRepository.save(blog);
        });
    }

    private String generateLargeString() {
        StringBuilder returnValue = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            returnValue.append(UUID.randomUUID().toString());
        }
        return returnValue.toString();
    }
}
