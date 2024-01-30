package com.project.bank.security;

import com.project.bank.entity.Member;
import com.project.bank.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
//@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final MemberRepository memberRepository;




    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(memberId).orElseThrow();
        log.info("-0--"+ member);

        if(member == null){  //회원 정보가 없다
            throw new UsernameNotFoundException(memberId);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();


        authorities.add(new SimpleGrantedAuthority(member.getRole().toString()));

        //db로 회원 정보
        return new CustomUserMember(member,authorities);



    }








}
