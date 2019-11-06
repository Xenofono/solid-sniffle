package com.sti.project.projekt.blog.repositories;

import com.sti.project.projekt.blog.entities.BlogEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends ReactiveCrudRepository<BlogEntity, Long> {
}
