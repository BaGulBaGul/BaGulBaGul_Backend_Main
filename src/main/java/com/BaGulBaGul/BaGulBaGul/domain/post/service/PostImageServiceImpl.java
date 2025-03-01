package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostImage;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostImageRepository;
import com.BaGulBaGul.BaGulBaGul.domain.upload.Resource;
import com.BaGulBaGul.BaGulBaGul.domain.upload.exception.ResourceNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.upload.repository.ResourceRepository;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.ResourceService;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.TransactionResourceService;
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
public class PostImageServiceImpl implements PostImageService {

    private final PostImageRepository postImageRepository;
    private final ResourceService resourceService;
    private final TransactionResourceService transactionResourceService;
    private final ResourceRepository resourceRepository;

    /*
     * post와 연결된 PostImage를 순서대로 정렬해서 반환
     */
    @Override
    @Transactional
    public List<PostImage> getByOrder(Post post) {
        //post와 연결된 s3이미지 검색
        List<PostImage> images = postImageRepository.findImageByPost(post);
        //이미지를 순서에 따라 정렬
        images.sort((o1, o2) -> o1.getOrder() > o2.getOrder() ? 1 : o1.getOrder() == o2.getOrder() ? 0 : -1);
        return images;
    }

    /*
     * 새로운 이미지 순서를 받아 연결. 이에 따라 PostImage를 삭제하거나 추가하거나 순서를 변경
     * 새로 추가된 이미지는 임시테이블에서 제거
     * 연결이 끊어진 이미지는 비동기로 삭제
     * 그리고 첫번째 이미지로 post의 대표이미지를 설정
     */
    @Override
    @Transactional
    public void setImages(Post post, List<Long> resourceIds) {
        //null은 빈 리스트로 변경
        if(resourceIds == null)
            resourceIds = Collections.EMPTY_LIST;

        //새로운 resource의 id의 순서를 map으로 생성
        Map<Long, Integer> orders = new HashMap<>();
        for(int i = 0; i< resourceIds.size(); i++) {
            orders.put(resourceIds.get(i), i);
        }

        //먼저 모든 resource를 배치로 찾아옴


        // * 기존 이미지 삭제, 순서변경
        //기존에 연결되어 있던 이미지들을 검색
        List<PostImage> originalImages = postImageRepository.findImageByPost(post);
        //기존 이미지들의 resource id를 set으로 생성
        Set<Long> originalResourceIdSet = originalImages.stream().map(x -> x.getResource().getId()).collect(Collectors.toSet());
        //삭제될 이미지들
        List<PostImage> imagesToDelete = new ArrayList<>();

        //기존 이미지를 중에서 남길 이미지와 삭제할 이미지를 찾는다.
        for(PostImage image : originalImages) {
            //남아있을 이미지는 순서만 변경
            if(orders.containsKey(image.getResource().getId())) {
                image.setOrder(orders.get(image.getResource().getId()));
            }
            //남아있지 않다면 삭제 대상
            else{
                imagesToDelete.add(image);
            }
        }

        // * 새로운 이미지 연결, 리소스 임시테이블에서 삭제
        //새로운 이미지들의 resourceId
        List<Long> newResourceIds = new ArrayList<>();
        for(int i = 0; i< resourceIds.size(); i++) {
            Long id = resourceIds.get(i);
            //새로운 이미지라면
            if(!originalResourceIdSet.contains(id)) {
                //새로운 이미지
                newResourceIds.add(id);
                //리소스를 찾아옴. 만약 존재하지 않는다면 ResourceNotFoundException
                Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
                //PostS3Image를 저장
                postImageRepository.save(
                        PostImage.builder()
                                .post(post)
                                .resource(resource)
                                .order(i)
                                .build()
                );
            }
        }
        //새로운 이미지는 임시저장 테이블에서 제거
        resourceService.cancelTempResources(newResourceIds);

        // * 대표이미지 설정
        //첫번째 이미지를 대표이미지로 설정. 작성자는 한명이므로 FK로 인한 데드락은 없다고 가정.
        post.setImage_url(
                resourceIds.isEmpty() ? null : resourceService.getResourceUrlFromId(resourceIds.get(0))
        );

        // * 연결 끊긴 이미지 삭제
        //삭제 대상 이미지들의 resource id값
        List<Long> resourceIdsToDelete = imagesToDelete.stream().map(x -> x.getResource().getId()).collect(Collectors.toList());
        //삭제 대상인 PostS3Image db에서 삭제
        postImageRepository.deleteAllByIdInBatch(resourceIdsToDelete);
        //삭제 대상 리소스를 현재 트랜젝션 커밋 이후에 비동기로 삭제
        transactionResourceService.deleteResourcesAsyncAfterCommit(resourceIdsToDelete);
    }
}
