package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import java.util.List;

public interface PostImageService {
    List<String> getImageUrls(Post post);
    void setImages(Post post, List<String> keys);
}
