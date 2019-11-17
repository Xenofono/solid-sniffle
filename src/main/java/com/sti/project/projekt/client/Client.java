package com.sti.project.projekt.client;

import com.sti.project.projekt.blog.model.request.BlogModelRequest;
import com.sti.project.projekt.blog.model.response.BlogModelResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
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
 * Client class, has all the endpoints the user will use, prepares HTML with Thymeleaf templating engine and returns to user.
 * @author Kristoffer Näsström
 */
@Controller
public class Client {

    private String env;
    private WebClient webClient;

    public Client() {
        this.env = System.getProperty("os.name");
        setEnvironment();
    }


    /**
     * If env is Windows 10, then run locally, otherwise it's running live.
     */
    private void setEnvironment() {
        System.out.println(this.env);
        if(env.equalsIgnoreCase("Windows 10")){
            webClient = WebClient.create("http://localhost:8080/api");
        }
        else{
            webClient = WebClient.create("https://cryptic-savannah-22712.herokuapp.com/api");
        }
    }


    /**
     * gets all blogs from backend and renders them in Thymeleaf
     * @param model to feed into thymeleaf
     * @return Mono<String> with all blogs
     */
    @GetMapping("/client")
    public Mono<String> getAllBlogs(Model model) {
        IReactiveDataDriverContextVariable reactiveContext = new ReactiveDataDriverContextVariable(
                webClient.get().uri("/")
                        .retrieve()
                        .bodyToFlux(BlogModelResponse.class));

        model.addAttribute("blogs", reactiveContext);

        return Mono.just("blogs");
    }

    /**
     * get specific blog by id from backend
     * @param id to search for in backend
     * @param model to load into Thymeleaf
     * @return render HTML with found blog
     */
    @GetMapping("/client/{id}")
    public Mono<String> getBlogById(@PathVariable String id, Model model) {
        IReactiveDataDriverContextVariable reactiveContext = new ReactiveDataDriverContextVariable(
                webClient.get().uri("/{id}", id)
                        .retrieve()
                        .bodyToFlux(BlogModelResponse.class));

        model.addAttribute("blog", reactiveContext);

        return Mono.just("blog");
    }

    /**
     * return page containing form to create new blog
     * @param model empty BlogModelRequest to fill with form details
     * @return page with form
     */
    @GetMapping("/client/new")
    public String createNewBlog(Model model) {
        model.addAttribute("formblog", new BlogModelRequest());
        return "newBlog";
    }

    /**
     * method is activated from the form in newBlog.html, it receives a new BlogModelRequest filled with data from the form.
     * The new object is sent to the backend, the method then redirects the browser to the newly created blog.
     * @param newBlog received from the form in newBlog.html
     * @return html page of the new blog
     */
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

    /**
     * takes in id of blog to update and retrieves it, it is sent to updateBlog.html to connect the form to the object
     * to update.
     * @param id which blog to update
     * @param model model to add render with thymeleaf
     * @return html-page connected to the blog to update
     */
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

    /**
     * method is called by the form in updateBlog.html, it makes a put-request to the backend and then redirects
     * the browser to the page of the updated blog
     * @param updatedBlog the updated blog details
     * @param id which blog to update
     * @return the page of the newly updated blog
     */
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

    /**
     * method deletes the blog in the backend using the id accepted as a parameter.
     * @param id which blog to delete
     * @return the starting page
     */
    @GetMapping("/client/delete/{id}")
    public String deleteById(@PathVariable String id){
        webClient.delete().uri("/{id}", id)
                .retrieve()
        .bodyToMono(Void.class).log("Deleted: ").subscribe();

        return "redirect:/client";
    }


}
