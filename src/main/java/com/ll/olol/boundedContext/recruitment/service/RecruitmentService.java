package com.ll.olol.boundedContext.recruitment.service;

import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.api.localCode.LocalCodeApiClient;
import com.ll.olol.boundedContext.chat.service.ChatService;
import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.notification.event.EventAfterCourseTime;
import com.ll.olol.boundedContext.notification.event.EventAfterUpdateArticle;
import com.ll.olol.boundedContext.recruitment.entity.CreateForm;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticleForm;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentFormRepository;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly 라는 것을 명시, 나중을 위해
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;

    private final RecruitmentFormRepository recruitmentFormRepository;

    private final ChatService chatService;

    private final LocalCodeApiClient localCodeApiClient;

    private final ApplicationEventPublisher publisher;

    public Optional<RecruitmentArticle> findById(Long id) {
        return recruitmentRepository.findById(id);
    }

    public List<RecruitmentArticle> findAll() {
        return recruitmentRepository.findAll();
    }

    @Transactional
    public RecruitmentArticle createArticle(String articleName, String content, Member member, Integer typeValue, LocalDateTime deadLineDate) {

        chatService.createChatRoomDetailDTO(member, articleName);

        RecruitmentArticle recruitmentArticle = RecruitmentArticle
                .builder()
                .member(member)
                .articleName(articleName)
                .content(content)
                .views(0L)
                .typeValue(typeValue)
                .deadLineDate(deadLineDate)
                .build();


        recruitmentRepository.save(recruitmentArticle);
        return recruitmentArticle;
    }

    @Transactional
    public void createArticleForm(RecruitmentArticle recruitmentArticle, Integer dayNight, Long recruitsNumber,
                                  String mountainName, String mtAddress, Long ageRange, LocalDateTime startTime, Long durationOfTime) {
        // 동만 붙은 부분만 가져옴
        RsData<String> checkMt = mtAddressChecked(mtAddress);

        String realMountainAddress = null;

        if (checkMt.isSuccess()) {
            realMountainAddress = checkMt.getData();
        }

        if (durationOfTime == null) {
            durationOfTime = 5L;
        }
        RecruitmentArticleForm recruitmentArticleForm;
        if (startTime == null) {
            recruitmentArticleForm = RecruitmentArticleForm
                    .builder()
                    .recruitmentArticle(recruitmentArticle)
                    .dayNight(dayNight)
                    .recruitsNumbers(recruitsNumber)
                    .mountainName(mountainName)
                    .mtAddress(realMountainAddress)
                    .ageRange(ageRange)
                    .startTime(null)
                    .courseTime(null)
                    .localCode(localCodeApiClient.requestLocalCode(realMountainAddress))
                    .build();
        } else {
            recruitmentArticleForm = RecruitmentArticleForm
                    .builder()
                    .recruitmentArticle(recruitmentArticle)
                    .dayNight(dayNight)
                    .recruitsNumbers(recruitsNumber)
                    .mountainName(mountainName)
                    .mtAddress(realMountainAddress)
                    .ageRange(ageRange)
                    .startTime(startTime)
                    .courseTime(startTime.plusHours(durationOfTime))
                    .localCode(localCodeApiClient.requestLocalCode(realMountainAddress))
                    .build();
        }
        recruitmentFormRepository.save(recruitmentArticleForm);
    }


    private RsData<String> mtAddressChecked(String mountainName) {
        String[] mtPullAddress = mountainName.split(" ");

        String realMtAddress = null;

        for (String checkAdd : mtPullAddress) {
            if (checkAdd.endsWith("동")) {
                realMtAddress = checkAdd;
            } else if (checkAdd.endsWith("읍")) {
                realMtAddress = checkAdd;
            } else if (checkAdd.endsWith("면")) {
                realMtAddress = checkAdd;
            } else if (checkAdd.endsWith("가")) {
                realMtAddress = checkAdd;
            }
        }

        if (realMtAddress != null) {
            return RsData.of("S-1", "주소를 저장했습니다.", realMtAddress);
        }

        return RsData.of("F-1", "동을 저장 못함");
    }

    private Specification<RecruitmentArticle> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<RecruitmentArticle> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<RecruitmentArticle, Member> u1 = q.join("member", JoinType.LEFT);
                Join<RecruitmentArticle, Comment> a = q.join("comment", JoinType.LEFT);
                Join<Comment, Member> u2 = a.join("member", JoinType.LEFT);
                return cb.or(cb.like(q.get("articleName"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(q.get("recruitmentArticleForm").get("mountainName"), "%" + kw + "%"),      // 산
                        cb.like(u1.get("nickname"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("nickname"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public Page<RecruitmentArticle> getlist(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        if (kw == null || kw.trim().length() == 0) {
            return recruitmentRepository.findAll(pageable);
        }
        Specification<RecruitmentArticle> spec = search(kw);
        return recruitmentRepository.findAll(spec, pageable);
    }

    public Page<RecruitmentArticle> getListByConditions(Long ageRange, int dayNight, int typeValue, String kw,
                                                        Pageable pageable) {
        Specification<RecruitmentArticle> spec = Specification.where(null);

        if (ageRange != 0L) {
            spec = spec.and(
                    (root, query, cb) -> cb.equal(root.get("recruitmentArticleForm").get("ageRange"), ageRange));
        }

        if (dayNight != 0) {
            spec = spec.and(
                    (root, query, cb) -> cb.equal(root.get("recruitmentArticleForm").get("dayNight"), dayNight));
        }

        if (typeValue != 0) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("typeValue"), typeValue));
        }

        if (kw != null && !kw.trim().isEmpty()) {
            spec = spec.and(search(kw));
        }

        return recruitmentRepository.findAll(spec, pageable);
    }

    @Transactional
    public void updateArticleForm(RecruitmentArticle recruitmentArticle) {
        recruitmentRepository.save(recruitmentArticle); // 이거 save안해줘도 되는것이 아닌가?
    }

    @Transactional
    public RsData update(RecruitmentArticle recruitmentArticle, CreateForm createForm) {

        // 동만 붙은 부분만 가져옴
        RsData<String> checkMt = mtAddressChecked(createForm.getMtAddress());

        String realMountainAddress = null;

        if (checkMt.isSuccess()) {
            realMountainAddress = checkMt.getData();
        }

        recruitmentArticle.update(createForm);
        recruitmentArticle.getRecruitmentArticleForm().update(createForm, realMountainAddress);

        publisher.publishEvent(new EventAfterUpdateArticle(this, recruitmentArticle));
        return RsData.of("S-1", "수정 완료");
    }

    public RsData canUpdate(Optional<RecruitmentArticle> recruitmentArticle, Member member) {
        if (recruitmentArticle.isEmpty()) {
            return RsData.of("F-1", "존재하지 않는 모임 공고입니다");
        }
        if (recruitmentArticle.get().getMember().getId() != member.getId()) {
            return RsData.of("F-2", "모집자만이 수정 가능합니다");
        }
        if (recruitmentArticle.get().getDeadLineDate().isBefore(LocalDateTime.now())) {
            return RsData.of("F-3", " 마감 후에는 수정이 불가능합니다");
        }

        return RsData.of("S-1", "모임 글 수정 가능합니다.");
    }

    @Transactional
    public RsData deleteArticle(RecruitmentArticle recruitmentArticle) {
        recruitmentRepository.delete(recruitmentArticle);
        return RsData.of("S-1", "삭제 완료");
    }

    public RsData canDelete(Optional<RecruitmentArticle> recruitmentArticle, Member member) {
        if (recruitmentArticle.isEmpty()) {
            return RsData.of("F-1", "존재하지 않는 모임 공고입니다");
        }

        if (recruitmentArticle.get().getMember().getId() != member.getId() && !member.isAdmin()) {
            return RsData.of("F-2", "모집자만이 삭제 가능합니다");
        }

        return RsData.of("S-1", "모임 삭제 가능합니다.");
    }

    @Transactional
    public void addView(RecruitmentArticle recruitmentArticle) {
        recruitmentArticle.setViews(recruitmentArticle.getViews() + 1);
        recruitmentRepository.save(recruitmentArticle);
    }

    public List<RecruitmentPeople> findByRecruitmentPeopleId(Long id) {
        Optional<RecruitmentArticle> article = findById(id);
        return article.get().getRecruitmentPeople();
    }

    @Transactional
    public RsData deadLine(Member member, RecruitmentArticle article) {
        if (member.getId() != article.getMember().getId()) {
            return RsData.of("F-1", "만든 사람만 마감버튼을 누를 수 있어요.");
        }
        article.setDeadLineDate(LocalDateTime.now());
        return RsData.of("S-1", "마감 성공");
    }

    //@Scheduled(fixedRate = 60000)
    @Transactional
    public void checkTimeDeadLine() {
        List<RecruitmentArticle> all = recruitmentRepository.findByDeadLineDateBeforeAndIsDeadLine(LocalDateTime.now(),
                false);
        for (RecruitmentArticle article : all) {
            if (LocalDateTime.now().isAfter(article.getDeadLineDate())) {
                article.setDeadLine(true);
            }
        }
    }

    @Transactional
    @Scheduled(fixedDelay = 30 * 1000) // 30분 59초마다 실행 (단위: 밀리초)
    public void triggerEvent() {
        //List<RecruitmentArticle> recruitmentArticleList = recruitmentService.findAll();

        LocalDateTime currentTime = LocalDateTime.now();
        List<RecruitmentArticle> recruitmentArticleList = recruitmentRepository.findByRecruitmentArticleForm_CourseTimeBeforeAndIsEventTriggered(
                currentTime, false);

        for (RecruitmentArticle article : recruitmentArticleList) {
            // 30초
            if (article.getRecruitmentArticleForm().getCourseTime().plusSeconds(30L).isBefore(currentTime)) {
                article.setEventTriggered(true);
                sendNotificationAuthor(article);
            }
            // 2시간
//            if (article.getRecruitmentArticleForm().getCourseTime().plusHours(2).isBefore(currentTime)) {
//                article.setEventTriggered(true);
//                sendNotificationAuthor(article);
//            }
        }
    }


    @Transactional
    public void sendNotificationAuthor(RecruitmentArticle recruitmentArticle) {
        System.out.println(recruitmentArticle.isEventTriggered());
        publisher.publishEvent(new EventAfterCourseTime(this, recruitmentArticle));
    }

    public List<RecruitmentArticle> findByRecruitmentArticleForm_CourseTimeBeforeAndIsEventTriggered(
            LocalDateTime currentTime, boolean isEventTriggered) {
        return recruitmentRepository.findByRecruitmentArticleForm_CourseTimeBeforeAndIsEventTriggered(currentTime,
                isEventTriggered);
    }

    public List<RecruitmentArticle> findByMemberOrderByIdDesc(Member loginedMember) {
        return recruitmentRepository.findByMemberOrderByIdDesc(loginedMember);
    }
}
