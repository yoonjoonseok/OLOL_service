package com.ll.olol.base.security;


//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = false)
//public class CustomUserDetailsService implements UserDetailsService {
//    private final MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username(%s) not found".formatted(username)));
//
//        return new User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
//    }
//}

