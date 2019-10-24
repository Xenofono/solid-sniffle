package com.sti.project.projekt.repositories;

import com.sti.project.projekt.entities.BlogEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BlogRepository extends ReactiveCrudRepository<BlogEntity, Long> {
}
