package com.ll.olol.boundedContext.review.entity;

import com.ll.olol.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@ToString
public class Review {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    LocalDateTime createDate;


    @ManyToOne
    Member toMember;

    @ManyToOne
    Member fromMember;

    private int reviewTypeCode; // 매력포인트(1 = 최고에요, 2 = 좋아요, 3 = 별로에요)

    
}
