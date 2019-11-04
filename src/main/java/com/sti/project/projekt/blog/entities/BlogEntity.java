package com.sti.project.projekt.blog.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogEntity {

    @Id
    private Long Id;
    private String creator;

    private String content;
    @CreatedDate
    private LocalDateTime created;
}
