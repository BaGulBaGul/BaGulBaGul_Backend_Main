package com.BaGulBaGul.BaGulBaGul.global;

import com.BaGulBaGul.BaGulBaGul.domain.event.Category;
import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.CategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.post.*;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.*;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InitDummyDB {

    private final PostLikeRepository postLikeRepository;
    private final CategoryRepository categoryRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCommentLikeRepository postCommentLikeRepository;
    private final PostCommentChildRepository postCommentChildRepository;
    private final PostCommentChildLikeRepository postCommentChildLikeRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RecruitmentRepository recruitmentRepository;

    private final EventService eventService;
    private final RecruitmentService recruitmentService;

    private List<User> users;
    private List<Category> categories;
    private List<Event> events;

    @Transactional
    public void init(){
        users = createUser(10);
        categories = createCategory();
        events = createEvent(10);
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

    private List<Category> createCategory() {
        String[] names = {"문화/예술","공연전시/행사","식품/음료","교육/체험","스포츠/레저"};
        List<Category> result = new ArrayList<>();
        for(String name:names){
            Category category = new Category(name);
            categoryRepository.save(
                    category
            );
            result.add(category);
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
            Set<Category> categorySet = new HashSet<>();
            for(int i=0;i<categoryCnt;i++) {
                categorySet.add(categories.get(rand.nextInt(categories.size())));
            }
            List<String> categoryNames = categorySet.stream().map((category) -> category.getName()).collect(Collectors.toList());

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
                            .categories(categoryNames)
                            .imageIds(null)
                            .build()
            );

            Event event = eventRepository.findById(eventId).get();
            Post post = event.getPost();
            initPost(post);

            //모집글
            int recruitmentCnt = rand.nextInt(10);
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
            Post post = recruitment.getPost();
            initPost(post);
        }
        return result;
    }

    public void initPost(Post post) {
        Random rand = new Random();

        //댓글, 대댓글
        int commentCount = rand.nextInt(30);
        createComment(post, commentCount);

        //좋아요
        int likeCount = rand.nextInt(users.size());
        Set<User> likeUsers = new HashSet<>();
        for(int i=0;i<likeCount;i++) {
            //자기가 쓴 글이 아니라면 추가
            User temp = users.get(rand.nextInt(users.size()));
            if(temp != post.getUser()) {
                likeUsers.add(users.get(rand.nextInt(users.size())));
            }
        }
        for(User user : likeUsers) {
            postLikeRepository.save(
                    new PostLike(post, user)
            );
        }
        post.setLikeCount(likeUsers.size());
    }

    public void createComment(Post post, int commentCount) {
        Random rand = new Random();
        //댓글
        for (int cnt = 0; cnt < commentCount; cnt++) {
            //댓글 생성
            PostComment postComment = PostComment.builder()
                    .user(users.get(rand.nextInt(users.size())))
                    .post(post)
                    .content("테스트댓글" + cnt)
                    .build();
            postCommentRepository.save(
                    postComment
            );
            //대댓글 50%확률로 생성
            if(rand.nextBoolean()) {
                int commentChildCount = rand.nextInt(10);
                createCommentChild(postComment, commentChildCount);
                postComment.setCommentChildCount(commentChildCount);
            }
            //댓글 좋아요 추가
            int likeCount = rand.nextInt(users.size());
            Set<User> likeUsers = new HashSet<>();
            for(int i=0;i<likeCount;i++) {
                //자기가 쓴 글이 아니라면 추가
                User temp = users.get(rand.nextInt(users.size()));
                if(temp != postComment.getUser()) {
                    likeUsers.add(temp);
                }
            }
            for(User user : likeUsers) {
                postCommentLikeRepository.save(new PostCommentLike(postComment, user));
            }
            postComment.setLikeCount(likeUsers.size());
        }
        post.setCommentCount(commentCount);
    }
    public void createCommentChild(PostComment postComment, int commentChildCount) {
        Random rand = new Random();
        for(int cnt=0;cnt<commentChildCount;cnt++) {
            PostCommentChild postCommentChild = postCommentChildRepository.save(
                    PostCommentChild.builder()
                            .user(users.get(rand.nextInt(users.size())))
                            .postComment(postComment)
                            .content("테스트대댓글" + cnt)
                            .build()
            );
            //대댓글 좋아요 추가
            int likeCount = rand.nextInt(users.size());
            Set<User> likeUsers = new HashSet<>();
            for(int i=0;i<likeCount;i++) {
                //자기가 쓴 글이 아니라면 추가
                User temp = users.get(rand.nextInt(users.size()));
                if(temp != postCommentChild.getUser()) {
                    likeUsers.add(temp);
                }
            }
            for(User user : likeUsers) {
                postCommentChildLikeRepository.save(new PostCommentChildLike(postCommentChild, user));
            }
            postCommentChild.setLikeCount(likeUsers.size());
        }
    }
}
