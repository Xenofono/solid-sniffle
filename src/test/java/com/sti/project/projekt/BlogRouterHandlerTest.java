package com.sti.project.projekt;

import com.sti.project.projekt.blog.entities.BlogEntity;
import com.sti.project.projekt.blog.model.request.BlogModelRequest;
import com.sti.project.projekt.blog.model.response.BlogModelResponse;
import com.sti.project.projekt.blog.repositories.BlogRepository;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@DirtiesContext
public class BlogRouterHandlerTest {



    @Autowired
    private WebTestClient webTestClient;


    @Autowired
    BlogRepository blogRepository;

    private final String URI = "/api/";



    private List<BlogEntity> data() {
        Lorem lorem = LoremIpsum.getInstance();
        List<BlogEntity> dummyBlogs = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dummyBlogs.add(new BlogEntity(null, lorem.getName(), lorem.getTitle(5), lorem.getWords(500), LocalDateTime.now()));
        }
        dummyBlogs.add(new BlogEntity("abc123", "Kristoffer Näsström", "Min dagbok", "blabla", LocalDateTime.now()));
        return dummyBlogs;
    }

    @BeforeEach
    public void setup() {
        blogRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(blogRepository::save)
                .doOnNext(item -> System.out.println("Item is: " + item))
                .blockLast();
    }

    @Test
    public void getAllBlogs() {


        webTestClient.get().uri(URI)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BlogModelResponse.class)
                .hasSize(8);
    }

    @Test
    public void getBlogById__success(){
        webTestClient.get().uri(URI.concat("{id}"), "abc123")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BlogModelResponse.class)
                .consumeWith(blog -> Assertions.assertEquals("Kristoffer Näsström", blog.getResponseBody().getCreator()));

    }

    @Test
    public void getBlogById__unsuccessful(){
        webTestClient.get().uri(URI.concat("{id}"), "cba321")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    public void createBlog__successful(){
        BlogModelRequest request = new BlogModelRequest("Bara Testar", "testi", "lalalallala");

        webTestClient.post().uri(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), BlogModelRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BlogModelResponse.class)
                .consumeWith(newBlog -> {
                    Assertions.assertEquals("Bara Testar", newBlog.getResponseBody().getCreator());
                    Assertions.assertEquals("lalalallala", newBlog.getResponseBody().getContent());
                });
    }

    @Test
    public void createBlog__notEqual(){
        BlogModelRequest request = new BlogModelRequest("Bara Testar", "testi", "lalalallala");

        webTestClient.post().uri(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), BlogModelRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BlogModelResponse.class)
                .consumeWith(newBlog -> {
                    Assertions.assertNotEquals("Felaktig titel", newBlog.getResponseBody().getCreator());
                    Assertions.assertNotEquals("inte lalalallala", newBlog.getResponseBody().getContent());
                });
    }

    @Test
    public void deleteBlog__success(){

        webTestClient.delete().uri(URI.concat("{id}"), "abc123")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Void.class);

        webTestClient.get().uri(URI.concat("{id}"), "abc123")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    public void updateBlog_success(){
        BlogModelRequest updateRequest = new BlogModelRequest();
        updateRequest.setCreator("Testy Mctest");
        updateRequest.setTitle("New title");
        updateRequest.setContent("I'm just a test");

        LocalDateTime timeThatWontBeUpdated = LocalDateTime.of(1987, 10, 14, 1, 23);
        updateRequest.setCreated(timeThatWontBeUpdated);

        webTestClient.put().uri(URI.concat("{id}"), "abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), BlogModelRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.creator").isEqualTo("Testy Mctest")
                .jsonPath("$.content").isEqualTo("I'm just a test")
                .consumeWith(updatedEntity -> Assertions.assertNotEquals(timeThatWontBeUpdated, data().get(7).getCreated()));

    }


}
