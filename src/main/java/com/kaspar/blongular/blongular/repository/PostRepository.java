package com.kaspar.blongular.blongular.repository;

import com.kaspar.blongular.blongular.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {



}
