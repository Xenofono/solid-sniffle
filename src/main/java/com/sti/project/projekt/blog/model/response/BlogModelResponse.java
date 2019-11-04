package com.sti.project.projekt.blog.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogModelResponse {

    private String creator;
    private String content;
    private LocalDateTime created;
}
