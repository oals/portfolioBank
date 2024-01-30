package com.project.bank.service;

import com.project.bank.dto.AccountDTO;
import com.project.bank.dto.MemberDTO;
import com.project.bank.entity.Account;
import com.project.bank.entity.Member;
import com.project.bank.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{


    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void register(MemberDTO memberDTO) {

        Member member = Member.createMember(memberDTO,passwordEncoder);

        memberRepository.save(member);

    }

    @Override
    public boolean MemberIdCheck(String memberId) {


       boolean chk = memberRepository.findById(memberId).isPresent();
        

        return chk;
    }

    @Override
    public MemberDTO SelectMemberInfo(String memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow();

        MemberDTO memberDTO = modelMapper.map(member,MemberDTO.class);

        return memberDTO;
    }


}
