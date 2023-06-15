package com.ll.olol.base.initData;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticleForm;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
@Profile({"dev", "test"})
public class NotProd {

    private final InitService initService;


    @PostConstruct
    //여기에 그냥 코드를 넣어도 될거같은데 스프링 라이프 사이클 때문에 트랜잭션이 잘 처리가 안됨.
    public void NotProd() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;


        private final PasswordEncoder passwordEncoder;

        public void dbInit1() {
            Member member1 = createMember(18, "남자", "홍길동", "1234", "1111@1111", "도사", LocalDateTime.now(),
                    LocalDateTime.now());
            Member member2 = createMember(30, "남자", "김철중", "2345", "1111@2222", "강력반", LocalDateTime.now(),
                    LocalDateTime.now());
            Member member3 = createMember(22, "남자", "송강호", "22544", "1111@3333", "한강", LocalDateTime.now(),
                    LocalDateTime.now());
            Member member4 = createMember(55, "남자", "공유", "2368", "1111@4444", "도깨비", LocalDateTime.now(),
                    LocalDateTime.now());
            Member member5 = createMember(10, "남자", "admin", passwordEncoder.encode("1234"), "1111@5555", "관리자",
                    LocalDateTime.now(),
                    LocalDateTime.now());
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);
            em.persist(member5);

