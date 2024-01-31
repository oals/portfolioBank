# PortfolioBank
은행 웹 사이트 개인 프로젝트 포트폴리오

# 소개
 개인의 계좌를 사이트에 등록 시켜 사용자 간의 이체와 적금 가입이 가능한 사이트 입니다.  


https://github.com/oals/PortfolioBankTest 

2023년 9월에 일주일간 만들고 방치 해뒀던 은행 프로젝트를 업데이트 했다

# 제작기간 & 참여 인원
<UL>
  <LI>2023.09.01 ~ 2023.09.07</LI>
  <LI>2024.01.16 ~ 2024.01.30</LI>
  <LI>개인 프로젝트</LI>
</UL>


# 사용기술
![js](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white)
![js](https://img.shields.io/badge/Java-FF0000?style=for-the-badge&logo=Java&logoColor=white)
![js](https://img.shields.io/badge/IntelliJ-004088?style=for-the-badge&logo=IntelliJ&logoColor=white)
![js](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white)
![js](https://img.shields.io/badge/security-6DB33F?style=for-the-badge&logo=security&logoColor=white)

![js](https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jquery&logoColor=white)
![js](https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)
![js](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white)


# E-R 다이어그램



# Entity




<details>
 <summary> Member Entity 
 
 </summary> 
 
    
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
 



</details>


<details>
 <summary> Account Entity
 
 </summary> 



 
    @Getter
    @Table(name="account")
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    public class Account {

    @Id
    @Column(name="account_number")
    private String accountNumber;       // 계좌 번호


    @Column(name="account_pswd")
    private String accountPswd;         //계좌 비밀번호

    @Column(name="create_date")
    private String createDate;          // 생성 일자

    @Column(name="balance")
    private int balance;                //계좌 잔액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;              // 회원 아이디


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountProductNo")
    private AccountProduct accountProduct;


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<History> histories;


    public void addHistory(History history){
        this.histories.add(history);
    }


    public void MinusBalance(int balance){
        this.balance -= balance;

    }

    public void PlusBalance(int balance){
        this.balance += balance;

    }



     }







</details>



<details>
 <summary> AccountProduct Entity
 
 </summary> 
 



    @Getter
    @Table(name="account_product")
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    public class AccountProduct {

    @Id
    private String accountProductName;
     private String productIntroduction; // 상품 소개
     private String eligibleCustomers; // 가입대상
     private String interestRate; // 이율
     private String interestPayment; // 이자지급
     private String additionalServices; // 부가서비스
     private String features; // 특징
     private String depositorProtection; // 예금자 보호여부


     }





</details>



<details>
 <summary> History Entity


 
 </summary> 

 


    @Getter
    @Table(name="history")
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    public class History {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long Id;
    private int balance;               // 계좌 잔액

    private int money;               //  이체 금액
    private String updateDate;   // 입출금 일자

    private String opt;              // 입/출금/이자 여부

    private String counterpartyAccountNumber; // 조회할 계좌
    private String counterpartyName;          //상대방 입출금자
    private String counterpartyAccountName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    }





</details>




<details>
 <summary> Savings Entity
 
 </summary> 




    @Entity
    @Getter
    @Builder
    @Table(name="savings")
    @AllArgsConstructor
    @NoArgsConstructor
    public class Savings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SavingsNo;

    private boolean isActive;

    private int savingsBalance; //현재까지 적금된 금액

    private int returnAmount; // 적금 반환액

    private int missedPayments; // 미납 횟수

    private int earnedInterest; //반환액

    private String savingsStartDate; // 적금 가입 날짜

    private String savingsEndDate; //적금이 끝나는 날짜


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "savingsProductId")
    private SavingsProduct savingsProduct;   //적금 상품


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountNumber")
    private Account account;   //적금 게좌


    @OneToMany(mappedBy = "savings", cascade = CascadeType.ALL)
    private List<SavingsHistory> histories;


    public void addHistory(SavingsHistory savingsHistory){
        this.histories.add(savingsHistory);
    }

    public void EndSavings(int returnAmount, int earnedInterest ){
        this.isActive = false;
        this.returnAmount = returnAmount;
        this.earnedInterest = earnedInterest;
    }

    public void UpdateSavingsBalance(int savingsBalance){
        this.savingsBalance += savingsBalance;

    }

    public void UpdateMissedPayments(){
        this.missedPayments = this.missedPayments + 1;
    }





    }

 
 

</details>


<details>
 <summary> SavingsHistory Entity
 
 </summary> 




    @Getter
    @Setter
    @Table(name="Savings_history")
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    public class SavingsHistory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long savingsId;

    private Boolean savingsPaymentStatus; // 이번 달 적금 처리 상태

    private String savingsStartDate;   // 적금 한 날짜

    private int savingsCount; // 적금 만기까지의 카운트


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SavingsNo")
    private Savings savings;


    }
    


 
 

