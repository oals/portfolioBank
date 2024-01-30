package com.project.bank.security;

import com.project.bank.entity.Member;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
@Log4j2
public class CustomUserMember extends User {
    private final String memberId;
    private final String pswd;
    private final String username;

    // member.getEmail()이 User객체에서 Id역할: User의 Id는 유일성을 보장하는 필드이여함.
    public CustomUserMember(Member member, List<GrantedAuthority> authorities) {
        super(member.getMemberId(), member.getPswd(), authorities);
        log.info("접근 체크");
        //User객체가 가지는 username과 password 이외에 id,getAddress,password 추가.
        this.memberId = member.getMemberName();
        this.pswd = member.getPswd();
        this.username = member.getMemberId();

    }
}
