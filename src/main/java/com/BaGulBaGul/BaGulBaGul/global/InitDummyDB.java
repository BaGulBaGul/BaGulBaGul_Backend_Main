package com.BaGulBaGul.BaGulBaGul.global;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventCommentService;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostCommentService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentCommentService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("db-dev")
@RequiredArgsConstructor
public class InitDummyDB implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RecruitmentRepository recruitmentRepository;

    private final EventService eventService;
    private final RecruitmentService recruitmentService;
    private final PostCommentService postCommentService;
    private final EventCommentService eventCommentService;
    private final RecruitmentCommentService recruitmentCommentService;

    private List<User> users;
    private List<String> categoryNames = Arrays.asList("문화/예술","공연전시/행사","식품/음료","교육/체험","스포츠/레저");
    private List<Event> events;

    private static final int USER_COUNT = 10;
    private static final int EVENT_COUNT = 5;
    private static final int RECRUITMENT_COUNT = 5;
    private static final int COMMENT_MAX_COUNT = 10;
    private static final int COMMENTCHILD_MAX_COUNT = 5;


    @Value("${FLAG_INIT_DUMMY_DATA}")
    boolean FLAG_INIT_DUMMY_DB;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(FLAG_INIT_DUMMY_DB) {
            init();
        }
    }

    public void init(){
        users = createUser(USER_COUNT);
        events = createEvent(EVENT_COUNT);
    }

    private List<User> createUser(int userCnt) {
        Random rand = new Random();
        List<User> result = new ArrayList<>();

        for(int cnt=0; cnt<userCnt; cnt++) {
            User user = User.builder()
                    .email("test" + cnt + "email.com")
                    .nickName("testUser" + cnt)
                    .imageURI("testImage.png")
                    .build();
            userRepository.save(user);
            result.add(user);
        }

        return result;
    }

    private List<Event> createEvent(int eventCnt) {
        Random rand = new Random();
        final String[] tags = {"태그1", "태그2", "태그3", "태그4", "태그5", "태그6", "태그7", "태그8", "태그9"};
        final EventType[] eventTypes = {EventType.FESTIVAL, EventType.PARTY, EventType.LOCAL_EVENT};
        List<Event> result = new ArrayList<>();
        for(int cnt = 0; cnt < eventCnt; cnt++) {

            //태그
            Set<String> tagSet = new HashSet<>();
            for (int i = 0; i < rand.nextInt(7); i++) {
                tagSet.add(tags[rand.nextInt(tags.length)]);
            }

            //시간
            LocalDateTime startDate = null;//LocalDateTime.now().minusHours(rand.nextInt(168));
            LocalDateTime endDate = null;
            int r = rand.nextInt(3);
            //기간이 이미 지난 경우
            if(r == 0) {
                startDate = LocalDateTime.now().minusHours(24).minusHours(rand.nextInt(168));
                do {
                    endDate = LocalDateTime.now().minusHours(rand.nextInt(84));
                }
                while(endDate.isBefore(startDate));
            }
            //현재 진행중
            else if(r == 1){
                startDate = LocalDateTime.now().minusHours(rand.nextInt(48));
                endDate = LocalDateTime.now().plusHours(rand.nextInt(96));
            }
            //아직 시작 안함
            else {
                startDate = LocalDateTime.now().plusHours(rand.nextInt(100));
                do {
                    endDate = LocalDateTime.now().plusHours(24).plusHours(rand.nextInt(200));
                }
                while(endDate.isBefore(startDate));
            }

            //위치
            String[] LOCATION_NAMES = {"서울특별시", "경기도", "강원도"};
            final Map<String, String[]> LOCATION_NAMES2 = new HashMap<>();
            LOCATION_NAMES2.put("서울특별시", new String[]{"강남구", "강동구", "강서구", "관악구", "광진구", "구로구", "동대문구", "도봉구", "서대문구", "서초구", "성동구", "용산구", "영등포구", "종로구", "은평구"});
            LOCATION_NAMES2.put("경기도", new String[]{"고양시", "수원시", "성남시", "용인시", "안양시", "안산시", "과천시", "광주시", "군포시", "김포시", "화성시", "평택시", "하남시", "여주시", "부천시", "구리시", "파주시", "의정부시"});
            LOCATION_NAMES2.put("강원도", new String[]{"철원군", "화천군", "양구군", "고성군", "인제군", "속초시", "양양군", "평창군", "춘천시"});

            String l1 = LOCATION_NAMES[rand.nextInt(LOCATION_NAMES.length)];
            String l2 = LOCATION_NAMES2.get(l1)[rand.nextInt(LOCATION_NAMES2.get(l1).length)];
            String fullLocation = l1 + " " + l2;
            String abstractLocation = fullLocation;
            Float latitudeLocation = (float)(rand.nextInt(1000)) / 10;
            Float longitudeLocation = (float)(rand.nextInt(1000)) / 10;

            //작성자
            User writer = users.get(rand.nextInt(users.size()));

            //기타
            EventType type = eventTypes[rand.nextInt(eventTypes.length)];
            int maxHeadCount = rand.nextInt(100);
            String title = "테스트" + cnt;
            String content = "테스트게시글" + cnt;
            List<String> tagStr = tagSet.stream().collect(Collectors.toList());

            //카테고리
            int categoryCnt = rand.nextInt(2);
            Set<String> categoryNameSet = new HashSet<>();
            for(int i=0;i<categoryCnt;i++) {
                categoryNameSet.add(categoryNames.get(rand.nextInt(categoryNames.size())));
            }

            //이벤트 등록
            Long eventId = eventService.registerEvent(
                    writer.getId(),
                    EventRegisterRequest.builder()
                            .type(type)
                            .title(title)
                            .maxHeadCount(maxHeadCount)
                            .fullLocation(fullLocation)
                            .abstractLocation(abstractLocation)
                            .latitudeLocation(latitudeLocation)
                            .longitudeLocation(longitudeLocation)
                            .content(content)
                            .startDate(startDate)
                            .endDate(endDate)
                            .tags(tagStr)
                            .categories(categoryNameSet.stream().collect(Collectors.toList()))
                            .imageIds(null)
                            .build()
            );

            Event event = eventRepository.findById(eventId).get();
            //댓글, 대댓글
            int commentCount = rand.nextInt(COMMENT_MAX_COUNT);
            createEventComment(eventId, commentCount);

            //좋아요
            int likeCount = rand.nextInt(users.size());
            for(int i=0;i<likeCount;i++) {
                //자기가 쓴 글이 아니라면 추가
                User user = users.get(rand.nextInt(users.size()));
                if(user != writer) {
                    try {
                        eventService.addLike(eventId, user.getId());
                    } catch (DuplicateLikeException e) {
                    }
                }
            }

            //모집글
            int recruitmentCnt = rand.nextInt(RECRUITMENT_COUNT);
            createRecruitment(event, recruitmentCnt);
        }
        return result;
    }

    private List<Recruitment> createRecruitment(Event event, int recruitmentCnt) {
        Random rand = new Random();
        final String[] tags = {"태그1", "태그2", "태그3", "태그4", "태그5", "태그6", "태그7", "태그8", "태그9"};
        List<Recruitment> result = new ArrayList<>();
        for(int cnt = 0; cnt < recruitmentCnt; cnt++) {
            //태그
            Set<String> tagSet = new HashSet<>();
            for (int i = 0; i < rand.nextInt(7); i++) {
                tagSet.add(tags[rand.nextInt(tags.length)]);
            }

            //시간
            LocalDateTime startDate = null;//LocalDateTime.now().minusHours(rand.nextInt(168));
            LocalDateTime endDate = null;
            int r = rand.nextInt(3);
            //기간이 이미 지난 경우
            if(r == 0) {
                startDate = LocalDateTime.now().minusHours(24).minusHours(rand.nextInt(168));
                do {
                    endDate = LocalDateTime.now().minusHours(rand.nextInt(84));
                }
                while(endDate.isBefore(startDate));
            }
            //현재 진행중
            else if(r == 1){
                startDate = LocalDateTime.now().minusHours(rand.nextInt(48));
                endDate = LocalDateTime.now().plusHours(rand.nextInt(96));
            }
            //아직 시작 안함
            else {
                startDate = LocalDateTime.now().plusHours(rand.nextInt(100));
                do {
                    endDate = LocalDateTime.now().plusHours(24).plusHours(rand.nextInt(200));
                }
                while(endDate.isBefore(startDate));
            }

            //작성자
            User writer = users.get(rand.nextInt(users.size()));

            //기타
            int maxHeadCount = rand.nextInt(100);
            String title = "테스트" + cnt;
            String content = "테스트게시글" + cnt;
            List<String> tagStr = tagSet.stream().collect(Collectors.toList());

            Long recruitmentId = recruitmentService.registerRecruitment(
                    event.getId(),
                    writer.getId(),
                    RecruitmentRegisterRequest.builder()
                            .maxHeadCount(maxHeadCount)
                            .startDate(startDate)
                            .endDate(endDate)
                            .title(title)
                            .content(content)
                            .tags(tagStr)
                            .imageIds(null)
                            .build()
            );

            Recruitment recruitment = recruitmentRepository.findById(recruitmentId).get();

            //댓글, 대댓글
            int commentCount = rand.nextInt(COMMENT_MAX_COUNT);
            createRecruitmentComment(recruitmentId, commentCount);

            //좋아요
            int likeCount = rand.nextInt(users.size());
            for(int i=0;i<likeCount;i++) {
                //자기가 쓴 글이 아니라면 추가
                User user = users.get(rand.nextInt(users.size()));
                if(user != writer) {
                    try {
                        recruitmentService.addLike(recruitmentId, user.getId());
                    } catch (DuplicateLikeException e) {
                    }
                }
            }
        }
        return result;
    }

    public void createEventComment(Long eventId, int commentCount) {
        Random rand = new Random();
        //댓글
        for (int cnt = 0; cnt < commentCount; cnt++) {
            User writer = users.get(rand.nextInt(users.size()));
            //댓글 생성
            Long postCommentId = eventCommentService.registerComment(
                    eventId,
                    writer.getId(),
                    PostCommentRegisterRequest.builder()
                            .content("테스트댓글" + cnt)
                            .build()
            );

            //대댓글 생성
            int commentChildCount = rand.nextInt(COMMENTCHILD_MAX_COUNT);
            createEventCommentChild(postCommentId, commentChildCount);

            //댓글 좋아요 추가
            int likeCount = rand.nextInt(users.size());
            for(int i=0;i<likeCount;i++) {
                //자기가 쓴 글이 아니라면 추가
                User user = users.get(rand.nextInt(users.size()));
                if(user != writer) {
                    try {
                        eventCommentService.addLikeToComment(postCommentId, user.getId());
                    } catch (DuplicateLikeException e) {
                    }
                }
            }
        }
    }

    public void createRecruitmentComment(Long recruitmentId, int commentCount) {
        Random rand = new Random();
        //댓글
        for (int cnt = 0; cnt < commentCount; cnt++) {
            User writer = users.get(rand.nextInt(users.size()));
            //댓글 생성
            Long postCommentId = recruitmentCommentService.registerComment(
                    recruitmentId,
                    writer.getId(),
                    PostCommentRegisterRequest.builder()
                            .content("테스트댓글" + cnt)
                            .build()
            );

            //대댓글 생성
            int commentChildCount = rand.nextInt(COMMENTCHILD_MAX_COUNT);
            createRecruitmentCommentChild(postCommentId, commentChildCount);

            //댓글 좋아요 추가
            int likeCount = rand.nextInt(users.size());
            for(int i=0;i<likeCount;i++) {
                //자기가 쓴 글이 아니라면 추가
                User user = users.get(rand.nextInt(users.size()));
                if(user != writer) {
                    try {
                        recruitmentCommentService.addLikeToComment(postCommentId, user.getId());
                    } catch (DuplicateLikeException e) {
                    }
                }
            }
        }
    }
    public void createEventCommentChild(Long postCommentId, int commentChildCount) {
        Random rand = new Random();
        for(int cnt=0;cnt<commentChildCount;cnt++) {
            User writer = users.get(rand.nextInt(users.size()));
            //대댓글 등록
            Long postCommentChildId = eventCommentService.registerCommentChild(
                    postCommentId,
                    writer.getId(),
                    PostCommentChildRegisterRequest.builder()
                            .content("테스트대댓글" + cnt)
                            .replyTargetPostCommentChildId(null)
                            .build()
            );
            //대댓글 좋아요 추가
            int likeCount = rand.nextInt(users.size());
            for(int i=0;i<likeCount;i++) {
                //자기가 쓴 글이 아니라면 추가
                User user = users.get(rand.nextInt(users.size()));
                if(user != writer) {
                    try {
                        eventCommentService.addLikeToCommentChild(postCommentChildId, user.getId());
                    } catch (DuplicateLikeException e) {
                    }
                }
            }
        }
    }
    public void createRecruitmentCommentChild(Long postCommentId, int commentChildCount) {
        Random rand = new Random();
        for(int cnt=0;cnt<commentChildCount;cnt++) {
            User writer = users.get(rand.nextInt(users.size()));
            //대댓글 등록
            Long postCommentChildId = recruitmentCommentService.registerCommentChild(
                    postCommentId,
                    writer.getId(),
                    PostCommentChildRegisterRequest.builder()
                            .content("테스트대댓글" + cnt)
                            .replyTargetPostCommentChildId(null)
                            .build()
            );
            //대댓글 좋아요 추가
            int likeCount = rand.nextInt(users.size());
            for(int i=0;i<likeCount;i++) {
                //자기가 쓴 글이 아니라면 추가
                User user = users.get(rand.nextInt(users.size()));
                if(user != writer) {
                    try {
                        recruitmentCommentService.addLikeToCommentChild(postCommentChildId, user.getId());
                    } catch (DuplicateLikeException e) {
                    }
                }
            }
        }
    }
}
