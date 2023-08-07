package com.BaGulBaGul.BaGulBaGul.global;

import com.BaGulBaGul.BaGulBaGul.domain.post.*;
import com.BaGulBaGul.BaGulBaGul.domain.post.constant.PostType;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.*;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InitDummyDB {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostCommentRepository postCommentRepository;

    @Autowired
    PostCommentChildRepository postCommentChildRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostCategoryRepository postCategoryRepository;

    private List<User> users;
    private List<Category> categories;
    private List<Post> posts;

    @Transactional
    public void init(){
        users = initUser(10);
        categories = initCategory();
        posts = initPost(30);
    }

    private List<User> initUser(int userCnt) {
        Random rand = new Random();
        List<User> result = new ArrayList<>();

        for(int cnt=0; cnt<userCnt; cnt++) {
            String sex = rand.nextBoolean() ? "man" : "woman";
            User user = User.builder()
                    .sex(sex)
                    .email("test" + cnt + "email.com")
                    .nickName("testUser" + cnt)
                    .imageURL("testImage.png")
                    .build();
            userRepository.save(user);
            result.add(user);
        }

        return result;
    }

    private List<Category> initCategory() {
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

    private List<Post> initPost(int postCnt) {
        Random rand = new Random();
        final String[] tags = {"태그1", "태그2", "태그3", "태그4", "태그5", "태그6", "태그7", "태그8", "태그9"};
        final PostType[] postTypes = {PostType.FESTIVAL, PostType.PARTY, PostType.LOCAL_EVENT};
        List<Post> result = new ArrayList<>();
        for(int cnt = 0; cnt < postCnt; cnt++) {
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

            Post post = Post.builder()
                    .user(users.get(rand.nextInt(users.size())))
                    .title("테스트" + cnt)
                    .tags(tagSet.stream().collect(Collectors.joining(" ")))
                    .type(postTypes[rand.nextInt(postTypes.length)])
                    .startDate(startDate)
                    .endDate(endDate)
                    .headCount(rand.nextInt(100))
                    .image_url("tttt")
                    .content("테스트게시글" + cnt)
                    .build();
            postRepository.save(post);
            int commentCount = 0, likeCount = rand.nextInt(10);//users.size());
            //댓글
            for (int i = 0; i < commentCount; i++) {
                PostComment postComment = PostComment.builder()
                        .user(users.get(rand.nextInt(users.size())))
                        .post(post)
                        .content("테스트댓글" + i)
                        .build();
                postCommentRepository.save(
                        postComment
                );
                //대댓글 존재
                if(rand.nextBoolean()) {
                    int commentChildCount = rand.nextInt(5);
                    postCommentChildRepository.save(
                            PostCommentChild.builder()
                                .user(users.get(rand.nextInt(users.size())))
                                .postComment(postComment)
                                .content("테스트대댓글" + i)
                                .build()
                            );
                }
            }
            //post.setCommentCount(commentCount);

            //좋아요
            Set<User> likeUsers = new HashSet<>();
            for(int i=0;i<likeCount;i++) {
                likeUsers.add(users.get(rand.nextInt(users.size())));
            }
            for(User user : likeUsers) {
                postLikeRepository.save(
                        new PostLike(post, user)
                );
            }
            //post.setLikeCount(likeUsers.size());

            //카테고리
            int categoryCnt = rand.nextInt(5);
            Set<Category> categorySet = new HashSet<>();
            for(int i=0;i<categoryCnt;i++) {
                categorySet.add(categories.get(rand.nextInt(categories.size())));
            }
            for(Category category : categorySet) {
                postCategoryRepository.save(
                        new PostCategory(post, category)
                );
            }
        }
        return result;
    }
}
