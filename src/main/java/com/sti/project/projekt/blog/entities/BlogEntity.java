package com.sti.project.projekt.blog.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * The type Blog entity. It represents the java object to be saved in the mongo database.
 * @Author Kristoffer Näsström
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogEntity {

    @Id
    private String id;
    private String creator;
    private String title;
    private String content;
    private LocalDateTime created;
}
