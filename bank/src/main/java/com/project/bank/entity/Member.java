package com.project.bank.entity;


import com.project.bank.config.Role;
import com.project.bank.dto.MemberDTO;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name="member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @Column(name="member_id")
    private String memberId; // 아이디
    private String memberName; //유저이름
    private String pswd;     // 비밀번호
    private String email;     // 이메일
    private String phone;       // 전화번호
    private String age;         // 생년월일
    private String address;     // 주소
    private String gender;      // 성별
    private LocalDateTime regDate;  // 등록일자

    @Enumerated(EnumType.STRING)
    private Role role;       // 레벨



    public static Member createMember(MemberDTO memberDTO, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setMemberId(memberDTO.getMemberId());
        member.setMemberName(memberDTO.getMemberName());
        member.setEmail(memberDTO.getEmail());
        member.setPhone(memberDTO.getPhone());
        member.setAge(memberDTO.getAge());
        member.setAddress(memberDTO.getAddress());
        member.setGender(memberDTO.getGender());
        member.setRegDate(LocalDateTime.now());
        member.setRole(Role.USER); //일반 유저 디폴트값 5등급

        // 암호화
        String password = passwordEncoder.encode(memberDTO.getPswd());
        member.setPswd(password);

        return member;
    }



}
