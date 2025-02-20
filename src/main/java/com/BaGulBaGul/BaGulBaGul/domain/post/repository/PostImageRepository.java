package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    @Query(value = "SELECT pi from PostImage pi where pi.post = :post")
    List<PostImage> findImageByPost(@Param("post") Post post);
}
