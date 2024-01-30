package com.project.bank.dto;

import com.project.bank.constant.Level;
import lombok.*;

import com.project.bank.config.Role;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDTO {

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
    private Role role;       // 권한


}
