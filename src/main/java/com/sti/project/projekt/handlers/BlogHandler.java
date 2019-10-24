package com.sti.project.projekt.handlers;

import com.sti.project.projekt.model.response.BlogModelResponse;
import com.sti.project.projekt.repositories.BlogRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BlogHandler {


    private static Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();
    private BlogRepository blogRepository;

    public BlogHandler(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public Mono<ServerResponse> getAllBlogPosts(ServerRequest request) {

        Flux<BlogModelResponse> allResponses = blogRepository.findAll().map(entity -> {
            BlogModelResponse response = new BlogModelResponse();
            BeanUtils.copyProperties(entity, response);
            return response;
        });

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allResponses, BlogModelResponse.class)
                .switchIfEmpty(NOT_FOUND);
    }
}
