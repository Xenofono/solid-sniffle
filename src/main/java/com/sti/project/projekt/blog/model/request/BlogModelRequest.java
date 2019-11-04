package com.sti.project.projekt.blog.model.request;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class BlogModelRequest {

    @NotNull(message = "Creator can't be null")
    @Size(min = 2, message = "Name must be longer than 2")
    private String creator;
    @NotNull
    private String content;
    private LocalDateTime created;

    public BlogModelRequest(String creator, String content) {
        this.creator = creator;
        this.content = content;
        this.created = LocalDateTime.now();
    }
}
