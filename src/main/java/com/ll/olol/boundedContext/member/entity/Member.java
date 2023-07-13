package com.ll.olol.boundedContext.member.entity;

import com.ll.olol.boundedContext.chat.entity.ChatRoom;
import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.report.entity.ArticleReport;
import com.ll.olol.boundedContext.review.entity.Review;
import com.ll.olol.boundedContext.review.entity.ReviewMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)

public class Member {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "member")
    private List<Comment> comments;

    @OneToMany(mappedBy = "reviewMember")
    private List<ReviewMember> reviewMembers;

    @OneToMany(mappedBy = "fromMember", cascade = CascadeType.REMOVE)
    @OrderBy("id desc") // 정렬
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Review> fromReviewList;

    @OneToMany(mappedBy = "toMember", cascade = CascadeType.REMOVE)
    @OrderBy("id desc") // 정렬
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Review> toReviewList;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;

    // 일반회원인지, 카카오로 가입한 회원인지, 구글로 가입한 회원인지
    private String providerTypeCode;


    @Column(unique = true)
    private String username;

    private String password;


    @Column(unique = true)
    private String email;

    private int age;

    @Column(unique = true)
    private String nickname;

    private String gender;

    private String imageLink;

    private String fcmToken;

    @OneToMany(mappedBy = "member")
    private List<RecruitmentPeople> recruitmentPeople;

    @OneToMany(mappedBy = "member")
    private List<RecruitmentArticle> recruitmentArticle;

    @OneToMany(mappedBy = "fromMember")
    private List<ArticleReport> articleReport;

    private int reviewScore;
    @Setter
    private boolean receivePush = true;
    @Setter
    private boolean receiveMail = true;

    //    @OneToMany(mappedBy = "fromMember", cascade = {CascadeType.ALL})
//    @OrderBy("id desc") // 정렬
//    @LazyCollection(LazyCollectionOption.EXTRA)
//    @Builder.Default // @Builder 가 있으면 ` = new ArrayList<>();` 가 작동하지 않는다. 그래서 이걸 붙여야 한다.
//    private List<LikeableRecruitmentArticle> fromLikeableArticle = new ArrayList<>();
    @ManyToMany(mappedBy = "chatMembers")
    private List<ChatRoom> chatRooms = new ArrayList<>();


    // 이 함수 자체는 만들어야 한다. 스프링 시큐리티 규격
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 모든 멤버는 member 권한을 가진다.
        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        // username이 admin인 회원은 추가로 admin 권한도 가진다.
        if (isAdmin()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }

        return grantedAuthorities;
    }

    public boolean isAdmin() {
        return "admin".equals(username);
    }
}
