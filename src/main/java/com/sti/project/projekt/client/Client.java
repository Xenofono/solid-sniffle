package com.sti.project.projekt.client;

import com.sti.project.projekt.blog.model.request.BlogModelRequest;
import com.sti.project.projekt.blog.model.response.BlogModelResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Client.
 */
@Controller
public class Client {

    private WebClient webClient = WebClient.create("https://cryptic-savannah-22712.herokuapp.com/api");


    @GetMapping("/client")
    public String getAllBlogs(Model model) {
        IReactiveDataDriverContextVariable reactiveContext = new ReactiveDataDriverContextVariable(
                webClient.get().uri("/")
                        .retrieve()
                        .bodyToFlux(BlogModelResponse.class));

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

    @GetMapping("/client/new")
    public String createNewBlog(Model model) {
        model.addAttribute("formblog", new BlogModelRequest());
        return "newBlog";
    }

    @PostMapping("/postblog")
    public String createNew(@ModelAttribute BlogModelRequest newBlog) {
        newBlog.setCreated(LocalDateTime.now());
        webClient.post().uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newBlog), BlogModelRequest.class)
                .retrieve()
                .bodyToMono(BlogModelResponse.class)
                .log("Saved item: ")
                 .subscribe();
        return "redirect:/client";


    }

    @GetMapping("/client/update/{id}")
    public String updateBlog(@PathVariable String id,Model model){
        IReactiveDataDriverContextVariable reactiveContext = new ReactiveDataDriverContextVariable(
                webClient.get().uri("/{id}", id)
                .retrieve()
                .bodyToMono(BlogModelResponse.class)
        );
        model.addAttribute("updateBlog", reactiveContext);

        return "updateBlog";
    }


}
