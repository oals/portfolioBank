package com.project.bank.service;
import com.project.bank.dto.PageRequestDTO;
import com.project.bank.dto.PageResponseDTO;
import com.project.bank.dto.SavingsDTO;
import com.project.bank.dto.SavingsProductDTO;
import java.util.List;

public interface SavingsService {

//
//    default SavingsDTO entityToDTO(Savings savings){
//
//
//        SavingsDTO savingsDTO = SavingsDTO.builder()
//                .accountNumber(savings.getAccount().getAccountNumber())
//                .balance(savings.getBalance())
//                .product_name(savings.getProduct_name())
//                .percent(savings.getPercent())
//                .AllBalance(savings.getAllBalance())
//                .savingsDate(savings.getSavingsDate())
//                .build();
//
//
//
//        return savingsDTO;
//    }
//
//    default Savings dtoToEntity(SavingsDTO savingsDTO){
//
//
//        Account account = Account.builder()
//                .accountNumber(savingsDTO.getAccountNumber())
//                .build();
//
//
//        Savings savings = Savings.builder()
//                .account(account)
//                .balance(savingsDTO.getBalance())
//                .product_name(savingsDTO.getProduct_name())
//                .percent(savingsDTO.getPercent())
//                .AllBalance(savingsDTO.getAllBalance())
//                .savingsDate(savingsDTO.getSavingsDate())
//                .build();
//
//
//
//        return savings;
//    }


     boolean SavingsCheckBalance(String accountNumber, String productName);  //적금시 계좌의 잔액 검사



     boolean NewSavings(String accountNumber, String productName);  //적금 신규 가입 유저

     PageResponseDTO<SavingsDTO> getMySavings(PageRequestDTO pageRequestDTO, String accountNumber, boolean isActive);


     boolean ChkSavings(String memberId,String savingsProductName);

     List<SavingsProductDTO> selectSavingsProductList();

     SavingsProductDTO selectSavingsProductDetailView(Long savingsProductId);








}
