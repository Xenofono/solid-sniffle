package com.sti.project.projekt.routers;

import com.sti.project.projekt.handlers.BlogHandler;
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
        return RouterFunctions.route(GET("/").and(accept(MediaType.APPLICATION_JSON)), blogHandler::getAllBlogPosts)
                .andRoute(GET("/{id}").and(accept(MediaType.APPLICATION_JSON)), blogHandler::getBlogById)
                .andRoute(POST("/").and(accept(MediaType.APPLICATION_JSON)), blogHandler::createNewBlog)
                .andRoute(DELETE("/{id}").and(accept(MediaType.APPLICATION_JSON)), blogHandler::deleteBlogById)
                .andRoute(PUT("/{id}").and(accept(MediaType.APPLICATION_JSON)), blogHandler::updateById);
    }


}
