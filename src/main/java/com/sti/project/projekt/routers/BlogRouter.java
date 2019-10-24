package com.sti.project.projekt.routers;

import com.sti.project.projekt.handlers.BlogHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class BlogRouter {

    @Bean
    public RouterFunction<ServerResponse> blogRouter(BlogHandler blogHandler) {

    }
}
