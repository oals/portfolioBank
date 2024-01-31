# PortfolioBank
은행 웹 사이트 개인 프로젝트 포트폴리오

# 소개
 개인의 계좌를 생성해 사용자 간의 이체와 적금 가입 및 사기 계좌 조회, 사기 계좌 신고를 할 수 있는 사이트 입니다.  

https://github.com/oals/PortfolioBankTest 

2023년 9월에 일주일간 개발 했었던 은행 프로젝트를 업데이트 했습니다.


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

![bank erd text](https://github.com/oals/portfolioBank/assets/136543676/6f98c94a-98d5-4190-b668-04e3d7bd2328)


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
    @JoinColumn(name = "accountProductName")
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
    @JoinColumn(name = "account_number")
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
    private Account account;   //적금 계좌


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

<br>


<h3>계좌 생성</h3>
<br>


![계좌생성](https://github.com/oals/portfolioBank/assets/136543676/66c58a1d-b2a6-4d1d-be5b-60f13b824fd1)




<br>
<br>

<details>
 <summary> 계좌 생성 플로우 차트
 
 </summary> 



![계좌 생성](https://github.com/oals/portfolioBank/assets/136543676/4037ddac-5ae3-4296-ab99-9389a6e29c6a)




 


</details>


<details>
 <summary> SMS 인증 플로우 차트
 
 </summary> 
 
![sms 인증](https://github.com/oals/portfolioBank/assets/136543676/9cf8f27d-5ec6-4a39-8d14-9533aa508114)


</details>



<details>
 <summary> SMS 인증 이미지
 
 </summary> 

![sms 인증 폰](https://github.com/oals/portfolioBank/assets/136543676/0eabaf09-8d2f-4d63-a230-7e64e08f2b51)



</details>




<details>
 <summary> SMS 전송 Service
 
 </summary> 



    @Override
    public String SendSms(String phoneNumber) {

        String newPhoneNumber = phoneNumber.replaceAll("-","");
        Message coolsms = new Message("api_key", "api_secret");

        Random rand = new Random();
        String randNum = "";

        randNum = rand.nextInt((9999 - 1000) + 1) + 1000 + "";


        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("to", newPhoneNumber);
            params.put("from", "01090465953");
            params.put("type", "sms");
            params.put("text", "bank 회원가입 인증번호는 [" + randNum + "] 입니다.");

            coolsms.send(params); // 메시지 전송

        } catch (CoolsmsException e) {
            throw new RuntimeException(e);
        }


        return randNum;

       }

    


</details>




<details>
 <summary> SMS 인증 Script
 
 </summary> 



    $('#sendSMS').click(function(){
  
    let phoneNumber = $('#phone').val()

    if(phoneNumber.length == 13){

    $.ajax({
        type:'GET',
        dataType:'text',
        url: '/sendSMS',
        data : {
            phoneNumber : phoneNumber
        },
        success:function(data){
            alert('인증번호가 전송되었습니다.')

            $('#smsChkBtn').click(function(){
                let smsData = $('#smsData').val()
                if(data == smsData){
                    alert('인증 되었습니다.')
                    smsChk = true
                }else{
                    alert('인증 실패')
                    smsChk = false
                }

            })


        },
        error:function(e){
            alert('오류')
        }
    })

    }else{
        alert('번호를 제대로 입력해주세요.')
    }

    })





</details>





<UL>
  <LI>CoolSMS의 SMS 전송 API를 사용해 계좌 생성 시 SMS 인증을 하도록 구현했습니다. </LI>
  <LI>계좌에 상품을 연결시키고 상품 당 하나의 계좌만 생성 가능하도록 구현했습니다.</LI>
  <LI>계좌의 비밀번호를 설정해 계좌 정보 페이지 접근, 이체 , 적금 가입 시 검증을 거치도록 구현했습니다. </LI>

</UL>


<br>
<br>





<h3>계좌 이체</h3>
<br>



![계좌이체](https://github.com/oals/portfolioBank/assets/136543676/1590b910-7e75-41e7-9765-cee1d4ce3f23)



<br>
<br>

<details>
 <summary> 계좌 이체 플로우 차트
 
 </summary> 
 
![계좌 이체](https://github.com/oals/portfolioBank/assets/136543676/3140b460-aada-44d0-a766-5b7a4276f563)


</details>



<details>
 <summary> 입금 계좌 존재 검사 Service
 
 </summary> 



    @Override
    public boolean CheckReceiveAccount(TransferDTO transferDTO) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QAccount qAccount = QAccount.account;

        // 받는 사람의 계좌 정보 검사

        boolean chk = queryFactory.selectFrom(qAccount)
                .where(qAccount.accountProduct.accountProductName.eq(transferDTO.getReceiveAccountName())
                        .and(qAccount.accountNumber.eq(transferDTO.getReceiveAccountNumber()))
                        .and(qAccount.member.memberName.eq(transferDTO.getReceiveMemberName())))
                .fetchOne() == null ? false : true;


        return chk;

      }



</details>


<details>
 <summary> 출금 계좌 잔액 검사 Service
 
 </summary> 
 



    @Override
    public boolean CheckSendAccount(TransferDTO transferDTO) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QAccount qAccount = QAccount.account;

        Account account = accountRepository.findById(transferDTO.getSendAccountNumber()).orElseThrow();

        boolean sendChk = false;

        if(account.getBalance() < transferDTO.getSendBalance()){
            sendChk = false;
        }else{
            sendChk = true;
        }

        return sendChk;
       }


</details>


<details>
 <summary> 입출금 Service
 
 </summary> 
 



    @Override
    public boolean SendMoney(TransferDTO transferDTO) {

        boolean result = true;
       
        Account SendAccount = accountRepository.findById(transferDTO.getSendAccountNumber()).orElseThrow();

        Account ReceiveAccount = accountRepository.findById(transferDTO.getReceiveAccountNumber()).orElseThrow();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        String date = now.format(formatter);


        try{

        // 보내는 사람의 계좌 이체 내역 추가
            History SendHistory = History.builder()
                    .balance(SendAccount.getBalance() - transferDTO.getSendBalance())  //잔액
                    .money(transferDTO.getSendBalance())                                //보낸 금액
                    .opt("송금")
                    .counterpartyName(ReceiveAccount.getMember().getMemberName())       //받는 사람 이름
                    .counterpartyAccountNumber(ReceiveAccount.getAccountNumber())           //받는 사람의 계좌 정보
                    .counterpartyAccountName(ReceiveAccount.getAccountProduct().getAccountProductName())
                    .updateDate(date)                                    //해당 날짜
                    .account(SendAccount)
                    .build();

            SendAccount.addHistory(SendHistory);




        //받는 사람의 계좌 이체 내역 추가
        History ReceiveHistory = History.builder()
                    .balance(ReceiveAccount.getBalance() + transferDTO.getSendBalance()) //잔액
                    .money(transferDTO.getSendBalance())                                 //받은 금액
                    .opt("입금")
                    .counterpartyName(SendAccount.getMember().getMemberName())             //보낸 사람 이름
                    .counterpartyAccountNumber(SendAccount.getAccountNumber())            //보낸 사람 계좌 정보
                    .counterpartyAccountName(SendAccount.getAccountProduct().getAccountProductName())
                    .updateDate(date)
                    .account(ReceiveAccount)
                    .build();

            ReceiveAccount.addHistory(ReceiveHistory);


             //출금 계좌 잔액 감소
            SendAccount.MinusBalance(transferDTO.getSendBalance());
            accountRepository.save(SendAccount);

            //입금 계좌 잔액 증가
            ReceiveAccount.PlusBalance(transferDTO.getSendBalance());
            accountRepository.save(ReceiveAccount);

        }catch (Exception e){
            e.printStackTrace();
            result =false;
        }


        return result;
        }

    

</details>






<UL>
  <LI>계좌 정보 페이지 접근, 계좌 이체 시 계좌 생성 시에 설정해둔 비밀번호를 입력해야 접근 할 수 있도록 구현했습니다.</LI>
  <LI>계좌 정보 페이지에서 가입 중인 적금,종료된 적금,입출금 내역,적금 및 적금 반환 내역을 확인 할 수 있습니다. </LI>
  <LI>계좌 이체 시 상대방의 계좌 상품 및 계좌번호를 입력해 이체 할 수 있도록 구현했습니다.</LI>


</UL>


<br>
<br>




<h3>적금</h3>
<br>



![적금가입](https://github.com/oals/portfolioBank/assets/136543676/9b87152d-4834-4f22-9e7e-77906806dc6c)



<br>
<br>

<details>
 <summary> 적금 플로우 차트
 
 </summary> 
 
![적금가입](https://github.com/oals/portfolioBank/assets/136543676/2b9e631f-a1e5-4fd6-8a59-bdb2708d5561)


</details>


<details>
 <summary> 적금 다중 스레드 플로우 차트
 
 </summary> 
 
![적금 스레드](https://github.com/oals/portfolioBank/assets/136543676/19ce8acc-7092-4cc6-84dc-b0340df80cd8)


</details>


<details>
 <summary> 다중 스레드 Service
 
 </summary> 
 


     public class MyRunnable implements Runnable {
        private final String accountNumber;
        private final String savingsProductName;
        private boolean threadChk;

        public MyRunnable(String accountNumber,String savingsProductName) {
            this.accountNumber = accountNumber;
            this.savingsProductName =  savingsProductName;
            this.threadChk = true;
        }

        @Override
        public void run() {
            while(threadChk) {

                try {//30L * 24 * 60 * 60 * 1000
                    Thread.sleep(60000);

                    threadChk = threadService.savingsThread(accountNumber,savingsProductName);



                }    catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
      }





</details>



<details>
 <summary> savingsThread
 
 </summary> 
 



       @Override
       @Transactional
       public boolean savingsThread(String accountNumber,String savingsProductName) {


        boolean threadChk = true;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QSavings qSavings = QSavings.savings;

        Savings savingsHistoryEntity = queryFactory.selectFrom(qSavings)
                .where(qSavings.account.accountNumber.eq(accountNumber)
                        .and(qSavings.savingsProduct.productName.eq(savingsProductName)
                                .and(qSavings.isActive.eq(true))))
                .limit(1L)
                .fetchOne();

        //만기 체크
        int savingCount = savingsHistoryEntity.getHistories().size() + 1;
        log.info("savingCount : " +  savingCount);


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        String date = now.format(formatter);

        //스레드 종료
        if(savingCount == savingsHistoryEntity.getSavingsProduct().getMaturityPeriod() + 1){

            double principal = savingsHistoryEntity.getSavingsBalance(); // 원금
            double monthlyInterestRate = savingsHistoryEntity.getSavingsProduct().getAppliedInterestRate(); // 월간 이율
            int termInMonths = savingsHistoryEntity.getSavingsProduct().getMaturityPeriod(); // 기간 (개월)

            // 이율 변환
            monthlyInterestRate = monthlyInterestRate / 12; // 연간 이율을 월간 이율로 변환
            int returnSavings = (int)(principal * Math.pow((1 + monthlyInterestRate / 100), termInMonths));


            Account account = savingsHistoryEntity.getAccount();

            History history = History.builder()
                    .balance(account.getBalance() +  returnSavings)  //현재 잔액
                    .money(returnSavings)                      //보낸 금액
                    .opt("적금 반환")
                    .counterpartyName(savingsHistoryEntity.getSavingsProduct().getProductName())
                    .account(savingsHistoryEntity.getAccount())
                    .updateDate(date)                                    //해당 날짜
                    .build();

            account.PlusBalance(returnSavings);

            savingsHistoryEntity.EndSavings(returnSavings,returnSavings - (int)principal);


            accountRepository.save(account);
            historyRepository.save(history);
            savingsRepository.save(savingsHistoryEntity);

            log.info("만기 반환액 : " + returnSavings);

            threadChk = false;


        } else{  //만기 상태가 아닐 시


            if(savingsHistoryEntity.getAccount().getBalance() >= savingsHistoryEntity.getSavingsProduct().getMonthlyDepositAmount()){

                int accountBalance = savingsHistoryEntity.getAccount().getBalance();
                //계좌에서 금액 감소
                savingsHistoryEntity.getAccount().MinusBalance(savingsHistoryEntity.getSavingsProduct().getMonthlyDepositAmount());


                //적금 데이터 저장
                SavingsHistory savingsHistory = SavingsHistory.builder()
                        .savingsStartDate(date)
                        .savingsPaymentStatus(true)
                        .savingsCount(savingCount)
                        .savings(savingsHistoryEntity)
                        .build();

                savingsHistoryEntity.addHistory(savingsHistory);
                savingsHistoryEntity.UpdateSavingsBalance(savingsHistoryEntity.getSavingsProduct().getMonthlyDepositAmount());

                //일반 내역에 저장
                History history = History.builder()
                        .balance(accountBalance -  savingsHistoryEntity.getSavingsProduct().getMonthlyDepositAmount())  //현재 잔액
                        .money(savingsHistoryEntity.getSavingsProduct().getMonthlyDepositAmount())                      //보낸 금액
                        .opt("적금")
                        .counterpartyName(savingsHistoryEntity.getSavingsProduct().getProductName())
                        .account(savingsHistoryEntity.getAccount())
                        .updateDate(date)                                    //해당 날짜
                        .build();

                accountRepository.save(savingsHistoryEntity.getAccount());
                savingsRepository.save(savingsHistoryEntity);
                historyRepository.save(history);


            }else{
                //계좌 잔고가 부족 할때 미납 처리
                SavingsHistory savingsHistory = SavingsHistory.builder()
                        .savingsStartDate(date)
                        .savingsPaymentStatus(false)
                        .savingsCount(savingCount)
                        .savings(savingsHistoryEntity)
                        .build();

                savingsHistoryEntity.addHistory(savingsHistory);
                savingsHistoryEntity.UpdateMissedPayments();

                savingsRepository.save(savingsHistoryEntity);


            }
        }


        return threadChk;
      }






</details>



<UL>
  <LI>가입 할 적금 상품을 선택 할 수 있고 상품 별 이율,적금 기간을 다르게 구현 했습니다. </LI>
  <LI>적금 가입 시 다중 스레드가 등록되도록 구현했습니다.</LI>
  <LI>적금 기간이 끝났을 때 스레드가 종료 되며 이율과 현재 적금액을 계산해 반환 금액을 이체 하도록 구현 했습니다.</LI>
  <LI>적금 가입 시 연결 할 계좌를 선택 할 수 있고 해당 계좌에 첫 적금액이 존재 하는 지 검사하도록 구현했습니다.</LI>


</UL>


<br>
<br>




<h3>적금 반환</h3>
<br>


![적금-완료-및-반환-내역](https://github.com/oals/portfolioBank/assets/136543676/4e2a7beb-4545-46da-90ef-acc2b56247f7)



<br>
<br>


<UL>
  <LI>테스트를 위해 1분 간격으로 스레드가 동작하도록 구현했습니다.</LI>
  <LI>종료된 적금에 가입 했던 적금 내역이 기록 되고 적금 반환액이 적금 내역에 기록 된 걸 확인 할 수 있습니다.</LI>



</UL>


<br>
<br>





<h3>사기 계좌 조회</h3>
<br>



![사기조회](https://github.com/oals/portfolioBank/assets/136543676/2d8ea14f-0b5e-4017-aa79-3bdf9e2be443)



<br>
<br>


<details>
 <summary> 사기 계좌 조회 플로우 차트
 
 </summary> 
 
![사기 계좌 조회](https://github.com/oals/portfolioBank/assets/136543676/2e337516-f6ba-4a1b-a556-6882ff2336f6)


</details>

<details>
 <summary> 사기 계좌 조회 Service
 
 </summary> 
 

       @Override
       public PageResponseDTO<ReportListDTO> searchAccount(PageRequestDTO pageRequestDTO, String suspectAccountNumber) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QReport qReport = QReport.report;


        Pageable pageable = pageRequestDTO.getPageable();

        List<Report> query = queryFactory.selectFrom(qReport)
                .where(qReport.suspectAccountNumber.eq(suspectAccountNumber)
                        .and(qReport.reportStatus.eq(true)))
                .orderBy(qReport.reportDate.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();


        List<ReportListDTO> list = new ArrayList<>();

        for(int i = 0; i < query.size(); i++){

            ReportListDTO reportListDTO = ReportListDTO.builder()
                    .reportNum(query.get(i).getReportNum())
                    .siteName(query.get(i).getSiteName())
                    .scamProductType(query.get(i).getScamProductType())
                    .depositAmount(query.get(i).getDepositAmount())
                    .reportDate(query.get(i).getReportDate())
                    .build();

            list.add(reportListDTO);

        }

        Long count = queryFactory.selectFrom(qReport)
                .where(qReport.suspectAccountNumber.eq(suspectAccountNumber)
                        .and(qReport.reportStatus.eq(true)))
                .stream().count();

        return PageResponseDTO.<ReportListDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .list(list)
                .total( count.intValue())
                .build();



       }


</details>


<UL>
  <LI>계좌 번호를 통해 사기 계좌를 조회 할 수 있습니다.</LI>
  <LI>자세히 보기 버튼을 통해 신고 정보를 확인 할 수 있습니다.</LI>
  <LI>신고자 정보는 관리자만 확인 할 수 있도록 구현했습니다.</LI>  


</UL>


<br>
<br>



<h3>사기 계좌 신고</h3>
<br>



![사기신고](https://github.com/oals/portfolioBank/assets/136543676/65482db2-4d34-4209-aa6b-76eb15e42074)





<br>
<br>


</details>

<UL>
  <LI>사기 계좌의 정보 및 사기 당했던 상황에 대해 여러 정보를 작성 할 수 있도록 구현 했습니다.</LI>
  <LI>신고 완료 시 관리자가 해당 신고 정보를 검토 후 등록 및 삭제 할 수 있도록 구현 했습니다.</LI>


</UL>


<br>
<br>




<h3>사기 계좌 신고 등록</h3>
<br>



![사기신고승인](https://github.com/oals/portfolioBank/assets/136543676/21281e69-65d6-4930-b3a5-27d8829a498b)





<br>
<br>



<UL>
  <LI>관리자가 계좌 신고 정보를 검토 후 삭제 및 등록을 할 수 있도록 구현했습니다. </LI>


</UL>


<br>
<br>






<h3>회원가입</h3>
<br>


![회원가입](https://github.com/oals/portfolioBank/assets/136543676/b8d35d05-2baa-423a-8730-c2cdbe6ae574)




<br>
<br>


<UL>
  <LI>아이디 중복 방지, SMS 인증을 통해 회원 가입이 가능하도록 구현 했습니다.</LI>
  <LI> 우편 번호 API를 사용하여 주소를 검색 할 수 있도록 구현했습니다. </LI>


</UL>


<br>
<br>


# 프로젝트를 통해 느낀 점과 소감


2023년 9월에 일주일간 개발 했었던 프로젝트를 업데이트햇다. <br>
5개월 전 그때 당시에는 괜찮게 코드를 썼다고 생각했었는데 지금 보니 엉망진창이였다. <br>
그래서 대부분의 코드를 사용하지 못했고 지웠다. <br>
공부를 계속 함으로써 성장 했기에 이렇게 느낄 수 있는 게 아닐까 싶다. <br>
<br>
앞으로는 리엑트와 깃허브를 공부해볼 생각이다.<br>
다음 프로젝트가 웹이 될 지 안드로이드가 될 지는 모르겠지만 웹을 만든다면 JSX 문법을 사용해 리엑트를 써볼 생각이다..






















