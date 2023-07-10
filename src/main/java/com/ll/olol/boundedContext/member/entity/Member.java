package com.ll.olol.boundedContext.member.entity;

import com.ll.olol.boundedContext.chat.entity.ChatRoom;
import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
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

    @OneToMany(mappedBy = "member")
    private List<RecruitmentPeople> recruitmentPeople;

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
