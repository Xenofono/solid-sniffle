package com.sti.project.projekt.routers;

import com.sti.project.projekt.handlers.BlogHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class BlogRouter {



    @Bean
    public RouterFunction<ServerResponse> blogFluxRouter(BlogHandler blogHandler) {
        return RouterFunctions.route(GET("/").and(accept(MediaType.APPLICATION_JSON)), blogHandler::getAllBlogPosts)
                .andRoute(GET("/{id}").and(accept(MediaType.APPLICATION_JSON)), blogHandler::getBlogById);
    }


}
