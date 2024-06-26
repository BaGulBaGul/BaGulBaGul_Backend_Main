package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostImage;
import java.util.List;

public interface PostImageService {
    List<PostImage> getByOrder(Post post);
    void setImages(Post post, List<Long> resourceIds);
}
