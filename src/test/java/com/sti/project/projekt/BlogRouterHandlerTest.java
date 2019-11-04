package com.sti.project.projekt;

import com.sti.project.projekt.entities.BlogEntity;
import com.sti.project.projekt.model.request.BlogModelRequest;
import com.sti.project.projekt.model.response.BlogModelResponse;
import com.sti.project.projekt.repositories.BlogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BlogRouterHandlerTest {



    @Autowired
    private WebTestClient webTestClient;



    @Test
    public void getAllBlogs() {


        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BlogModelResponse.class)
                .hasSize(3);
    }

    @Test
    public void getBlogById__success(){
        webTestClient.get().uri("/19")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BlogModelResponse.class)
                .consumeWith(blog -> Assertions.assertEquals("Kalle Anka", blog.getResponseBody().getCreator()));

    }

    @Test
    public void getBlogById__unsuccessful(){
        webTestClient.get().uri("/46")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    public void createBlog__successful(){
        BlogModelRequest request = new BlogModelRequest("Bara Testar", "lalalallala");

        webTestClient.post().uri("/")
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
        BlogModelRequest request = new BlogModelRequest("Bara Testar", "lalalallala");

        webTestClient.post().uri("/")
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
}
