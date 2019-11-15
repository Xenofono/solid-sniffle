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
import java.util.Comparator;

/**
 * The type Blog handler, this is an implementation of a service layer for the BlogRouter.
 * @Author Kristoffer Näsström
 */
@Component
public class BlogHandler {


    private static Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();
    private BlogRepository blogRepository;

    /**
     * Instantiates a new Blog handler.
     *
     * @param blogRepository the blog repository
     */
    public BlogHandler(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    /**
     * Gets all blog posts.
     *
     * @param request the request from the router
     * @return all blog posts
     */
    public Mono<ServerResponse> getAllBlogPosts(ServerRequest request) {

        Flux<BlogModelResponse> allResponses = blogRepository.findAll().map(entity -> {
            System.out.println(entity);
            System.out.println(entity);
            System.out.println(entity);
            BlogModelResponse response = new BlogModelResponse();
            BeanUtils.copyProperties(entity, response);
            if(response.getContent().length()>= 125){
                response.setContentSummary(response.getContent().substring(0, 125)+"...");
            }
            else{
                response.setContentSummary(response.getContent());
            }
            return response;
        }).log("retrieving: ");
        System.out.println("HEJDÅÅÅ");

        allResponses = allResponses.sort(Comparator.comparing(BlogModelResponse::getId).reversed());

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allResponses, BlogModelResponse.class)
                .switchIfEmpty(NOT_FOUND);
    }

    /**
     * Gets blog by id.
     *
     * @param request which id to search for
     * @return the blog by id, otherwise NOT_FOUND
     */
    public Mono<ServerResponse> getBlogById(ServerRequest request) {
        String id = request.pathVariable("id");
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

    /**
     * Create new blog entity.
     *
     * @param request BlogModelRequest that will be converted to BlogEntity.
     * @return the new saved entity, otherwise NOT_FOUND if save failed.
     */
    public Mono<ServerResponse> createNewBlog(ServerRequest request) {
        Mono<BlogEntity> newEntity = request.bodyToMono(BlogEntity.class);
        System.out.println(newEntity);
        return newEntity.flatMap(savedItem -> {
            savedItem.setCreated(LocalDateTime.now());
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(blogRepository.save(savedItem), BlogEntity.class);

        }).switchIfEmpty(NOT_FOUND);

    }

    /**
     * Delete blog by id mono.
     *
     * @param request which id to delete
     * @return http 200 response with Void body.
     */
    public Mono<ServerResponse> deleteBlogById(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Void> deletedBlog = blogRepository.deleteById(id);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deletedBlog, Void.class);

    }

    /**
     * Update by id mono.
     *
     * @param request which id to update as well as a new BlogModelRequest
     * @return the newly updated BlogEntity converted to BlogModelResponse
     */
    public Mono<ServerResponse> updateById(ServerRequest request) {
        String id = request.pathVariable("id");

        Mono<BlogModelRequest> newBlogDetails = request.bodyToMono(BlogModelRequest.class);

        Mono<BlogEntity> updatedBlog = newBlogDetails.flatMap(newItem -> blogRepository.findById(id).flatMap(oldItem -> {
            if(newItem.getContent() != null && newItem.getCreator() != null && newItem.getTitle() != null){
                oldItem.setCreator(newItem.getCreator());
                oldItem.setContent(newItem.getContent());
                oldItem.setTitle(newItem.getTitle());
                return blogRepository.save(oldItem);
            }
            return blogRepository.findById(id);
        }));
        return updatedBlog.flatMap(blog -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(blog))
                .switchIfEmpty(NOT_FOUND);


    }
}
