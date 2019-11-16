package com.sti.project.projekt.client;

import com.sti.project.projekt.blog.model.request.BlogModelRequest;
import com.sti.project.projekt.blog.model.response.BlogModelResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    private WebClient webClient = WebClient.create("http://localhost:8080/api");


    @GetMapping("/client")
    public Mono<String> getAllBlogs(Model model) {
        IReactiveDataDriverContextVariable reactiveContext = new ReactiveDataDriverContextVariable(
                webClient.get().uri("/")
                        .retrieve()
                        .bodyToFlux(BlogModelResponse.class));

        model.addAttribute("blogs", reactiveContext);

        return Mono.just("blogs");
    }

    @GetMapping("/client/{id}")
    public Mono<String> getBlogById(@PathVariable String id, Model model) {
        IReactiveDataDriverContextVariable reactiveContext = new ReactiveDataDriverContextVariable(
                webClient.get().uri("/{id}", id)
                        .retrieve()
                        .bodyToFlux(BlogModelResponse.class));

        model.addAttribute("blog", reactiveContext);

        return Mono.just("blog");
    }

    @GetMapping("/client/new")
    public String createNewBlog(Model model) {
        model.addAttribute("formblog", new BlogModelRequest());
        return "newBlog";
    }

    @PostMapping("/postblog")
    public Mono<String> createNew(@ModelAttribute BlogModelRequest newBlog) {
        newBlog.setCreated(LocalDateTime.now());
        return webClient.post().uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newBlog), BlogModelRequest.class)
                .retrieve()
                .bodyToMono(BlogModelResponse.class)
                .map(redirectBlog -> "redirect:/client/" + redirectBlog.getId());

    }

    @GetMapping("/client/update/{id}")
    public Mono<String> updateBlog(@PathVariable String id,Model model){
        IReactiveDataDriverContextVariable reactiveContext = new ReactiveDataDriverContextVariable(
                webClient.get().uri("/{id}", id)
                        .retrieve()
                        .bodyToFlux(BlogModelResponse.class));

        model.addAttribute("oldBlogs", reactiveContext);
        model.addAttribute("updatedBlog", new BlogModelRequest());

        return Mono.just("updateBlog");
    }

    @PostMapping("/updateblog/{id}")
    public Mono<String> updateSend(@ModelAttribute BlogModelRequest updatedBlog, @PathVariable String id) {
        System.out.println(updatedBlog);
        return webClient.put().uri("/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedBlog), BlogModelRequest.class)
                .retrieve()
                .bodyToMono(BlogModelResponse.class)
                .log("Updated: ")
                .map(updated -> "redirect:/client/"+id);

    }

    @GetMapping("/client/delete/{id}")
    public String deleteById(@PathVariable String id){
        webClient.delete().uri("/{id}", id)
                .retrieve()
        .bodyToMono(Void.class).log("Deleted: ").subscribe();

        return "redirect:/client";
    }


}
