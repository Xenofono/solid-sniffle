package com.sti.project.projekt.blog.handlers;

import com.sti.project.projekt.blog.entities.BlogEntity;
import com.sti.project.projekt.blog.model.request.BlogModelRequest;
import com.sti.project.projekt.blog.model.response.BlogModelResponse;
import com.sti.project.projekt.blog.repositories.BlogRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

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
            response.setContentSummary(response.getContent().substring(0, 125)+"...");
            return response;
        });

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allResponses, BlogModelResponse.class)
                .switchIfEmpty(NOT_FOUND);
    }

    public Mono<ServerResponse> getBlogById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        System.out.println("HOHOHPO");
        return blogRepository.findById(id).flatMap(entity -> {
            BlogModelResponse response = new BlogModelResponse();
            BeanUtils.copyProperties(entity, response);
            System.out.println(response);
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(response));

        }).switchIfEmpty(NOT_FOUND);


    }

    public Mono<ServerResponse> createNewBlog(ServerRequest request) {
        Mono<BlogEntity> newEntity = request.bodyToMono(BlogEntity.class);

        return newEntity.flatMap(savedItem -> {
            savedItem.setCreated(LocalDateTime.now());
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(blogRepository.save(savedItem), BlogEntity.class);

        }).switchIfEmpty(NOT_FOUND);

    }

    public Mono<ServerResponse> deleteBlogById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<Void> deletedBlog = blogRepository.deleteById(id);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deletedBlog, Void.class);

    }

    public Mono<ServerResponse> updateById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));

        Mono<BlogModelRequest> newBlogDetails = request.bodyToMono(BlogModelRequest.class);

        Mono<BlogEntity> updatedBlog = newBlogDetails.flatMap(newItem -> blogRepository.findById(id).flatMap(oldItem -> {
            oldItem.setCreator(newItem.getCreator());
            oldItem.setContent(newItem.getContent());
            return blogRepository.save(oldItem);
        }));
        return updatedBlog.flatMap(blog -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(blog))
                .switchIfEmpty(NOT_FOUND);


    }
}
