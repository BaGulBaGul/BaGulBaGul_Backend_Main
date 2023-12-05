package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostS3Image;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostS3ImageRepository;
import com.BaGulBaGul.BaGulBaGul.global.upload.repository.S3TempResourceRepository;
import com.BaGulBaGul.BaGulBaGul.global.upload.service.ResourceService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostS3ImageService implements PostImageService {

    private final PostS3ImageRepository postS3ImageRepository;
    private final ResourceService resourceUploadService;
    private final S3TempResourceRepository s3TempResourceRepository;

    /*
     * post와 연결된 이미지들의 url을 순서대로 정렬해서 반환
     */
    @Override
    @Transactional
    public List<String> getImageKeys(Post post) {
        //post와 연결된 s3이미지 검색
        List<PostS3Image> images = postS3ImageRepository.findImageByPost(post);
        //이미지를 순서에 따라 정렬
        images.sort((o1, o2) -> o1.getOrder() > o2.getOrder() ? 1 : o1.getOrder() == o2.getOrder() ? 0 : -1);
        //이미지의 키만 추출해서 반환
        return images.stream()
                .map(image -> image.getKey())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getImageUrls(List<String> keys) {
        return keys.stream()
                .map(key -> resourceUploadService.getResourceUrlFromKey(key))
                .collect(Collectors.toList());
    }

    /*
     * 새로운 이미지 순서를 받아 연결. 이에 따라 PostS3Image를 삭제하거나 추가하거나 순서를 변경
     * 추가된 이미지는 s3 임시테이블에서 제거
     * 연결이 끊어진 이미지는 s3에서 비동기로 삭제
     * 그리고 첫번째 이미지로 post의 대표이미지를 설정
     */
    @Override
    @Transactional
    public void setImages(Post post, List<String> keys) {
        //null은 빈 리스트로 변경
        if(keys == null)
            keys = Collections.EMPTY_LIST;

        //새로운 이미지의 키의 순서를 map으로 생성
        Map<String, Integer> orders = new HashMap<>();
        for(int i=0;i<keys.size();i++) {
            orders.put(keys.get(i), i);
        }


        // * 기존 이미지 삭제, 순서변경
        //기존에 연결되어 있던 이미지들을 검색
        List<PostS3Image> originalImages = postS3ImageRepository.findImageByPost(post);
        //기존 이미지의 키를 set으로 생성
        Set<String> originalImageKeySet = originalImages.stream().map(PostS3Image::getKey).collect(Collectors.toSet());
        //삭제될 이미지들(삭제 대상은 여기서 찾지만 rollback을 대비해서 s3에 삭제 요청은 마지막에 보냄.
        List<PostS3Image> imagesToDelete = new ArrayList<>();
        //기존 이미지를 중에서 남길 이미지와 삭제할 이미지를 찾는다.
        for(PostS3Image image : originalImages) {
            //남아있을 이미지는 순서만 변경
            if(orders.containsKey(image.getKey())) {
                image.setOrder(orders.get(image.getKey()));
            }
            //남아있지 않다면 삭제 대상
            else{
                imagesToDelete.add(image);
            }
        }

        // * 새로운 이미지 연결, s3 임시테이블에서 삭제
        //새로운 이미지들의 키값
        List<String> newImageKeys = new ArrayList<>();
        for(int i=0;i<keys.size();i++) {
            String key = keys.get(i);
            //새로운 이미지라면
            if(!originalImageKeySet.contains(key)) {
                //새로운 이미지
                newImageKeys.add(key);
                //PostS3Image를 저장
                postS3ImageRepository.save(
                        PostS3Image.builder()
                                .post(post)
                                .key(key)
                                .order(i)
                                .build()
                );
            }
        }
        //새로운 이미지는 임시저장 테이블에서 제거
        s3TempResourceRepository.deleteAllByIdInBatch(newImageKeys);

        // * 대표이미지 설정
        //첫번째 이미지를 대표이미지로 설정. 작성자는 한명이므로 FK로 인한 데드락은 없다고 가정.
        post.setImage_url(
                keys.isEmpty() ? null : resourceUploadService.getResourceUrlFromKey(keys.get(0))
        );

        // * 연결 끊긴 이미지 삭제
        //삭제 대상 이미지들의 키값
        List<String> imageKeysToDelete = imagesToDelete.stream().map(PostS3Image::getKey).collect(Collectors.toList());
        //삭제 대상인 PostS3Image db에서 삭제
        postS3ImageRepository.deleteAllByIdInBatch(imageKeysToDelete);
        //s3에 삭제 요청을 보내기 전에 db요청을 전부 보내서 삭제 후에 db가 rollback되는 상황을 방지
        postS3ImageRepository.flush();
        //s3에 있는 삭제 대상 이미지를 비동기로 삭제
        resourceUploadService.deleteResourcesAsync(imageKeysToDelete);
    }
}
