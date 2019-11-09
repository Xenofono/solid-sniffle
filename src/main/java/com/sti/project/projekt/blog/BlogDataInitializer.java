package com.sti.project.projekt.blog;

import com.sti.project.projekt.blog.entities.BlogEntity;
import com.sti.project.projekt.blog.repositories.BlogRepository;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Blog data initializer.
 */
@Component
@Profile({"!dev"})
@Slf4j
public class BlogDataInitializer implements CommandLineRunner {


    private BlogRepository blogRepository;
    private Lorem lorem = LoremIpsum.getInstance();


    public BlogDataInitializer(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        initialDataSetup();
    }




    private List<BlogEntity> data() {
        List<BlogEntity> dummyBlogs = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dummyBlogs.add(new BlogEntity(null, lorem.getName(), lorem.getTitle(5), lorem.getWords(500), LocalDateTime.now()));
        }
        return dummyBlogs;
    }




        private void initialDataSetup() {
        blogRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(blogRepository::save)
                .thenMany(blogRepository.findAll())
                .subscribe(item -> System.out.println("Item inserted from command line runner: " + item));


    }
}

