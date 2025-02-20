package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserImage;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserImageRepository;
import com.BaGulBaGul.BaGulBaGul.domain.upload.Resource;
import com.BaGulBaGul.BaGulBaGul.domain.upload.exception.ResourceNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.upload.repository.ResourceRepository;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.ResourceService;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.TransactionResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserImageServiceImpl implements UserImageService {

    private final ResourceRepository resourceRepository;

    private final UserImageRepository userImageRepository;
    private final ResourceService resourceService;
    private final TransactionResourceService transactionResourceService;


    @Override
    @Transactional
    public void setImage(User user, Long resourceId) {
        UserImage userImage = userImageRepository.findByUser(user).orElse(null);
        //이미 존재하면 삭제
        if(userImage != null) {
            //트랜젝션 커밋 이후에 비동기로 이미지 파일 삭제
            transactionResourceService.deleteResourceAsyncAfterCommit(userImage.getResourceId());
            //이미지 정보 db에서 삭제
            userImageRepository.delete(userImage);
        }
        //resourceId가 존재하면 프로필 이미지 연결
        if(resourceId != null) {
            //리소스 검색, 없으면 예외
            Resource resource = resourceRepository.findById(resourceId)
                    .orElseThrow(() -> new ResourceNotFoundException(resourceId));
            //리소스를 유저에 연결
            userImageRepository.save(
                    UserImage.builder()
                            .user(user)
                            .resource(resource)
                            .build()
            );
            //리소스 임시자원 해제
            resourceService.cancelTempResource(resourceId);
        }
        //유저 프로필 이미지 url 변경
        if(resourceId == null) {
            user.setImageURI(null);
        }
        else {
            user.setImageURI(resourceService.getResourceUrlFromId(resourceId));
        }
    }
}
