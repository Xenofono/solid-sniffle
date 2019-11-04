package com.sti.project.projekt.blog.routers;

import com.sti.project.projekt.blog.handlers.BlogHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class BlogRouter {



    @Bean
    public RouterFunction<ServerResponse> blogFluxRouter(BlogHandler blogHandler) {
        return RouterFunctions.route(GET("/api").and(accept(MediaType.APPLICATION_JSON)), blogHandler::getAllBlogPosts)
                .andRoute(GET("/api/{id}").and(accept(MediaType.APPLICATION_JSON)), blogHandler::getBlogById)
                .andRoute(POST("/api/").and(accept(MediaType.APPLICATION_JSON)), blogHandler::createNewBlog)
                .andRoute(DELETE("/api/{id}").and(accept(MediaType.APPLICATION_JSON)), blogHandler::deleteBlogById)
                .andRoute(PUT("/api/{id}").and(accept(MediaType.APPLICATION_JSON)), blogHandler::updateById);
    }


}
