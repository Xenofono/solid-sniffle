package com.sti.project.projekt.blog.repositories;

import com.sti.project.projekt.blog.entities.BlogEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


public interface BlogRepository extends ReactiveMongoRepository<BlogEntity, String> {
}
