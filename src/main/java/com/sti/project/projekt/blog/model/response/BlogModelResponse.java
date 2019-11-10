package com.sti.project.projekt.blog.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

/**
 * The response model that will be returned by HTTP.
 * @Author Kristoffer Näsström
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogModelResponse {

    private String id;
    private String creator;
    private String title;
    private String content;
    private LocalDateTime created;

    private String contentSummary;

    public BlogModelResponse(String creator, String title, String content, LocalDateTime created) {
        this.creator = creator;
        this.title = title;
        this.content = content;
        this.created = created;
    }
}
