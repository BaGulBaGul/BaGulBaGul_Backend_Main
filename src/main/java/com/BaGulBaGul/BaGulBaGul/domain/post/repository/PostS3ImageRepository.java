package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostS3Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostS3ImageRepository extends JpaRepository<PostS3Image, String> {

    @Query(value = "SELECT psi from PostS3Image psi where psi.post = :post")
    List<PostS3Image> findImageByPost(@Param("post") Post post);
}
