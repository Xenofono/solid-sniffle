package com.sti.project.projekt.blog.repositories;

import com.sti.project.projekt.blog.entities.BlogEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * BlogRepository is an interface that extends ReactiveMongoRepository, it has BlogEntity as entity and uses String as Id.
 * @Author Kristoffer Näsström
 */
public interface BlogRepository extends ReactiveMongoRepository<BlogEntity, String> {
}
