package com.sti.project.projekt.client;

import com.sti.project.projekt.blog.model.response.BlogModelResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller
public class Client {

    private WebClient webClient = WebClient.create("http://localhost:8080/api");


    @GetMapping("/client")
    public String getAllBlogs(Model model) {
        IReactiveDataDriverContextVariable reactiveContext = new ReactiveDataDriverContextVariable(
                webClient.get().uri("/")
                .retrieve()
                .bodyToFlux(BlogModelResponse.class).log("Loading: "));

        model.addAttribute("blogs", reactiveContext);

        return "blogs";
    }

    @GetMapping("/client/{id}")
    public String getBlogById(@PathVariable String id, Model model) {
        IReactiveDataDriverContextVariable reactiveContext = new ReactiveDataDriverContextVariable(
                webClient.get().uri("/{id}", id)
                        .retrieve()
                        .bodyToFlux(BlogModelResponse.class));

        model.addAttribute("blog", reactiveContext);

        return "blog";
    }


}