</details>



<details>
 <summary> SavingsProduct Entity
 
 </summary> 





    @Entity
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class SavingsProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savingsProductId;
    private String productName; // 적금 상품의 이름
    private double interestRate; // 적금 상품의 이자율
    private int maturityPeriod; // 적금 계약의 만기 기간
    private String productIntro; // 적금 상품의 소개
    private String eligibilityCriteria; // 적금 상품의 가입 조건
    private double governmentSupport; // 정부 지원금의 정보
    private int monthlyDepositAmount; // 사용자가 매월 적금에 납입하는 금액
    private int maturityAmount; // 적금 계약이 만료될 때 받게 되는 총 금액

    private String productRating; // 상품평점
    private String consultationRequest; // 상담신청
    private String detailedDepositRate; // 예금금리를 상세히 알아보세요
    private String monthlyDepositAmountBased; // 월납입금액기준 월납입액 원 목표기간 개월 예금금리 %
    private String maturityPaymentAmountBased; // 만기지급액기준 가입금액

    private String subscriptionPeriod; // 가입기간
    private double subscriptionAmount; // 가입금액
    private String depositBusiness; // 납입방법
    private String subscriptionMethod; // 가입방법
    private String requiredDocuments; // 필요서류
    private String taxBenefits; // 세제혜택
    private double principalOrInterest; // 원금 또는 이자
    private String paymentRestrictions; // 지급제한
    private double appliedInterestRate; // 적용금리
    private String interestPaymentMethod; // 이자지급방식





    }





 
</details>



<details>
 <summary> Report Entity
 
 </summary> 





    @Entity
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportNum;
    private String siteName; // 사이트 명
    private String scamProductType; // 사기 물품 종류
    private String scamProductName; // 사기 물품명
    private String suspectId; // 용의자 아이디
    private String scamPostUrl; // 사기 게시글 url
    private String suspectBankName; // 용의자 은행명
    private String suspectFullName; // 용의자 성명
    private String suspectAccountNumber; // 용의자 계좌번호
    private String depositAmount; // 입금금액
    private String depositDate; // 입금일
    private String suspectContact; // 용의자 연락처
    private String suspectGender; // 용의자 성별
    private String suspectCharacteristics; // 용의자 특징
    private String statementContent; // 진술서 내용
    private String evidencePhotos; // 증거 사진
    private String reporterDescription; // 신고자 설명
    private String reporterContact; // 신고자 연락처
    private String reporterEmail; // 신고자 이메일

    private boolean reportStatus;    //검토 여부

    private boolean agreementToTheTerms;

    private boolean agreementToTheTerms2;

    private String reportDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;              // 회원 아이디



    }



 
</details>




<hr>



# 핵심 기능 및 페이지 소개





![회원가입](https://github.com/oals/portfolioBank/assets/136543676/b8d35d05-2baa-423a-8730-c2cdbe6ae574)


![계좌생성](https://github.com/oals/portfolioBank/assets/136543676/66c58a1d-b2a6-4d1d-be5b-60f13b824fd1)


![계좌이체](https://github.com/oals/portfolioBank/assets/136543676/1590b910-7e75-41e7-9765-cee1d4ce3f23)

![적금가입](https://github.com/oals/portfolioBank/assets/136543676/9b87152d-4834-4f22-9e7e-77906806dc6c)


![적금-완료-및-반환-내역](https://github.com/oals/portfolioBank/assets/136543676/4e2a7beb-4545-46da-90ef-acc2b56247f7)


![사기조회](https://github.com/oals/portfolioBank/assets/136543676/2d8ea14f-0b5e-4017-aa79-3bdf9e2be443)



![사기신고](https://github.com/oals/portfolioBank/assets/136543676/65482db2-4d34-4209-aa6b-76eb15e42074)




![사기신고승인](https://github.com/oals/portfolioBank/assets/136543676/21281e69-65d6-4930-b3a5-27d8829a498b)






