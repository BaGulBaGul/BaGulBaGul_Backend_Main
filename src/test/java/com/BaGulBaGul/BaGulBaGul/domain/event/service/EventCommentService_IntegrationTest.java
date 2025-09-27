package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventCommentService_IntegrationTest {

    @Autowired
    EventService eventService;
    @Autowired
    EventCommentService eventCommentService;
    @Autowired
    UserJoinService userJoinService;
    @Autowired
    PostCommentRepository postCommentRepository;
    @Autowired
    PostCommentChildRepository postCommentChildRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("관리자는 다른 사람의 댓글을 수정할 수 있다.")
    @Transactional
    void given_admin_can_modify_others_comment() {
        //given
        User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
        AuthenticatedUserInfo adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

        EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
        Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

        PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("댓글 내용");
        Long commentId = eventCommentService.registerComment(writerInfo, eventId, postCommentRegisterRequest);

        PostCommentModifyRequest postCommentModifyRequest = new PostCommentModifyRequest("수정된 댓글 내용");

        //when
        eventCommentService.modifyComment(adminInfo, commentId, postCommentModifyRequest);

        //then
        em.flush();
        em.clear();
        PostComment postComment = postCommentRepository.findByIdIfNotDeleted(commentId).orElse(null);
        assertThat(postComment.getContent()).isEqualTo(postCommentModifyRequest.getContent());
    }

    @Test
    @DisplayName("관리자는 다른 사람의 댓글을 삭제할 수 있다.")
    @Transactional
    void given_admin_can_delete_others_comment() {
        //given
        User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
        AuthenticatedUserInfo adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

        EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
        Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

        PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("댓글 내용");
        Long commentId = eventCommentService.registerComment(writerInfo, eventId, postCommentRegisterRequest);

        //when
        eventCommentService.deleteComment(adminInfo, commentId);

        //then
        em.flush();
        em.clear();
        PostComment postComment = postCommentRepository.findByIdIfNotDeleted(commentId).orElse(null);

        assertThat(postComment).isNull();
    }

    @Test
    @DisplayName("관리자는 다른 사람의 대댓글을 수정할 수 있다.")
    @Transactional
    void given_admin_can_modify_others_child_comment() {
        //given
        User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
        AuthenticatedUserInfo adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

        EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
        Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

        PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("댓글 내용");
        Long commentId = eventCommentService.registerComment(writerInfo, eventId, postCommentRegisterRequest);

        PostCommentChildRegisterRequest postCommentChildRegisterRequest = new PostCommentChildRegisterRequest("대댓글 내용", null);
        Long childCommentId = eventCommentService.registerCommentChild(writerInfo, commentId, postCommentChildRegisterRequest);

        PostCommentChildModifyRequest postCommentChildModifyRequest = new PostCommentChildModifyRequest("수정된 대댓글 내용",
                JsonNullable.undefined());

        //when
        eventCommentService.modifyCommentChild(adminInfo, childCommentId, postCommentChildModifyRequest);

        //then
        em.flush();
        em.clear();
        PostCommentChild postCommentChild = postCommentChildRepository.findByIdIfNotDeleted(childCommentId).orElse(null);
        assertThat(postCommentChild.getContent()).isEqualTo(postCommentChildModifyRequest.getContent());
    }

    @Test
    @DisplayName("관리자는 다른 사람의 대댓글을 삭제할 수 있다.")
    @Transactional
    void given_admin_can_delete_others_child_comment() {
        //given
        User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
        AuthenticatedUserInfo adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

        EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
        Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

        PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("댓글 내용");
        Long commentId = eventCommentService.registerComment(writerInfo, eventId, postCommentRegisterRequest);

        PostCommentChildRegisterRequest postCommentChildRegisterRequest = new PostCommentChildRegisterRequest("대댓글 내용", null);
        Long childCommentId = eventCommentService.registerCommentChild(writerInfo, commentId, postCommentChildRegisterRequest);

        //when
        eventCommentService.deleteCommentChild(adminInfo, childCommentId);

        //then
        em.flush();
        em.clear();
        PostCommentChild postCommentChild = postCommentChildRepository.findByIdIfNotDeleted(childCommentId).orElse(null);
        assertThat(postCommentChild).isNull();
    }

    @Test
    @DisplayName("일반 유저는 다른 사람의 댓글을 수정할 수 없다.")
    @Transactional
    void given_normal_user_cannot_modify_others_comment() {
        //given
        User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        User normalUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
        AuthenticatedUserInfo normalUserInfo = new AuthenticatedUserInfo(normalUser.getId(), List.of(GeneralRoleType.USER.name()));

        EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
        Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

        PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("댓글 내용");
        Long commentId = eventCommentService.registerComment(writerInfo, eventId, postCommentRegisterRequest);

        PostCommentModifyRequest postCommentModifyRequest = new PostCommentModifyRequest("수정된 댓글 내용");

        //when
        //then
        assertThrows(NoPermissionException.class, () -> {
            eventCommentService.modifyComment(normalUserInfo, commentId, postCommentModifyRequest);
        });
    }

    @Test
    @DisplayName("일반 유저는 다른 사람의 댓글을 삭제할 수 없다.")
    @Transactional
    void given_normal_user_cannot_delete_others_comment() {
        //given
        User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        User normalUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
        AuthenticatedUserInfo normalUserInfo = new AuthenticatedUserInfo(normalUser.getId(), List.of(GeneralRoleType.USER.name()));

        EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
        Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

        PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("댓글 내용");
        Long commentId = eventCommentService.registerComment(writerInfo, eventId, postCommentRegisterRequest);

        //when
        //then
        assertThrows(NoPermissionException.class, () -> {
            eventCommentService.deleteComment(normalUserInfo, commentId);
        });
    }

    @Test
    @DisplayName("일반 유저는 다른 사람의 대댓글을 수정할 수 없다.")
    @Transactional
    void given_normal_user_cannot_modify_others_child_comment() {
        //given
        User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        User normalUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
        AuthenticatedUserInfo normalUserInfo = new AuthenticatedUserInfo(normalUser.getId(), List.of(GeneralRoleType.USER.name()));

        EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
        Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

        PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("댓글 내용");
        Long commentId = eventCommentService.registerComment(writerInfo, eventId, postCommentRegisterRequest);

        PostCommentChildRegisterRequest postCommentChildRegisterRequest = new PostCommentChildRegisterRequest("대댓글 내용", null);
        Long childCommentId = eventCommentService.registerCommentChild(writerInfo, commentId, postCommentChildRegisterRequest);

        PostCommentChildModifyRequest postCommentChildModifyRequest = new PostCommentChildModifyRequest("수정된 대댓글 내용", null);

        //when
        //then
        assertThrows(NoPermissionException.class, () -> {
            eventCommentService.modifyCommentChild(normalUserInfo, childCommentId, postCommentChildModifyRequest);
        });
    }

    @Test
    @DisplayName("일반 유저는 다른 사람의 대댓글을 삭제할 수 없다.")
    @Transactional
    void given_normal_user_cannot_delete_others_child_comment() {
        //given
        User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        User normalUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
        AuthenticatedUserInfo normalUserInfo = new AuthenticatedUserInfo(normalUser.getId(), List.of(GeneralRoleType.USER.name()));

        EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
        Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

        PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("댓글 내용");
        Long commentId = eventCommentService.registerComment(writerInfo, eventId, postCommentRegisterRequest);

        PostCommentChildRegisterRequest postCommentChildRegisterRequest = new PostCommentChildRegisterRequest("대댓글 내용", null);
        Long childCommentId = eventCommentService.registerCommentChild(writerInfo, commentId, postCommentChildRegisterRequest);

        //when
        //then
        assertThrows(NoPermissionException.class, () -> {
            eventCommentService.deleteCommentChild(normalUserInfo, childCommentId);
        });
    }

    @Test
    @DisplayName("작성자는 자신의 댓글을 수정할 수 있다.")
    @Transactional
    void given_writer_can_modify_own_comment() {
        //given
        User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));

        Long eventId = eventService.registerEvent(writerInfo, EventSample.getNormalRegisterRequest(writer.getId()));

        PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("댓글 내용");
        Long commentId = eventCommentService.registerComment(writerInfo, eventId, postCommentRegisterRequest);

        PostCommentModifyRequest postCommentModifyRequest = new PostCommentModifyRequest("수정된 댓글 내용");

        //when
        eventCommentService.modifyComment(writerInfo, commentId, postCommentModifyRequest);

        //then
        em.flush();
        em.clear();
        PostComment postComment = postCommentRepository.findByIdIfNotDeleted(commentId).orElse(null);
        assertThat(postComment.getContent()).isEqualTo(postCommentModifyRequest.getContent());
    }

    @Test
    @DisplayName("작성자는 자신의 댓글을 삭제할 수 있다.")
    @Transactional
    void given_writer_can_delete_own_comment() {
        //given
        User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));

        Long eventId = eventService.registerEvent(writerInfo, EventSample.getNormalRegisterRequest(writer.getId()));

        PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("댓글 내용");
        Long commentId = eventCommentService.registerComment(writerInfo, eventId, postCommentRegisterRequest);

        //when
        eventCommentService.deleteComment(writerInfo, commentId);

        //then
        em.flush();
        em.clear();
        PostComment postComment = postCommentRepository.findByIdIfNotDeleted(commentId).orElse(null);
        assertThat(postComment).isNull();
    }

    @Test
    @DisplayName("작성자는 자신의 대댓글을 수정할 수 있다.")
    @Transactional
    void given_writer_can_modify_own_child_comment() {
        //given
        User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));

        Long eventId = eventService.registerEvent(writerInfo, EventSample.getNormalRegisterRequest(writer.getId()));

        PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("댓글 내용");
        Long commentId = eventCommentService.registerComment(writerInfo, eventId, postCommentRegisterRequest);

        PostCommentChildRegisterRequest postCommentChildRegisterRequest = new PostCommentChildRegisterRequest("대댓글 내용", null);
        Long childCommentId = eventCommentService.registerCommentChild(writerInfo, commentId, postCommentChildRegisterRequest);

        PostCommentChildModifyRequest postCommentChildModifyRequest = new PostCommentChildModifyRequest("수정된 대댓글 내용", JsonNullable.undefined());

        //when
        eventCommentService.modifyCommentChild(writerInfo, childCommentId, postCommentChildModifyRequest);

        //then
        em.flush();
        em.clear();
        PostCommentChild postCommentChild = postCommentChildRepository.findByIdIfNotDeleted(childCommentId).orElse(null);
        assertThat(postCommentChild.getContent()).isEqualTo(postCommentChildModifyRequest.getContent());
    }

    @Test
    @DisplayName("작성자는 자신의 대댓글을 삭제할 수 있다.")
    @Transactional
    void given_writer_can_delete_own_child_comment() {
        //given
        User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));

        Long eventId = eventService.registerEvent(writerInfo, EventSample.getNormalRegisterRequest(writer.getId()));

        PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("댓글 내용");
        Long commentId = eventCommentService.registerComment(writerInfo, eventId, postCommentRegisterRequest);

        PostCommentChildRegisterRequest postCommentChildRegisterRequest = new PostCommentChildRegisterRequest("대댓글 내용", null);
        Long childCommentId = eventCommentService.registerCommentChild(writerInfo, commentId, postCommentChildRegisterRequest);

        //when
        eventCommentService.deleteCommentChild(writerInfo, childCommentId);

        //then
        em.flush();
        em.clear();
        PostCommentChild postCommentChild = postCommentChildRepository.findByIdIfNotDeleted(childCommentId).orElse(null);
        assertThat(postCommentChild).isNull();
    }

    @Nested
    @DisplayName("조회 테스트")
    class EventCommentReadTest {

        User user, user2;
        AuthenticatedUserInfo authenticatedUserInfo, authenticatedUserInfo2;
        Long eventId;

        @BeforeEach
        void setUp() {
            user = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
            authenticatedUserInfo = new AuthenticatedUserInfo(user.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            user2 = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            authenticatedUserInfo2 = new AuthenticatedUserInfo(user2.getId(), List.of(GeneralRoleType.USER.name()));

            eventId = eventService.registerEvent(authenticatedUserInfo, EventSample.getNormalRegisterRequest(user.getId()));
        }

        @Test
        @DisplayName("댓글 페이징 조회 - 삭제된 댓글은 보이지 않아야 함")
        @Transactional
        void getEventCommentPage_deleted() {
            //given
            Long commentId1 = eventCommentService.registerComment(authenticatedUserInfo, eventId, new PostCommentRegisterRequest("test1"));
            Long commentId2 = eventCommentService.registerComment(authenticatedUserInfo2, eventId, new PostCommentRegisterRequest("test2"));
            eventCommentService.deleteComment(authenticatedUserInfo2, commentId2);

            //when
            Page<GetPostCommentPageResponse> page = eventCommentService.getEventCommentPage(eventId, user.getId(), Pageable.unpaged());

            //then
            assertThat(page.getContent().size()).isEqualTo(1);
            assertThat(page.getContent().get(0).getCommentId()).isEqualTo(commentId1);
        }

        @Test
        @DisplayName("대댓글 페이징 조회 - 삭제된 대댓글은 보이지 않아야 함")
        @Transactional
        void getEventCommentChildPage_deleted() {
            //given
            Long commentId = eventCommentService.registerComment(authenticatedUserInfo, eventId, new PostCommentRegisterRequest("test"));
            Long childCommentId1 = eventCommentService.registerCommentChild(authenticatedUserInfo, commentId, new PostCommentChildRegisterRequest("test_child1", null));
            Long childCommentId2 = eventCommentService.registerCommentChild(authenticatedUserInfo2, commentId, new PostCommentChildRegisterRequest("test_child2", null));
            eventCommentService.deleteCommentChild(authenticatedUserInfo2, childCommentId2);

            //when
            Page<GetPostCommentChildPageResponse> page = eventCommentService.getEventCommentChildPage(commentId, null, Pageable.unpaged());

            //then
            assertThat(page.getContent().size()).isEqualTo(1);
            assertThat(page.getContent().get(0).getCommentChildId()).isEqualTo(childCommentId1);
        }
    }
}
