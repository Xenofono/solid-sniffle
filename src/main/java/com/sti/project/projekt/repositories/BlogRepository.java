package com.sti.project.projekt.repositories;

import com.sti.project.projekt.model.BlogModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<BlogModel, Long> {
}
