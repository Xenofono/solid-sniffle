package com.sti.project.projekt.blog.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("blog_entity")
public class BlogEntity {

    @Id
    private Long Id;
    private String creator;
    private String title;
    private String content;
    @CreatedDate
    private LocalDateTime created;
}
