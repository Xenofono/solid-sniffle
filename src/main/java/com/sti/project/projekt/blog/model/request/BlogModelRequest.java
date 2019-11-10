package com.sti.project.projekt.blog.model.request;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * The request model that will be accepted in the HTTP-requestbody. If it passes validation is will create a BlogEntity.
 * @Author Kristoffer Näsström
 */
@Data
@NoArgsConstructor
public class BlogModelRequest {

    @NotNull(message = "Creator can't be null")
    @Size(min = 2, message = "Name must be longer than 2")
    private String creator;
    @NotNull
    private String title;
    @NotNull
    private String content;
    private LocalDateTime created;

    public BlogModelRequest(String creator, String title, String content) {
        this.creator = creator;
        this.title = title;
        this.content = content;
        this.created = LocalDateTime.now();
    }
}