            RecruitmentArticle recruitmentArticle1 = createRecruitmentArticle(LocalDateTime.now(), LocalDateTime.now(),
                    1L, member1, 1, "테스트용1",
                    "테스트용 게시글 내용입니다1.");
            RecruitmentArticle recruitmentArticle2 = createRecruitmentArticle(LocalDateTime.now(), LocalDateTime.now(),
                    3L, member2, 2, "테스트용2",
                    "테스트용 게시글 내용입니다2.");
            RecruitmentArticle recruitmentArticle3 = createRecruitmentArticle(LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1),
                    5L, member3, 1, "테스트용3",
                    "테스트용 게시글 내용입니다3.");
            RecruitmentArticle recruitmentArticle4 = createRecruitmentArticle(LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1), 10L, member4, 2, "테스트용4",
                    "테스트용 게시글 내용입니다4. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac nunc ac ligula ultricies bibendum. Morbi facilisis, justo ac finibus placerat, mauris nisl interdum arcu, non rhoncus lacus leo nec mi. Etiam scelerisque tellus non erat cursus dapibus. Duis tincidunt ullamcorper magna, sit amet auctor ipsum. Curabitur id lacinia nibh. Vestibulum gravida, nulla id faucibus malesuada, lectus metus pharetra velit, id posuere purus felis vitae tellus."
                            +
                            " Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac nunc ac ligula ultricies bibendum. Morbi facilisis, justo ac finibus placerat, mauris nisl interdum arcu, non rhoncus lacus leo nec mi. Etiam scelerisque tellus non erat cursus dapibus. Duis tincidunt ullamcorper magna, sit amet auctor ipsum. Curabitur id lacinia nibh. Vestibulum gravida, nulla id faucibus malesuada, lectus metus pharetra velit, id posuere purus felis vitae tellus."
                            +
                            " Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac nunc ac ligula ultricies bibendum. Morbi facilisis, justo ac finibus placerat, mauris nisl interdum arcu, non rhoncus lacus leo nec mi. Etiam scelerisque tellus non erat cursus dapibus. Duis tincidunt ullamcorper magna, sit amet auctor ipsum. Curabitur id lacinia nibh. Vestibulum gravida, nulla id faucibus malesuada, lectus metus pharetra velit, id posuere purus felis vitae tellus."
                            +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac nunc ac ligula ultricies bibendum. Morbi facilisis, justo ac finibus placerat, mauris nisl interdum arcu, non rhoncus lacus leo nec mi. Etiam scelerisque tellus non erat cursus dapibus. Duis tincidunt ullamcorper magna, sit amet auctor ipsum. Curabitur id lacinia nibh. Vestibulum gravida, nulla id faucibus malesuada, lectus metus pharetra velit, id posuere purus felis vitae tellus."
                            +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac nunc ac ligula ultricies bibendum. Morbi facilisis, justo ac finibus placerat, mauris nisl interdum arcu, non rhoncus lacus leo nec mi. Etiam scelerisque tellus non erat cursus dapibus. Duis tincidunt ullamcorper magna, sit amet auctor ipsum. Curabitur id lacinia nibh. Vestibulum gravida, nulla id faucibus malesuada, lectus metus pharetra velit, id posuere purus felis vitae tellus."
                            +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac nunc ac ligula ultricies bibendum. Morbi facilisis, justo ac finibus placerat, mauris nisl interdum arcu, non rhoncus lacus leo nec mi. Etiam scelerisque tellus non erat cursus dapibus. Duis tincidunt ullamcorper magna, sit amet auctor ipsum. Curabitur id lacinia nibh. Vestibulum gravida, nulla id faucibus malesuada, lectus metus pharetra velit, id posuere purus felis vitae tellus."
                            +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac nunc ac ligula ultricies bibendum. Morbi facilisis, justo ac finibus placerat, mauris nisl interdum arcu, non rhoncus lacus leo nec mi. Etiam scelerisque tellus non erat cursus dapibus. Duis tincidunt ullamcorper magna, sit amet auctor ipsum. Curabitur id lacinia nibh. Vestibulum gravida, nulla id faucibus malesuada, lectus metus pharetra velit, id posuere purus felis vitae tellus."
                            +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac nunc ac ligula ultricies bibendum. Morbi facilisis, justo ac finibus placerat, mauris nisl interdum arcu, non rhoncus lacus leo nec mi. Etiam scelerisque tellus non erat cursus dapibus. Duis tincidunt ullamcorper magna, sit amet auctor ipsum. Curabitur id lacinia nibh. Vestibulum gravida, nulla id faucibus malesuada, lectus metus pharetra velit, id posuere purus felis vitae tellus."
                            +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac nunc ac ligula ultricies bibendum. Morbi facilisis, justo ac finibus placerat, mauris nisl interdum arcu, non rhoncus lacus leo nec mi. Etiam scelerisque tellus non erat cursus dapibus. Duis tincidunt ullamcorper magna, sit amet auctor ipsum. Curabitur id lacinia nibh. Vestibulum gravida, nulla id faucibus malesuada, lectus metus pharetra velit, id posuere purus felis vitae tellus."
                            +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac nunc ac ligula ultricies bibendum. Morbi facilisis, justo ac finibus placerat, mauris nisl interdum arcu, non rhoncus lacus leo nec mi. Etiam scelerisque tellus non erat cursus dapibus. Duis tincidunt ullamcorper magna, sit amet auctor ipsum. Curabitur id lacinia nibh. Vestibulum gravida, nulla id faucibus malesuada, lectus metus pharetra velit, id posuere purus felis vitae tellus."
                            +
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac nunc ac ligula ultricies bibendum. Morbi facilisis, justo ac finibus placerat, mauris nisl interdum arcu, non rhoncus lacus leo nec mi. Etiam scelerisque tellus non erat cursus dapibus. Duis tincidunt ullamcorper magna, sit amet auctor ipsum. Curabitur id lacinia nibh. Vestibulum gravida, nulla id faucibus malesuada, lectus metus pharetra velit, id posuere purus felis vitae tellus.");

            em.persist(recruitmentArticle1);
            em.persist(recruitmentArticle2);
            em.persist(recruitmentArticle3);
            em.persist(recruitmentArticle4);

            for (int i = 5; i < 20; i++) {
                em.persist(createRecruitmentArticleForm(
                        createRecruitmentArticle(LocalDateTime.now(),
                                LocalDateTime.now().plusDays(1), 100L, member4, 2, String.format("테스트 데이터[%03d]", i),
                                "테스트용 게시글 내용입니다."),
                        1, 1L,
                        "삼성산", "석수동", "41171102", 30L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "카카오톡1")
                );
            }

            for (int i = 20; i < 70; i++) {
                em.persist(createRecruitmentArticleForm(
                        createRecruitmentArticle(LocalDateTime.now(),
                                LocalDateTime.now().plusDays(1), 30L, member4, 1, String.format("테스트 데이터[%03d]", i),
                                "테스트용 게시글 내용입니다."),
                        2, 1L,
                        "삼성산", "석수동", "41171102", 20L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "카카오톡1")
                );
            }

            for (int i = 70; i < 100; i++) {
                em.persist(createRecruitmentArticleForm(
                        createRecruitmentArticle(LocalDateTime.now(),
                                LocalDateTime.now().plusDays(1), 20L, member1, 2, String.format("테스트 데이터[%03d]", i),
                                "테스트용 게시글 내용입니다."),
                        1, 1L,
                        "삼성산", "석수동", "41171102", 40L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "카카오톡1")
                );
            }

            Comment comment1 = createComment(recruitmentArticle1, member1, "안녕하세요11", LocalDateTime.now(),
                    LocalDateTime.now());
            Comment comment2 = createComment(recruitmentArticle2, member2, "안녕하세요22", LocalDateTime.now(),
                    LocalDateTime.now());
            Comment comment3 = createComment(recruitmentArticle3, member3, "안녕하세요33", LocalDateTime.now(),
                    LocalDateTime.now());
            Comment comment4 = createComment(recruitmentArticle4, member4, "안녕하세요44", LocalDateTime.now(),
                    LocalDateTime.now());
            em.persist(comment1);
            em.persist(comment2);
            em.persist(comment3);
            em.persist(comment4);

            RecruitmentArticleForm recruitmentArticleForm1 = createRecruitmentArticleForm(recruitmentArticle1, 1, 1L,
                    "삼성산", "석수동", "41171102", 30L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "카카오톡1");
            RecruitmentArticleForm recruitmentArticleForm2 = createRecruitmentArticleForm(recruitmentArticle2, 2, 2L,
                    "지리산", "사량면", "48220370", 40L, LocalDateTime.now(), LocalDateTime.now().plusDays(2), "카카오톡2");
            RecruitmentArticleForm recruitmentArticleForm3 = createRecruitmentArticleForm(recruitmentArticle3, 1, 2L,
                    "한라산", "석수동", "48220370", 40L, LocalDateTime.now(), LocalDateTime.now().plusDays(2), "카카오톡3");
            RecruitmentArticleForm recruitmentArticleForm4 = createRecruitmentArticleForm(recruitmentArticle4, 2, 4L,
                    "백두산", "석수동", "48220370", 20L, LocalDateTime.now(), LocalDateTime.now().plusDays(4), "카카오톡4");
            em.persist(recruitmentArticleForm1);
            em.persist(recruitmentArticleForm2);
            em.persist(recruitmentArticleForm3);
            em.persist(recruitmentArticleForm4);
        }


        private static RecruitmentArticle createRecruitmentArticle(LocalDateTime createDate, LocalDateTime deadLineDate,
                                                                   Long views, Member member, int typeValue,
                                                                   String articleName,
                                                                   String content) {
            RecruitmentArticle recruitmentArticle = new RecruitmentArticle();
            recruitmentArticle.setArticleName(articleName);
            recruitmentArticle.setCreateDate(createDate);
            recruitmentArticle.setContent(content);
            recruitmentArticle.setMember(member);
            recruitmentArticle.setDeadLineDate(deadLineDate);
            recruitmentArticle.setTypeValue(typeValue);
            recruitmentArticle.setViews(views);
            return recruitmentArticle;
        }

        private static Member createMember(int age, String gender, String username, String password, String email,
                                           String nickname, LocalDateTime createDate, LocalDateTime modifyDate) {
            Member member = new Member();
            member.setAge(age);
            member.setCreateDate(createDate);
            member.setGender(gender);
            member.setPassword(password);
            member.setNickname(nickname);
            member.setModifyDate(modifyDate);
            member.setEmail(email);
            member.setUsername(username);
            return member;
        }

        private static Comment createComment(RecruitmentArticle recruitmentArticle, Member member, String content,
                                             LocalDateTime createDate, LocalDateTime modifyDate) {
            Comment comment = new Comment();
            comment.setModifyDate(modifyDate);
            comment.setCreateDate(createDate);
            comment.setRecruitmentArticle(recruitmentArticle);
            comment.setMember(member);
            comment.setContent(content);
            return comment;
        }

        private static RecruitmentArticleForm createRecruitmentArticleForm(RecruitmentArticle recruitmentArticle,
                                                                           int dayNight, Long recruitsNumbers,
                                                                           String mountainName, String mtAddress,
                                                                           String localCode, Long ageRange,
                                                                           LocalDateTime startTime,
                                                                           LocalDateTime courseTime,
                                                                           String connectType) {
            RecruitmentArticleForm recruitmentArticleForm = new RecruitmentArticleForm();
            recruitmentArticleForm.setRecruitmentArticle(recruitmentArticle);
            recruitmentArticleForm.setDayNight(dayNight);
            recruitmentArticleForm.setRecruitsNumbers(recruitsNumbers);
            recruitmentArticleForm.setMountainName(mountainName);
            recruitmentArticleForm.setMtAddress(mtAddress);
            recruitmentArticleForm.setLocalCode(localCode);
            recruitmentArticleForm.setAgeRange(ageRange);
            recruitmentArticleForm.setStartTime(startTime);
            recruitmentArticleForm.setCourseTime(courseTime);
            recruitmentArticleForm.setConnectType(connectType);
            return recruitmentArticleForm;
        }
    }
}