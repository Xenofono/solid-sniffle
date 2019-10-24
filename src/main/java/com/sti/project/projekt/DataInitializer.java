package com.sti.project.projekt;

import com.sti.project.projekt.model.BlogModel;
import com.sti.project.projekt.repositories.BlogRepository;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DataInitializer implements CommandLineRunner {

    private BlogRepository blogRepository;

    public DataInitializer(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    private List<BlogModel> exampleBlogs = List.of(new BlogModel(null, "Kristoffer Näsström", generateLargeString(), LocalDateTime.now()),
            new BlogModel(null, "Kristoffer Näsström", generateLargeString(), LocalDateTime.now()),
            new BlogModel(null, "Kristoffer Näsström", generateLargeString(), LocalDateTime.now()));

    @Override
    public void run(String... args) throws Exception {
        exampleBlogs.forEach(blogRepository::save);
    }

    private String generateLargeString() {
        StringBuilder returnValue = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            returnValue.append(UUID.randomUUID().toString());
        }
        return returnValue.toString();
    }
}
