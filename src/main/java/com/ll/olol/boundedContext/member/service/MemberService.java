package com.ll.olol.boundedContext.member.service;

import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
import com.ll.olol.boundedContext.notification.service.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly 라는 것을 명시, 나중을 위해
public class MemberService {
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    @Transactional
    public RsData<Member> join(String username, String password) {
        return join("GRAMGRAM", username, password);
    }

    private RsData<Member> join(String providerTypeCode, String username, String password) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
        }

        if (StringUtils.hasText(password)) {
            password = passwordEncoder.encode(password);
        }

        Member member = Member
                .builder()
                .providerTypeCode(providerTypeCode)
                .username(username)
                .password(password)
                .reviewScore(482)
                .build();

        memberRepository.save(member);

        return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
    }


    @Transactional
    public RsData<Member> whenSocialLogin(String providerTypeCode, String username) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) {
            return RsData.of("S-1", "로그인 되었습니다.", opMember.get());
        }

        return join(providerTypeCode, username, "");
    }

    @Transactional
    public RsData modifyMemberInfo(Member member, String nickname, int ageRange, String gender, String email) {
        Member resultMember = memberRepository.findById(member.getId()).get();
        resultMember.setNickname(nickname);
        resultMember.setAge(ageRange);
        resultMember.setGender(gender);
        resultMember.setEmail(email);

        memberRepository.save(resultMember);

        return RsData.of("S-1", "닉네임 수정 성공");
    }

    public RsData additionalInfo(Member loginedMember) {
        Optional<Member> member = memberRepository.findById(loginedMember.getId());
        if (member.get().getEmail() == null || member.get().getNickname() == null) {
            return RsData.of("F-1", "추가 정보를 입력해주세요");
        }
        return RsData.of("S-1", "success!");
    }
}