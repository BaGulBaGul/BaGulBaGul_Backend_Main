package com.BaGulBaGul.BaGulBaGul.domain.user.info.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserImage;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserImageRepository;
import com.BaGulBaGul.BaGulBaGul.global.upload.Resource;
import com.BaGulBaGul.BaGulBaGul.global.upload.exception.ResourceNotFoundException;
import com.BaGulBaGul.BaGulBaGul.global.upload.repository.ResourceRepository;
import com.BaGulBaGul.BaGulBaGul.global.upload.service.ResourceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserImageServiceImpl implements UserImageService {

    private final ResourceRepository resourceRepository;

    private final UserImageRepository userImageRepository;
    private final ResourceService resourceService;


    @Override
    @Transactional
    public void setImage(User user, Long resourceId) {
        UserImage userImage = userImageRepository.findByUser(user).orElse(null);
        //이미 존재하면 삭제
        if(userImage != null) {
            userImageRepository.delete(userImage);
            resourceService.deleteResource(resourceId);
        }
        //resourceId가 존재하면 프로필 이미지 연결
        if(resourceId != null) {
            //리소스 검색, 없으면 예외
            Resource resource = resourceRepository.findById(resourceId)
                    .orElseThrow(() -> new ResourceNotFoundException());
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
