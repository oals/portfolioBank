package com.project.bank.service;


import com.project.bank.dto.MemberDTO;

public interface MemberService {




    public void register(MemberDTO memberDTO);

    public boolean MemberIdCheck(String memberId);

    public MemberDTO SelectMemberInfo(String memberId);



}
