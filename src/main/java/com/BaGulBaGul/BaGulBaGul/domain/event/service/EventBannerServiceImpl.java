package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.EventBanner;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventBannerModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.EventNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventBannerRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.upload.Resource;
import com.BaGulBaGul.BaGulBaGul.domain.upload.exception.ResourceNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.upload.repository.ResourceRepository;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.ResourceService;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.TransactionResourceService;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventBannerServiceImpl implements EventBannerService {

    private final EventBannerRepository eventBannerRepository;
    private final ResourceService resourceService;
    private final TransactionResourceService transactionResourceService;
    private final EventRepository eventRepository;
    private final ResourceRepository resourceRepository;

    @Override
    @Transactional
    public void setEventBanners(List<EventBannerModifyRequest> bannerRegisterRequests) {
        //이벤트 배너 전부 검색. key는 id
        Map<Long, EventBanner> eventBanners = eventBannerRepository.findAll()
                .stream().collect(Collectors.toMap(EventBanner::getId, Function.identity()));

        //새로운 자원들
        List<Long> newResourceIds = bannerRegisterRequests.stream()
                .map(EventBannerModifyRequest::getBannerImageResourceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        //변경 후 사용하지 않게 되는 자원 마크
        List<Long> targetBannerIds = bannerRegisterRequests.stream()
                .map(EventBannerModifyRequest::getTargetEventBannerId).collect(Collectors.toList());
        Set<Long> resourceIdsToDelete = eventBanners.values().stream()
                .filter(eventBanner -> targetBannerIds.contains(eventBanner.getId()))
                .map(EventBanner::getBannerImageResource)
                .filter(Objects::nonNull)
                .map(Resource::getId)
                .collect(Collectors.toSet());
        //배너 설정 요청 처리
        for(EventBannerModifyRequest request : bannerRegisterRequests) {
            //배너 검색
            EventBanner eventBanner = eventBanners.get(request.getTargetEventBannerId());
            if(eventBanner == null) {
                throw new GeneralException(ResponseCode.BAD_REQUEST);
            }
            //새로운 이벤트로 변경
            Event newEvent = null;
            if(request.getEventId() != null) {
                newEvent = eventRepository.findById(request.getEventId()).orElseThrow(EventNotFoundException::new);
            }
            eventBanner.setEvent(newEvent);
            //새로운 배너 이미지로 변경
            Resource newResource = null;
            if(request.getBannerImageResourceId() != null) {
                newResource = resourceRepository.findById(request.getBannerImageResourceId())
                        .orElseThrow(() -> new ResourceNotFoundException(request.getBannerImageResourceId()));
                //삭제 예정 자원에서 제외
                resourceIdsToDelete.remove(newResource.getId());
            }
            eventBanner.setBannerImageResource(newResource);
        }

        //배너에 설정 요청한 모든 이미지 자원을 전부 임시 자원에서 해제
        if(!newResourceIds.isEmpty()) {
            resourceService.cancelTempResources(newResourceIds);
        }

        //사용하지 않는 자원을 트랜젝션 이후에 삭제 처리
        if(!resourceIdsToDelete.isEmpty()) {
            transactionResourceService.deleteResourcesAsyncAfterCommit(resourceIdsToDelete);
        }
    }
}